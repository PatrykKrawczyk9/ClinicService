package pl.kurs.clinicservice.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@ComponentScan(basePackages = "pl.kurs")
@PropertySource("classpath:application-dev.properties")
@Configuration
public class BeansConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean createEmf(JpaVendorAdapter adapter, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setJpaVendorAdapter(adapter);
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
}
