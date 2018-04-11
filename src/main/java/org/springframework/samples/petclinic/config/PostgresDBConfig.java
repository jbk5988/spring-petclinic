package org.springframework.samples.petclinic.config;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(basePackages = "org.springframework.samples.petclinic.newDataStore", entityManagerFactoryRef = "postMigrationEntityManager", transactionManagerRef = "postMigrationTransactionManager")
public class PostgresDBConfig {
    @Autowired
    private Environment env;

    public PostgresDBConfig() {
        super();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean postMigrationEntityManager() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postMigrationDataSource());
        em.setPackagesToScan(new String[] { "org.springframework.samples.petclinic.newDataStore" });

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        final HashMap<String, Object> properties = new HashMap<String, Object>();
//        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.postgresdialect"));
        properties.put("hibernate.temp.use_jdbc_metadata_defaults",false);

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource postMigrationDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("postgres.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("postgres.datasource.url"));
        dataSource.setUsername(env.getProperty("postgres.datasource.username"));
        dataSource.setPassword(env.getProperty("postgres.datasource.password"));

        Resource initSchema = new ClassPathResource("db/postgres/schema.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
//        DatabasePopulatorUtils.execute(databasePopulator, dataSource);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager postMigrationTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(postMigrationEntityManager().getObject());
        return transactionManager;
    }

}



