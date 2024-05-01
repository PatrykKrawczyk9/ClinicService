package pl.kurs.clinicservice.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = "pl.kurs")
@PropertySource("classpath:application-dev.properties")
public class BeansConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean createEmf() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(getDataSource());
        emf.setJpaVendorAdapter(getJpaVendorAdapter());
        emf.setPackagesToScan("pl.kurs.clinicservice.models");
        return emf;
    }

    @Bean
    public HibernateJpaVendorAdapter getJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.H2);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        return adapter;
    }

    @Bean
    public BasicDataSource getDataSource() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:h2:mem:testdb");
        bds.setUsername("sa");
        bds.setPassword("");
        bds.setMinIdle(5);
        bds.setMaxIdle(100);
        return bds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        transactionManager.setDataSource(getDataSource());
        transactionManager.setJpaDialect(new HibernateJpaDialect());
        return transactionManager;
    }
}
