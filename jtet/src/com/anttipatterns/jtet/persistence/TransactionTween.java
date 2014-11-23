package com.anttipatterns.jtet.persistence;

import static com.anttipatterns.jtet.persistence.Persistence.SESSION;
import static com.anttipatterns.jtet.persistence.Persistence.SESSION_FACTORY;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.handler.IHandler;
import com.anttipatterns.jtet.request.IRequest;
import com.anttipatterns.jtet.response.IResponse;
import com.anttipatterns.jtet.tween.ITween;

public class TransactionTween implements ITween {

	public class TransactionHandler implements IHandler {
		private IHandler wrapped;

		public TransactionHandler(IHandler wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public IResponse handle(IRequest request) {
			ISessionFactory factory = request.getRegistry().queryProp(SESSION_FACTORY);

			try (ISession session = factory.createSession()) {
				try {
					session.begin();
					request.setProp(SESSION, session);
					IResponse rv = wrapped.handle(request);
					session.commit();
					return rv;
				}
				catch (Throwable t) {
					session.rollback();
					throw t;
				}
			}
		}
	}

	@Override
	public IHandler wrap(IHandler wrapped, Registry registry) {
		return new TransactionHandler(wrapped);
	}

}
