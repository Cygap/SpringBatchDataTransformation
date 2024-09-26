package dk.lector.lts.utils.data_transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class LoggingRegulationCodesChunkListener implements ChunkListener {

	    Logger logger = LoggerFactory.getLogger(LoggingRegulationCodesChunkListener.class);

	    @Override
	    public void beforeChunk(ChunkContext chunkContext) {
	        logger.info("Started chunk processing at: " + System.currentTimeMillis());
	    }

	    @Override
	    public void afterChunk(ChunkContext chunkContext) {
	    	long count = chunkContext.getStepContext().getStepExecution().getReadCount();
	    	logger.info("Chunk processing finished. Processed " + count + " items.");
	    }

	    @Override
	    public void afterChunkError(ChunkContext chunkContext) {
	        logger.error("afterChunkError");
	    }
}
