package com.anttipatterns.jtet.renderers;

public interface IRenderer {
	public IResponseBody render(IContext value, IContext system);
}
