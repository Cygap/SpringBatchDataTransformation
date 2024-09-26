package dk.lector.lts.utils.data_transformation;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import dk.lector.trading.common.fetchAndSaveUtils.dto.ObjectAttributeDTO;
import dk.lector.trading.persistence.entities.supplementary.CategoriesEntity;
import dk.lector.trading.persistence.entities.supplementary.ObjectAttributesEntity;
import dk.lector.trading.persistence.entities.supplementary.ObjectTypesEntity;
import dk.lector.trading.persistence.repositories.CategoriesRepository;
import dk.lector.trading.persistence.repositories.ImportLineItemsRepository;
import dk.lector.trading.persistence.repositories.ObjectAttributesEntityRepository;
import dk.lector.trading.persistence.repositories.ObjectTypesRepository;

@Component
public class RegulationCodesReader extends RepositoryItemReader<ObjectAttributeDTO> {
	private ExecutionContext executionContext;
	@Autowired
	private CategoriesRepository categoriesRepository;
	@Autowired
	private ObjectTypesRepository objectTypesRepository;
	@Autowired
	private ObjectAttributesEntityRepository objectAttributesEntityRepository;
	
	private CategoriesEntity categoriesEntity;
	private ObjectTypesEntity objectTypesEntity;
	
	RegulationCodesReader(ImportLineItemsRepository importLineItemsRepository) {
		super();
		this.setRepository(importLineItemsRepository);
		this.setMethodName("findOAdto");
		this.setPageSize(RegulationCodesCreatorBatchJob.CHUNK_SIZE);
		this.setSort(Map.of("id", Sort.Direction.ASC));
		this.setName("regulationCodesReader");
	}
	
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		categoriesEntity = categoriesRepository.findByNameAndContextAndDeletedIsFalse("REGULATION_CODES", "CODES").get(0);
		objectTypesEntity = objectTypesRepository.findByReferenceTableAndObjectType("IMPORT_LINE_ITEMS", "IMPORT_LINE_ITEMS").get(0);
		if (categoriesEntity == null || objectTypesEntity == null) {
			throw new RuntimeException("Could not find categories or object types");
		}
		this.executionContext = stepExecution.getExecutionContext();
		executionContext.put("categoriesEntity", categoriesEntity);
		executionContext.put("objectTypesEntity", objectTypesEntity);
	}
	
	@Override
	protected List<ObjectAttributeDTO> doPageRead() throws Exception {
		//Get chunk of data
		final List<ObjectAttributeDTO> curPage = super.doPageRead();
		
		if (curPage.isEmpty()) return curPage;
		//Find any attributes referring the same lines that already exist in the database
		final List<Long> referredLineIds = curPage.stream().map(ObjectAttributeDTO::getReferencedId).collect(toList());
		final List<ObjectAttributesEntity> existingObjectAttributesEntities = objectAttributesEntityRepository
				.findByObjectIdInAndCategoriesIdIdAndObjectTypesIdId(referredLineIds, categoriesEntity.getId(), objectTypesEntity.getId());
		System.out.println("Existing ObjectAttributes: " + existingObjectAttributesEntities.size());
		
		//Create a map of existing attributes by line id for easier access from the processor
		final Map<Long, List<String>> existingAttributesByLineId = existingObjectAttributesEntities.stream()
				.collect(groupingBy(ObjectAttributesEntity::getObjectId,
						mapping(ObjectAttributesEntity::getAttributeValue, toList())));
		executionContext.put("existingAttributes", existingAttributesByLineId);
		
		return curPage;
	}
}
