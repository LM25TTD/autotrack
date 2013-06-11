package com.autotrack.webmanager.config;

import java.util.Properties;

import javax.persistence.PersistenceUnit;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class AppConfig {

	@Bean(name = "sessionFactory")
	@PersistenceUnit(unitName = "autotrack")
	public SessionFactory sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		sessionFactory.setDataSource(PersistenceConfig.dataSource());
		sessionFactory.setPackagesToScan("com.autotrack.webmanager.model");

		Properties hibernateProperties = new Properties();
		hibernateProperties
				.setProperty("hibernate.connection.charSet", "UTF-8");
		hibernateProperties.setProperty("hibernate.ejb.naming_strategy",
				"org.hibernate.cfg.EJB3NamingStrategy");
		hibernateProperties.setProperty("hibernate.bytecode.provider",
				"javassist");
		hibernateProperties.setProperty("hibernate.dialect",
				"org.hibernate.dialect.PostgreSQLDialect");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.setProperty("hibernate.show_sql", "true");
		hibernateProperties.setProperty("hibernate.format_sql", "true");
		sessionFactory.setHibernateProperties(hibernateProperties);

		try {
			sessionFactory.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sessionFactory.getObject();
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory());

		return transactionManager;
	}

}
