package com.perscholas.springmvc_jpa.hibernate_session.impl;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perscholas.springmvc_jpa.hibernate_session.HibernateUtil;
import com.perscholas.springmvc_jpa.model.Course;
import com.perscholas.springmvc_jpa.model.Student;

@Component("mariaDbHibernateUtil")
public class MariaDbHibernateUtil implements HibernateUtil
{
	private SessionFactory sessionFactory;
	@Autowired
	private org.springframework.core.env.Environment env;
		
	public SessionFactory getSessionFactory() {
	    if (sessionFactory == null) {
		try
                {
		    Configuration configuration = new Configuration();
		    Properties properties = new Properties();
		    properties.put(Environment.DRIVER, env.getRequiredProperty("mariadb.driver"));
		    properties.put(Environment.URL, env.getRequiredProperty("mariadb.url"));
		    properties.put(Environment.USER, env.getRequiredProperty("mariadb.user"));
		    properties.put(Environment.PASS, env.getRequiredProperty("mariadb.password"));
		    properties.put(Environment.DIALECT, env.getRequiredProperty("mariadb.dialect"));
		    properties.put(Environment.SHOW_SQL, "true");
		    properties.put(Environment.HBM2DDL_AUTO, "update");
		    configuration.setProperties(properties);
		    configuration.addAnnotatedClass(Student.class);
		    configuration.addAnnotatedClass(Course.class);
		    StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
			.applySettings(configuration.getProperties()).build();
		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} 
                catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    return sessionFactory;
	}
}