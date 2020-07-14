package com.kakaoPay.kakaoPayTemp.common;


import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.kakaoPay.kakaoPayTemp.controller.PayPrcController;




@Configuration 
@MapperScan(basePackages = "com.kakaoPay.kakaoPayTemp.dao.*Mapper")
@EnableTransactionManagement
public class DbConfig {
	private static final Log log = LogFactory.getLog(DbConfig.class);
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
	
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		//sessionFactory.setMapperLocations(resolver.getResources("resources:**/**/*Mapper.xml"));
		sessionFactory.setMapperLocations(resolver.getResources("mappers/*Sql.xml"));
		return sessionFactory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		
		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
	 
		return sqlSessionTemplate;
	}
	
} 