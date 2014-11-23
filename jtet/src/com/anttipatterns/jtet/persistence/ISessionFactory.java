package com.anttipatterns.jtet.persistence;

public interface ISessionFactory {

	public abstract ISession createSession();

}