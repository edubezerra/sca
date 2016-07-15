package br.cefetrj.sca.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "br.cefetrj.sca.dominio.repositories")
public class PersistenceConfig {

	@Autowired
	private Environment env;

	@Value("${init-db:false}")
	private String initDatabase;

	@Bean
	public PlatformTransactionManager transactionManager() {
		EntityManagerFactory factory = entityManagerFactory().getObject();
		return new JpaTransactionManager(factory);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.TRUE);

		if (env.getProperty("hibernate.show_sql").equals("false")) {
			vendorAdapter.setShowSql(Boolean.FALSE);
		} else {
			vendorAdapter.setShowSql(Boolean.TRUE);
		}
		vendorAdapter.setShowSql(Boolean.FALSE);

		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("br.cefetrj.sca.dominio");

		Properties jpaProperties = new Properties();

		jpaProperties.put("hibernate.dialect",
				env.getProperty("hibernate.dialect"));
		
		jpaProperties.put("hibernate.hbm2ddl.auto",
				env.getProperty("hibernate.hbm2ddl.auto"));
		
		jpaProperties.put("hibernate.c3p0.timeout",
				env.getProperty("hibernate.c3p0.timeout"));
		
		jpaProperties.put("hibernate.c3p0.maxIdleTimeExcessConnections",
				env.getProperty("hibernate.c3p0.maxIdleTimeExcessConnections"));
		
		jpaProperties.put("hibernate.c3p0.validate",
				env.getProperty("hibernate.c3p0.validate"));
		
		jpaProperties.put("hibernate.c3p0.idle_test_period",
				env.getProperty("hibernate.c3p0.idle_test_period"));
		
		jpaProperties.put("hibernate.c3p0.automaticTestTable",
				env.getProperty("hibernate.c3p0.automaticTestTable"));
		
		jpaProperties.put("current_session_context_class",
				env.getProperty("current_session_context_class"));
		
		jpaProperties.put("cache.provider_class",
				env.getProperty("cache.provider_class"));
		
		jpaProperties.put("",
				env.getProperty(""));
		
		factory.setJpaProperties(jpaProperties);

		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		System.out.println("**************************" + initDatabase);
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(new ClassPathResource("db.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
		return dataSourceInitializer;
	}
}
