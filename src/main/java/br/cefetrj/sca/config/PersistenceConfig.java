package br.cefetrj.sca.config;

import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "br.cefetrj.sca.dominio", "br.cefetrj.sca.infra.cargadados",
		"br.cefetrj.sca.dominio.repositories" }, includeFilters = {
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = br.cefetrj.sca.dominio.AlunoFabrica.class) })
@EnableJpaRepositories(basePackages = "br.cefetrj.sca.dominio.repositories")
public class PersistenceConfig {

	@Value("${init-db:false}")
	private String initDatabase;

	@Bean
	public PlatformTransactionManager transactionManager() {
		EntityManagerFactory factory = entityManagerFactory();
		return new JpaTransactionManager(factory);
	}

	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(Boolean.TRUE);

		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("br.cefetrj.sca.dominio");

		Properties env = new Properties();

		try {
			InputStream propertiesInputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application.properties");
			env.load(propertiesInputStream);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		vendorAdapter.setShowSql(Boolean.FALSE);

		Properties jpaProperties = new Properties();

		vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("hibernate.show_sql")));

		jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));

		// jpaProperties.put("hibernate.hbm2ddl.auto",
		// env.getProperty("hibernate.hbm2ddl.auto"));

		// jpaProperties.put("hibernate.connection.provider_class",
		// env.getProperty("hibernate.connection.provider_class"));

		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory.getObject();
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	@Bean
	public DataSource dataSource() {

		Properties properties = new Properties();
		try {
			InputStream propertiesInputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("application.properties");
			properties.load(propertiesInputStream);

			PoolProperties p = new PoolProperties();

			p.setUrl(properties.getProperty("hibernate.connection.url"));
			p.setDriverClassName(properties.getProperty("hibernate.connection.driver_class"));
			p.setUsername(properties.getProperty("hibernate.connection.username"));
			p.setPassword(properties.getProperty("hibernate.connection.password"));

			p.setJmxEnabled(true);
			p.setTestWhileIdle(false);
			p.setTestOnBorrow(true);
			p.setValidationQuery(properties.getProperty("hibernate.connection.validation_query"));
			p.setTestOnReturn(false);
			p.setValidationInterval(30000);
			p.setTimeBetweenEvictionRunsMillis(30000);
			p.setMaxActive(100);
			p.setInitialSize(10);
			p.setMaxWait(10000);
			p.setRemoveAbandonedTimeout(60);
			p.setMinEvictableIdleTimeMillis(30000);
			p.setMinIdle(10);
			p.setLogAbandoned(true);
			
			p.setRemoveAbandoned(false);
			
//			ResetAbandonedTimer r;
			
			p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
					+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
			DataSource datasource = new DataSource();
			datasource.setPoolProperties(p);
			return datasource;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// Properties properties = new Properties();
		// try {
		// InputStream propertiesInputStream =
		// Thread.currentThread().getContextClassLoader()
		// .getResourceAsStream("application.properties");
		// properties.load(propertiesInputStream);
		//
		// BasicDataSource dataSource = new BasicDataSource();
		//
		// dataSource.setDriverClassName(properties.getProperty("hibernate.connection.driver_class"));
		// dataSource.setUrl(properties.getProperty("hibernate.connection.url"));
		// dataSource.setUsername(properties.getProperty("hibernate.connection.username"));
		// dataSource.setPassword(properties.getProperty("hibernate.connection.password"));
		//
		// return dataSource;
		// } catch (Exception e) {
		// e.printStackTrace();
		// return null;
		// }
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		System.out.println("**************************" + initDatabase);
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
		return dataSourceInitializer;
	}
}
