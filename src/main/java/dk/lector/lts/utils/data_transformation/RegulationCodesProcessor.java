package dk.lector.lts.utils.data_transformation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import dk.lector.trading.common.fetchAndSaveUtils.dto.ObjectAttributeDTO;
import dk.lector.trading.persistence.entities.supplementary.CategoriesEntity;
import dk.lector.trading.persistence.entities.supplementary.ObjectAttributesEntity;
import dk.lector.trading.persistence.entities.supplementary.ObjectTypesEntity;
@Component
public class RegulationCodesProcessor implements ItemProcessor<ObjectAttributeDTO, ObjectAttributesEntity>{
	private ExecutionContext executionContext;
	private CategoriesEntity categoriesEntity;
	private ObjectTypesEntity objectTypesEntity;
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		this.executionContext = stepExecution.getExecutionContext();
		this.categoriesEntity = Optional.ofNullable(executionContext.get("categoriesEntity"))
				.map(ce -> (CategoriesEntity) ce)
				.orElseThrow(() -> new RuntimeException("Could not find categories entity"));
		this.objectTypesEntity = Optional.ofNullable(executionContext.get("objectTypesEntity"))
				.map(ce -> (ObjectTypesEntity) ce)
				.orElseThrow(() -> new RuntimeException("Could not find object types entity"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ObjectAttributesEntity process(ObjectAttributeDTO oa) throws Exception {
		
		if (executionContext.containsKey("existingAttributes") 
				&& ((Map<Long, List<String>>)executionContext.get("existingAttributes")).containsKey(oa.getReferencedId())
				&& ((Map<Long, List<String>>)executionContext.get("existingAttributes")).get(oa.getReferencedId()).stream()
					.anyMatch(ea -> ea.equalsIgnoreCase(oa.getAttributeValue()))) {
			System.out.println("ObjectAttribute " + oa.getAttributeValue() + " already exists for lineId: "
					+ oa.getReferencedId());
			return null;
		}
		
		final ObjectAttributesEntity objectAttributesEntity = ObjectAttributesEntity.builder()
				.categoriesId(categoriesEntity).objectTypesId(objectTypesEntity)
				.objectId(oa.getReferencedId())
				.attributeValue(oa.getAttributeValue())
				.build();
		objectAttributesEntity.setCreatedBy(oa.getCreatedBy());
		objectAttributesEntity.setCreatedDate(oa.getCreatedDate());
		
		return objectAttributesEntity;
	}

}
