package dk.lector.lts.utils.data_transformation;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.lector.trading.persistence.entities.supplementary.ObjectAttributesEntity;
import dk.lector.trading.persistence.repositories.ObjectAttributesEntityRepository;
import jakarta.annotation.Nonnull;

@Component
public class RegulationCodesWriter implements ItemWriter<ObjectAttributesEntity> {
	
	@Autowired
	private ObjectAttributesEntityRepository objectAttributesEntityRepository;
	@Override
	public void write(@Nonnull Chunk<? extends ObjectAttributesEntity> regulationCodes) throws InterruptedException {
		String message = MessageFormat.format("{0} entities for Import lines from id={1} to id={2}.", 
				regulationCodes.size(),
				regulationCodes.isEmpty() ? null : regulationCodes.getItems().get(0).getObjectId(),
				regulationCodes.isEmpty() ? null : regulationCodes.getItems().get(regulationCodes.size() - 1).getObjectId());
		Logger logger = LoggerFactory.getLogger(RegulationCodesWriter.class);
		logger.info("Starting saving " + message);
		
		/* Test-write only one entity
		 * regulationCodes.getItems().stream()
		 * .filter(e -> e.getObjectId().equals(5551L)).findFirst()
		 * .ifPresent(objectAttributesEntityRepository::save);*/
		objectAttributesEntityRepository.saveAll(regulationCodes.getItems());
		
		logger.info("Saved " + message);
	}

}
