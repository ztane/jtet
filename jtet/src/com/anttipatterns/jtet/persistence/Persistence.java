package com.anttipatterns.jtet.persistence;

import static com.anttipatterns.jtet.props.Key.key;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.props.Key;
import com.anttipatterns.jtet.request.IRequest;

public class Persistence {
	public static final Key<IRequest, ISession> SESSION =
		key(ISession.class, "session");

	public static final Key<Registry, ISessionFactory> SESSION_FACTORY =
		key(ISessionFactory.class, "sessionFactory");
}
