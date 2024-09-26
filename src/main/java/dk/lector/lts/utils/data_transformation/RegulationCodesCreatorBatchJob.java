package dk.lector.lts.utils.data_transformation;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import dk.lector.trading.common.fetchAndSaveUtils.dto.ObjectAttributeDTO;
import dk.lector.trading.persistence.entities.supplementary.ObjectAttributesEntity;
import dk.lector.trading.persistence.repositories.ImportLineItemsRepository;

@Configuration
@EntityScan(basePackages = "dk.lector.trading.persistence.entities")
@EnableJpaRepositories(basePackages = "dk.lector.trading.persistence.repositories")
public class RegulationCodesCreatorBatchJob {
	
	static final int CHUNK_SIZE = 1000;
	
	@Bean
	RegulationCodesReader repositoryReader(ImportLineItemsRepository importLineItemsRepository) {
		RegulationCodesReader regulationCodesRepositoryReader = new RegulationCodesReader(
				importLineItemsRepository);
		
		//reading only non-deleted ImportLineItems with non-empty regulationCodes
		return regulationCodesRepositoryReader;
	}
	
	@Bean
	Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("repositoryReader")RepositoryItemReader<ObjectAttributeDTO> itemReader, //Specifically choose repositoryReader bean which returns custom RegulationCodesReader
			RegulationCodesProcessor processor, RegulationCodesWriter itemWriter) {
		return new JobBuilder("regulationCodesAttributesCreator", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(new StepBuilder("step1", jobRepository)
				.<ObjectAttributeDTO, ObjectAttributesEntity>chunk(CHUNK_SIZE, transactionManager)
				.listener(new LoggingRegulationCodesChunkListener())
				.reader(itemReader)
				.processor(processor)
				.writer(itemWriter)
				.build())
			.build();
	}
	
	
}
