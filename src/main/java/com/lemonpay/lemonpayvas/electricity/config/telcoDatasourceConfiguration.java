package com.lemonpay.lemonpayvas.electricity.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "telcoEntityManagerFactory",
        transactionManagerRef = "telcoTransactionManager",
        basePackages = { "com.etranzact.vasgate.electricity.telco.repo" })
public class telcoDatasourceConfiguration {
    @Bean(name="telcoProperties")
    @ConfigurationProperties("spring.datasource.telcodb")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name="telcoDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.telcodb")
    public DataSource datasource(@Qualifier("telcoProperties") DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name="telcoEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
            (EntityManagerFactoryBuilder builder,
             @Qualifier("telcoDatasource") DataSource dataSource){

        return builder.dataSource(dataSource)
                .packages("com.etranzact.vasgate.electricity.telco.entity")
                .persistenceUnit("entity").build();
    }

    @Bean(name = "telcoTransactionManager")
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager(
            @Qualifier("telcoEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
