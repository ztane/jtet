package com.anttipatterns.jtet.persistence;

import javax.persistence.EntityManager;

import org.jinq.jpa.JinqJPAStreamProvider;
import org.jinq.orm.stream.JinqStream;

public class Session implements ISession, AutoCloseable {
	private EntityManager em;
	private JinqJPAStreamProvider streams;

	public Session(EntityManager em, JinqJPAStreamProvider provider) {
		this.em = em;
		this.streams = provider;
	}
	
	@Override
	public EntityManager em() {
		return em;
	}

	@Override
	public <T> JinqStream<T> stream(Class<T> cls) {
		return streams.streamAll(em, cls);
	}	
	
	public void begin() {
		em.getTransaction().begin();
	}
	
	public void commit() {
		em.getTransaction().commit();
	}
	
	public void rollback() {
		em.getTransaction().rollback();
	}
	
	public void close() {
		em.close();
	}

	@Override
	public void add(Object... objects) {
		for (Object o: objects) {
			em.persist(o);
		}
	}
}
