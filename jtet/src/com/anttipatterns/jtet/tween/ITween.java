package com.anttipatterns.jtet.tween;

import com.anttipatterns.jtet.config.Registry;
import com.anttipatterns.jtet.handler.IHandler;

public interface ITween {
	public IHandler wrap(IHandler wrapped, Registry registry);
}
