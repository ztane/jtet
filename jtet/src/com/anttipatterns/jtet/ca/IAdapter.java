package com.anttipatterns.jtet.ca;

public interface IAdapter<T, U> {
	public T adapt(U adapted);
}
