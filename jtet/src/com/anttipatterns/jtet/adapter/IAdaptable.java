package com.anttipatterns.jtet.adapter;


public interface IAdaptable {
	public default <T> T queryAdapter(Class <? extends T> type) {
		return queryAdapter(type, "");
	}
	
	public default <T> T getAdapter(Class <? extends T> type) throws UnadaptableException {
		return getAdapter(type, "");		
	}

	public default <T> T queryAdapter(Class <? extends T> type, String name) {
		return null;
	}
	
	public default <T> T getAdapter(Class <? extends T> type, String name) throws UnadaptableException {
		T adapter = queryAdapter(type, name);
		if (adapter == null) {
			throw new UnadaptableException(this, type, name);
		}
		
		return adapter;
	}
}
