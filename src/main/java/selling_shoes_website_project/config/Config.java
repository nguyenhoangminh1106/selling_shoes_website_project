package selling_shoes_website_project.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan( basePackages = {"selling_shoes_website_project.controller"})
public class Config {
	@Bean
	/**
	 * INIT DATASOURCE
	 * 
	 * @return
	 */
    public DataSource dataSource() {
     DriverManagerDataSource dataSource = new
    DriverManagerDataSource();
 
     dataSource.setDriverClassName("com.mysql.jdbc.Driver");
     dataSource.setUrl("jdbc:mysql://localhost:3306/selling_shoes_website_project");
     dataSource.setUsername("root");
     dataSource.setPassword("mysql");
 
     return dataSource;
    }
     
    @Bean
    /**
     * INIT JDBC CONNECTION
     * 
     * @param dataSource
     * @return
     */
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
     return new JdbcTemplate(dataSource);
    }
    
    @Bean(name = "multipartResolver")
    /**
     * INIT FILE RESOLVER
     * 
     * @return
     */
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10000000);
        return multipartResolver;
    }
   
}
