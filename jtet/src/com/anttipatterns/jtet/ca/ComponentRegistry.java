package com.anttipatterns.jtet.ca;

import java.util.HashMap;
import java.util.Map;

import com.anttipatterns.jtet.utils.ClassUtils;

public class ComponentRegistry {
	static IAdapter<?, ?> NOT_FOUND = x -> null;
	static class AdapterSet {
		Map<Class<?>, IAdapter<?, ?>> registrations = new HashMap<>();
		Map<Class<?>, IAdapter<?, ?>> cache = new HashMap<>();

		private IAdapter<?, ?> doGet(Class<?> cls) {
			IAdapter<?, ?> value = registrations.get(cls);
			if (value != null) {
				return value;
			}
			
			value = cache.get(cls);
			return value;			
		}

		
		public IAdapter<?, ?> get(Class<?> cls) {
			IAdapter<?, ?> found = doGet(cls);
			if (found != null) {
				if (found == NOT_FOUND) {
					return null;
				}
				return found;
			}

			for (Class<?> c: ClassUtils.getAllSupers(cls)) {
				found = doGet(c);

				if (found != null && found != NOT_FOUND) {
					cache.put(cls, found);
					return found;
				}
			}
			
			cache.put(cls, NOT_FOUND);
			return null;
		}
		
		public void put(Class<?> cls, IAdapter<?, ?> adapter) {
			registrations.put(cls, adapter);
			cache.clear();
		}
	}
	
	Map<AdapterKey, AdapterSet> adapters = new HashMap<>(); 

	static class AdapterKey {
		Class<?> target;
		String name;
		
		public AdapterKey(Class<?> target, String name) {
			super();
			this.target = target;
			this.name = name;
		}

		@Override
		public int hashCode() {
			return name.hashCode() + 31 * target.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			AdapterKey other = (AdapterKey) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (target == null) {
				if (other.target != null) {
					return false;
				}
			} else if (!target.equals(other.target)) {
				return false;
			}
			return true;
		}
		
	}
	
	static AdapterKey key(Class<?> target, String name) {
		return new AdapterKey(target, name);
	}

	public <T, U> void registerAdapter(IAdapter<T, U> adapter, Class<U> from, Class<T> to) {
		registerAdapter(adapter, from, to, "");
	}
	
	public <T, U> void registerAdapter(IAdapter<T, U> adapter, Class<U> from, Class<T> to, String name) {
		AdapterKey key = key(to, name);
		AdapterSet adapterSet = adapters.get(key);
		if (adapterSet == null) {
			adapterSet = new AdapterSet();
			adapters.put(key, adapterSet);
		}
		
		adapterSet.put(from, adapter);
	}
	
	public <T, U> T queryAdapter(Class<? extends T> adapterInterface, U adapted) {
		return queryNamedAdapter(adapterInterface, adapted, "", null);
	}

	public <T, U> T queryAdapter(Class<? extends T> adapterInterface, U adapted, T defaultValue) {
		return queryNamedAdapter(adapterInterface, adapted, "", defaultValue);
	}

	protected <T, U> IAdapter<Object, Object> findAdapter(Class<? extends T> adapterInterface, String name, U adapted) {
		AdapterSet adapterSet = adapters.get(key(adapterInterface, name));
		if (adapterSet == null) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		IAdapter<Object, Object> adapter = (IAdapter<Object, Object>)adapterSet.get(adapted.getClass());
		return adapter;
	}
	
	public <T, U> T queryNamedAdapter(Class<? extends T> adapterInterface, U adapted, String name, T defaultValue) {
		IAdapter<Object, Object> adapter = findAdapter(adapterInterface, name, adapted);
		return adapter != null ? adapterInterface.cast(adapter.adapt(adapted)) : defaultValue;
	}

	public <T, U> T queryNamedAdapter(Class<? extends T> adapterInterface, U adapted, String name) {
		return queryNamedAdapter(adapterInterface, adapted, name, null);
	}
	
	public <T, U> T getAdapter(Class<T> adapterInterface, U adapted) throws ComponentLookupException {
		return getNamedAdapter(adapterInterface, adapted, "");
	}

	public <T, U> T getNamedAdapter(Class<T> adapterInterface, U adapted, String name) throws ComponentLookupException {
		IAdapter<Object, Object> adapter = findAdapter(adapterInterface, name, adapted);
		if (adapter == null) {
			throw new ComponentLookupException(adapterInterface, adapted, name);
		}

		return adapterInterface.cast(adapter.adapt(adapted));
 	}
}
