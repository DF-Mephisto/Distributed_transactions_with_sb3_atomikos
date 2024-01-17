package com.example.atomikos.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.example.atomikos.hibinaction.HAUser;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        entityManagerFactoryRef = "hibinactionEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager",
        basePackages = {"com.example.atomikos.hibinaction"}
)
public class HibInActionConfig {

    @Value("${hibinaction.datasource.jdbcUrl}")
    String dbUrl;

    @Value("${hibinaction.datasource.username}")
    String username;

    @Value("${hibinaction.datasource.password}")
    String password;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "create");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("javax.persistence.transactionType", "JTA");
        return jpaProperties;
    }

    @Bean(name = "hibinactionEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), jpaProperties(), null
        );
    }

    @Bean(name = "hibinactionDataSource")
    public DataSource dataSource() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(dbUrl);
        pgxaDataSource.setUser(username);
        pgxaDataSource.setPassword(password);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgxaDataSource);
        xaDataSource.setUniqueResourceName("xa_hibinaction");
        xaDataSource.setMaxPoolSize(30);
        return xaDataSource;
    }

    @Bean(name = "hibinactionEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("hibinactionEntityManagerFactoryBuilder") EntityManagerFactoryBuilder entityManagerFactoryBuilder,
            @Qualifier("hibinactionDataSource") DataSource dataSource
    ) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages(HAUser.class)
                .persistenceUnit("hibinactionDataSource")
                .properties(jpaProperties())
                .jta(true)
                .build();
    }
}