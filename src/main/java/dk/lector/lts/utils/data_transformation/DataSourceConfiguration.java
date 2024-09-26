package dk.lector.lts.utils.data_transformation;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfiguration {
	@Value("${mainDatasource.driver}") 
    private String mainDatasourceDriver;
    @Value("${mainDatasource.url}") 
    private String mainDatasourceUrl;
    @Value("${mainDatasource.username}") 
    private String mainDatasourceUsername;
    @Value("${mainDatasource.password}") 
    private String mainDatasourcePassword;
	
	@Value("${batchDatasource.driver}") 
    private String batchDatasourceDriver;
    @Value("${batchDatasource.url}") 
    private String batchDatasourceUrl;
    @Value("${batchDatasource.username}") 
    private String batchDatasourceUsername;
    @Value("${batchDatasource.password}") 
    private String batchDatasourcePassword;
	
	/*@Bean
	JdbcTransactionManager transactionManager(DataSource dataSource) {
		return new JdbcTransactionManager(dataSource);
	}*/
	@Bean
    @Primary
    // @Primary assigns a higher precedence to the annotated bean 
    // during injection when the class has multiple beans of same 
    // type, in this case you have two beans of the DataSource type.
    DataSource mainDatasource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(mainDatasourceDriver);
        config.setJdbcUrl(mainDatasourceUrl);
        config.setUsername(mainDatasourceUsername);
        config.setPassword(mainDatasourcePassword);
        return new HikariDataSource(config);
    }
	/**
	 * This is the datasource for the batch job - Using in memory H2 database to
	 * avoid polluting prod DB with batch tables. The batch job will create the
	 * tables it needs in this in memory DB. It should be acceptable as long as we
	 * run the batches once in a while.
	 * 
	 * @return IN memory H2 datasource for batch job
	 */
	@Bean
    @BatchDataSource
    // @BatchDataSource specifies that this bean will be used as the 
    // data source for the ItemReader during batch processing
    DataSource batchDatasource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(batchDatasourceDriver);
        config.setJdbcUrl(batchDatasourceUrl);
        config.setUsername(batchDatasourceUsername);
        config.setPassword(batchDatasourcePassword);
        return new HikariDataSource(config);
    }
}
