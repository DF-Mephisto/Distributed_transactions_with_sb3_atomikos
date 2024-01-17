package com.example.atomikos.config;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.example.atomikos.hibinaction.HAUser;
import com.example.atomikos.hibtest.HTUser;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "hibtestEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager",
        basePackages = {"com.example.atomikos.hibtest"}
)
public class HibTestConfig {

    @Value("${hibtest.datasource.jdbcUrl}")
    String dbUrl;

    @Value("${hibtest.datasource.username}")
    String username;

    @Value("${hibtest.datasource.password}")
    String password;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("javax.persistence.transactionType", "JTA");
        return jpaProperties;
    }

    @Bean(name = "hibtestEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties(), null
        );
    }

    @Bean(name = "hibtestDataSource")
    public DataSource dataSource() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(dbUrl);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgxaDataSource);
        xaDataSource.setUniqueResourceName("xa_hibtest");
        xaDataSource.setMaxPoolSize(30);
        return xaDataSource;
    }

    @Bean(name = "hibtestEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("hibtestEntityManagerFactoryBuilder") EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("hibtestDataSource") DataSource dataSource
    ) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages(HTUser.class)
                .persistenceUnit("hibtestDataSource")
                .properties(jpaProperties())
                .jta(true)
                .build();
    }
}