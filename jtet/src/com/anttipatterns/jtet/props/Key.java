package com.anttipatterns.jtet.props;


public class Key<Target, T> {
	private Class<? extends T> keyType;
	private String keyName;
	
	private Key(Class<? extends T> keyType, String keyName) {
		if (keyName == null) {
			keyName = "";
		}
		this.keyType = keyType;
		this.keyName = keyName;
	}
	
	public static <Target, T> Key<Target, T> key(Class<? extends T> keyType, String keyName) {
		return new Key<Target, T>(keyType, keyName);
	}
	
	public T cast(Object value) {
		return keyType.cast(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
		result = prime * result + ((keyType == null) ? 0 : keyType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		@SuppressWarnings("rawtypes")
		Key other = (Key) obj;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		if (keyType == null) {
			if (other.keyType != null)
				return false;
		} else if (!keyType.equals(other.keyType))
			return false;
		return true;
	}
}
