package com.autotrack.webmanager.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class PersistenceConfig {

	@Bean
	public static DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost/dbAutoTrack");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");
		return dataSource;
	}
	/*
	@Bean
    public static DataSource dataSource() {
        CloudEnvironment cloudEnvironment = new CloudEnvironment();
        RdbmsServiceInfo serviceInfo = cloudEnvironment.getServiceInfo("dbAutoTrack", RdbmsServiceInfo.class);
        RdbmsServiceCreator serviceCreator = new RdbmsServiceCreator();
        return serviceCreator.createService(serviceInfo);
    }*/

}
