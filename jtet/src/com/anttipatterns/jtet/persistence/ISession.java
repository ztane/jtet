package com.anttipatterns.jtet.persistence;

import javax.persistence.EntityManager;

import org.jinq.orm.stream.JinqStream;

public interface ISession extends AutoCloseable {
	public EntityManager em();
	public <T> JinqStream<T> stream(Class<T> cls);
	public default <T> JinqStream<T> query(Class<T> cls) {
		return stream(cls);
	}
	public void begin();
	public void commit();
	public void rollback();
	public void close();
	public void add(Object... object);
}
