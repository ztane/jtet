package com.anttipatterns.jtet.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jinq.jpa.JinqJPAStreamProvider;

public class SessionFactory implements ISessionFactory {
	private JinqJPAStreamProvider streams;
	private EntityManagerFactory factory;

	public SessionFactory(EntityManagerFactory factory) {
		this.factory = factory;
		streams = new JinqJPAStreamProvider(factory);
	}
	
	@Override
	public ISession createSession() {
		EntityManager entityManager = factory.createEntityManager();

		try {
			return new Session(entityManager, streams);
		}
		catch (Throwable t) {
			entityManager.close();
			throw t;
		}
	}
}
