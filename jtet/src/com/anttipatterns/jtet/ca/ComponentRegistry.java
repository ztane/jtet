package com.anttipatterns.jtet.ca;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.anttipatterns.jtet.utils.ClassUtils;


/**
 * The AdapterRegistry is a way to do multidispatch based on 
 * argument types.
 * 
 * @author ztane
 */
public class ComponentRegistry {
	/**
	 * Creates a new AdapterRegistry
	 */
	public ComponentRegistry() {}
	
	/**
	 * Lexical int array comparison. 
	 * @param left the first array
	 * @param right the second array
	 * @return -1 if the first is lexically before the second; 1 if the first is lexically after the second; 0 if the arrays are equal
	 */
    static int compare(int[] left, int[] right) {
    	final int ll = left.length, rl = right.length;
    	final int max = ll < rl ? ll : rl;
    	for (int i = 0; i < max; i++) {
            if (left[i] != right[i]) {
            	return left[i] < right[i] ? -1 : 1;
            }
        }
        return ll == rl ? 0 : ll < rl ? -1 : 1;
    }
	
    /**
     * The base class for adapter argument type tuple
	 *
     * @author ztane
     */
	static class ArgType {
		private final Class<?>[] argTypes;

		public ArgType(Class<?>... argTypes) {
			this.argTypes = argTypes;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(argTypes);
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
			ArgType other = (ArgType) obj;
			if (!Arrays.equals(argTypes, other.argTypes))
				return false;
			return true;
		}
		public boolean areAssignableFrom(ArgType arguments) {
			int size = argTypes.length;
			if (size != arguments.argTypes.length) {
				return false;
			}
			for (int i = 0; i < size; i++) {
				if (! argTypes[i].isAssignableFrom(arguments.argTypes[i])) {
					return false;
				}
			}	
			return true;
		}
		public int[] getCastDistancesTo(ArgType other) {
			final int[] rv = new int[this.argTypes.length];
			for (int i = 0; i < rv.length; i ++) {
				rv[i] = getCastDistance(
					other.argTypes[i],
					this.argTypes[i]
				);
			}
			return rv;
		}
		public static ArgType of(Arguments args) {
			return new ArgTypeVarArity(args);
		}
	}

	static final Map<SimpleEntry<Class<?>, Class<?>>, Integer> distances = new HashMap<>();
	static int getCastDistance(Class<?> from, Class<?> to) {
		return distances.computeIfAbsent(new SimpleEntry<>(from, to), entry -> {
			Class<?> keyFrom = entry.getKey();
			Class<?> keyTo = entry.getValue();
			if (keyFrom == keyTo) {
				return 0;
			}
			int sign = 1;
			
			if (! keyTo.isAssignableFrom(keyFrom)) {
				sign = -1;
				Class<?> tmp = keyFrom;
				keyFrom = keyTo;
				keyTo = tmp;
				if (! keyTo.isAssignableFrom(keyFrom)) {
					throw new ClassCastException(String.format("%s and %s are not in the same class hierarchy branch", keyFrom, keyTo));
				}
			}
			int result = ClassUtils.getAllSupers(keyFrom).indexOf(keyTo);
			assert result >= 0;
			return (result + 1) * sign;
		});
	}
	
	/**
	 * An internal concrete ArgType class
	 * 
	 * @author ztane
	 */
	private final static class ArgTypeVarArity extends ArgType {
		public ArgTypeVarArity(Arguments args) {
			super(getArgTypes(args.args));
		}

		private static Class<?>[] getArgTypes(Object[] args) {
			Class<?>[] argTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			return argTypes;
		}		
	}
	
	public final static class ArgTypeArity0 extends ArgType {
		public ArgTypeArity0() {
			super();
		}
	}

	public final static class ArgTypeArity1<A1> extends ArgType {
		public ArgTypeArity1(Class<? extends A1> a1)
		{
			super(a1);
		}
	}

	public final static class ArgTypeArity2<A1, A2> extends ArgType {
		public ArgTypeArity2(
				Class<? extends A1> a1,
				Class<? extends A2> a2)
		{
			super(a1, a2);
		}
	}

	public final static class ArgTypeArity3<A1, A2, A3> extends ArgType {
		public ArgTypeArity3(
				Class<? extends A1> a1,
				Class<? extends A2> a2,
				Class<? extends A3> a3)
		{
			super(a1, a2, a3);
		}
	}

	public final static class ArgTypeArity4<A1, A2, A3, A4> extends ArgType {
		public ArgTypeArity4(
				Class<? extends A1> a1,
				Class<? extends A2> a2,
				Class<? extends A3> a3,
				Class<? extends A4> a4) 
		{
			super(a1, a2, a3, a4);
		}
	}

	public final static class ArgTypeArity5<A1, A2, A3, A4, A5> extends ArgType {
		public ArgTypeArity5(
				Class<? extends A1> a1,
				Class<? extends A2> a2,
				Class<? extends A3> a3,
				Class<? extends A4> a4,
				Class<? extends A5> a5) 
		{
			super(a1, a2, a3, a4, a5);
		}
	}
	public final static class ArgTypeArity6<A1, A2, A3, A4, A5, A6> extends ArgType {
		public ArgTypeArity6(
				Class<? extends A1> a1,
				Class<? extends A2> a2,
				Class<? extends A3> a3,
				Class<? extends A4> a4,
				Class<? extends A5> a5,
				Class<? extends A6> a6) 
		{
			super(a1, a2, a3, a4, a5, a6);
		}
	}
	public final static class ArgTypeArity7<A1, A2, A3, A4, A5, A6, A7> extends ArgType {
		public ArgTypeArity7(
				Class<? extends A1> a1,
				Class<? extends A2> a2,
				Class<? extends A3> a3,
				Class<? extends A4> a4,
				Class<? extends A5> a5,
				Class<? extends A6> a6,
				Class<? extends A7> a7) 
		{
			super(a1, a2, a3, a4, a5, a6, a7);
		}
	}
	public final static class ArgTypeArity8<A1, A2, A3, A4, A5, A6, A7, A8> extends ArgType {
		public ArgTypeArity8(
				Class<? extends A1> a1,
				Class<? extends A2> a2,
				Class<? extends A3> a3,
				Class<? extends A4> a4,
				Class<? extends A5> a5,
				Class<? extends A6> a6,
				Class<? extends A7> a7,
				Class<? extends A8> a8) 
		{
			super(a1, a2, a3, a4, a5, a6, a7, a8);
		}
	}

	static interface Adapter {
	}

	public static interface AdapterArity0<Result> extends Adapter {
		public Result adapt();
	}
	
	public static interface AdapterArity1<Result, A1> extends Adapter {
		public Result adapt(A1 a1);
	}

	public static interface AdapterArity2<Result, A1, A2> extends Adapter {
		public Result adapt(A1 a1, A2 a2);
	}

	public static interface AdapterArity3<Result, A1, A2, A3> extends Adapter {
		public Result adapt(A1 a1, A2 a2, A3 a3);
	}

	public static interface AdapterArity4<Result, A1, A2, A3, A4> extends Adapter {
		public Result adapt(A1 a1, A2 a2, A3 a3, A4 a4);
	}

	public static interface AdapterArity5<Result, A1, A2, A3, A4, A5> extends Adapter {
		public Result adapt(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5);
	}

	public static interface AdapterArity6<Result, A1, A2, A3, A4, A5, A6> extends Adapter {
		public Result adapt(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6);
	}
	
	public static interface AdapterArity7<Result, A1, A2, A3, A4, A5, A6, A7> extends Adapter {
		public Result adapt(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7);
	}
	
	public static interface AdapterArity8<Result, A1, A2, A3, A4, A5, A6, A7, A8> extends Adapter {
		public Result adapt(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7, A8 a8);
	}

	public static ArgTypeArity0 argTypes() {
		return new ArgTypeArity0();
	}

	public static <A1> ArgTypeArity1<A1> argTypes(
			Class<? extends A1> a1
	) {
		return new ArgTypeArity1<>(a1);
	}

	public static <A1, A2> ArgTypeArity2<A1, A2> argTypes(
		Class<? extends A1> a1,
		Class<? extends A2> a2
	) {
		return new ArgTypeArity2<>(a1, a2);
	}

	public static <A1, A2, A3> ArgTypeArity3<A1, A2, A3> argTypes(
		Class<? extends A1> a1,
		Class<? extends A2> a2,
		Class<? extends A3> a3
	) 
	{
		return new ArgTypeArity3<>(a1, a2, a3);
	}

	public static <A1, A2, A3, A4> ArgTypeArity4<A1, A2, A3, A4> argTypes(
			Class<? extends A1> a1,
			Class<? extends A2> a2,
			Class<? extends A3> a3,
			Class<? extends A4> a4
		) 
	{
		return new ArgTypeArity4<>(a1, a2, a3, a4);
	}	

	public static <A1, A2, A3, A4, A5> ArgTypeArity5<A1, A2, A3, A4, A5> argTypes(
			Class<? extends A1> a1,
			Class<? extends A2> a2,
			Class<? extends A3> a3,
			Class<? extends A4> a4,
			Class<? extends A5> a5
		) 
	{
		return new ArgTypeArity5<>(a1, a2, a3, a4, a5);
	}	

	public static <A1, A2, A3, A4, A5, A6> ArgTypeArity6<A1, A2, A3, A4, A5, A6> argTypes(
			Class<? extends A1> a1,
			Class<? extends A2> a2,
			Class<? extends A3> a3,
			Class<? extends A4> a4,
			Class<? extends A5> a5,
			Class<? extends A6> a6
		) 
	{
		return new ArgTypeArity6<>(a1, a2, a3, a4, a5, a6);
	}	

	public static <A1, A2, A3, A4, A5, A6, A7> ArgTypeArity7<A1, A2, A3, A4, A5, A6, A7> argTypes(
			Class<? extends A1> a1,
			Class<? extends A2> a2,
			Class<? extends A3> a3,
			Class<? extends A4> a4,
			Class<? extends A5> a5,
			Class<? extends A6> a6,
			Class<? extends A7> a7
		) 
	{
		return new ArgTypeArity7<>(a1, a2, a3, a4, a5, a6, a7);
	}	

	public static <A1, A2, A3, A4, A5, A6, A7, A8> ArgTypeArity8<A1, A2, A3, A4, A5, A6, A7, A8> argTypes(
			Class<? extends A1> a1,
			Class<? extends A2> a2,
			Class<? extends A3> a3,
			Class<? extends A4> a4,
			Class<? extends A5> a5,
			Class<? extends A6> a6,
			Class<? extends A7> a7,
			Class<? extends A8> a8
		) 
	{
		return new ArgTypeArity8<>(a1, a2, a3, a4, a5, a6, a7, a8);
	}	

	static class AdapterRegistration {
		public MethodHandle handle;
		public Adapter adapter;
		public RegistrationKey registrationKey;
		public Object adapt(Object... arguments) {
			try {
				return handle.invokeWithArguments(arguments);
			} catch (Throwable e) {
				throw new RuntimeException("Unable to adapt, exception was thrown", e);
			}
		}
	};

	public <Result> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity0 argTypes,
		AdapterArity0<Result> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 0);
	}
	
	public <Result, A1> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity1<A1> argTypes,
		AdapterArity1<Result, A1> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 1);
	}
	
	public <Result, A1, A2> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity2<A1, A2> argTypes,
		AdapterArity2<Result, A1, A2> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 2);
	}

	public <Result, A1, A2, A3> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity3<A1, A2, A3> argTypes,
			AdapterArity3<Result, A1, A2, A3> adapter
		) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 3);
	}

	public <Result, A1, A2, A3, A4> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity4<A1, A2, A3, A4> argTypes,
		AdapterArity4<Result, A1, A2, A3, A4> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 4);
	}

	public <Result, A1, A2, A3, A4, A5> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity5<A1, A2, A3, A4, A5> argTypes,
		AdapterArity5<Result, A1, A2, A3, A4, A5> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 5);
	}

	public <Result, A1, A2, A3, A4, A5, A6> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity6<A1, A2, A3, A4, A5, A6> argTypes,
		AdapterArity6<Result, A1, A2, A3, A4, A5, A6> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 6);
	}

	public <Result, A1, A2, A3, A4, A5, A6, A7> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity7<A1, A2, A3, A4, A5, A6, A7> argTypes,
		AdapterArity7<Result, A1, A2, A3, A4, A5, A6, A7> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 7);
	}

	public <Result, A1, A2, A3, A4, A5, A6, A7, A8> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity8<A1, A2, A3, A4, A5, A6, A7, A8> argTypes,
		AdapterArity8<Result, A1, A2, A3, A4, A5, A6, A7, A8> adapter
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, null, 8);
	}

	@SuppressWarnings("unchecked")
	private static Class<Adapter>[] adapterTypes = new Class[] {
		AdapterArity0.class,
		AdapterArity1.class,
		AdapterArity2.class,
		AdapterArity3.class,
		AdapterArity4.class,
		AdapterArity5.class,
		AdapterArity6.class,
		AdapterArity7.class,
		AdapterArity8.class,
	};
	
	private void doRegisterAdapter(Class<?> resultClass, ArgType argTypes, Adapter adapter, String name, int arity) {
		if (arity > 8 || arity != argTypes.argTypes.length) {
			throw new RuntimeException("Unable to register adapter: too large arity or internal error");
		}

		if (adapter == null) {
			doUnregister(resultClass, argTypes, name);
			return;
		}

		if (name == null) {
			name = "";
		}
		
		Class<Adapter> atType = adapterTypes[arity];
		if (! atType.isAssignableFrom(adapter.getClass())) {
			throw new RuntimeException("Adapter does not extend the required class!");
		}
		
		MethodHandle handle;
		try {
			Class<?>[] adapterArgTypes = new Class[arity];
			Arrays.fill(adapterArgTypes, Object.class);
			handle = MethodHandles.publicLookup().findVirtual(
				atType,
				"adapt",
				MethodType.methodType(Object.class, adapterArgTypes)).bindTo(adapter);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		RegistrationKey key = new RegistrationKey(resultClass, argTypes, name);
		AdapterRegistration registration = new AdapterRegistration();
		registration.adapter = adapter;
		registration.handle = handle;
		registration.registrationKey = key;
		
		synchronized (this) {
			registrations.put(key, registration);
			clearCaches();
		}
	}

	private void doUnregister(Class<?> resultClass, ArgType argTypes,
			String name) {
		RegistrationKey key = new RegistrationKey(resultClass, argTypes, name);
		synchronized (this) {
			registrations.remove(key);
			clearCaches();
		}
	}

	private synchronized void clearCaches() {
		noMatchingAdapters.clear();
		cache.clear();
	}

	public static class Arguments {
		private Object[] args;
		private Arguments(Object... args) {
			this.args = args;
		}
	}
	
	public static Arguments arguments(Object... args) {
		return new Arguments(args);
	}
	
	public <T> T queryAdapter(Class<T> targetClass, Arguments args, String name, T defaultValue) {
		return doQueryAdapter(targetClass, args, name, defaultValue, false);
	}
	
	public <T> T queryAdapter(Class<T> targetClass, Arguments args, T defaultValue) {
		return doQueryAdapter(targetClass, args, "", defaultValue, false);
	}
	
	public <T> T getAdapter(Class<T> targetClass, Arguments args, String name) {
		return doQueryAdapter(targetClass, args, name, null, true);
	}
	
	public <T> T getAdapter(Class<T> targetClass, Arguments args) {
		return doQueryAdapter(targetClass, args, "", null, true);
	}
	
	private <T> T doQueryAdapter(Class<T> targetClass, Arguments args,
		String name, T defaultValue, boolean throwOnError)
	{
		AdapterRegistration registration = lookup(new RegistrationKey(targetClass, ArgType.of(args), name));
		if (registration == null) {
			if (throwOnError) {
				throw new ComponentLookupException(targetClass, args, name);
			}
			
			return defaultValue;
		}
		
		return targetClass.cast(registration.adapt(args.args));
	}
	
	private Map<RegistrationKey, AdapterRegistration> registrations = new HashMap<>();
	private Map<RegistrationKey, AdapterRegistration> cache = new HashMap<>();
	private Set<RegistrationKey> noMatchingAdapters = new HashSet<>();
	private class NoMatchingAdapterException extends RuntimeException { private static final long serialVersionUID = 1L; }
	
	private synchronized AdapterRegistration lookup(RegistrationKey lookupKey) {
		if (noMatchingAdapters.contains(lookupKey)) {
			return null; 
		}

		try {
			return cache.computeIfAbsent(lookupKey, (key) -> {	
				AdapterRegistration registration = registrations.get(key);
				if (registration != null) {
					cache.put(key, registration);
					return registration;
				}
				
				registration = findBestMatching(key);
				if (registration == null) {
					throw new NoMatchingAdapterException();
				}
				
				return registration;
			});
		} catch (NoMatchingAdapterException e) {
			noMatchingAdapters.add(lookupKey);
			return null;
		}
	}
	
	private AdapterRegistration findBestMatching(RegistrationKey key) {
		RegistrationKey winning = null;
		AdapterRegistration winningReg = null;
		for (Entry<RegistrationKey, AdapterRegistration> entry: registrations.entrySet()) {
			if (entry.getKey().providesFor(key)) {
				if (winning == null || key.isFirstBetterMatch(entry.getKey(), winning)) {
					winning = entry.getKey();
					winningReg = entry.getValue();
				}
			}
		}
		
		return winningReg;
	}
	static class RegistrationKey {
		public final Class<?> targetType;
		public final ArgType arguments;
		public final String name;
		public RegistrationKey(Class<?> targetType, ArgType arguments,
				String name) {
			super();
			this.targetType = targetType;
			this.arguments = arguments;
			if (name == null) {
				name = "";
			}
			this.name = name;
		}
		
		public boolean isFirstBetterMatch(
				RegistrationKey first,
				RegistrationKey second
		) {
			int[] firstDistance = first.arguments.getCastDistancesTo(arguments);
			int[] secondDistance = second.arguments.getCastDistancesTo(arguments);
			
			return compare(firstDistance, secondDistance) < 0;
		}
		public boolean providesFor(RegistrationKey key) {
			// wrong number of arguments
			if (key.argArity() != this.argArity()) {
				return false;
			}
			
			if (! this.name.equals(key.name)) {
				return false;
			}
			
			// return type cannot be assigned to requested
			if (! key.targetType.isAssignableFrom(targetType)) {
				return false;
			}

			return arguments.areAssignableFrom(key.arguments);
		}
		public int argArity() {
			return arguments.argTypes.length;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((arguments == null) ? 0 : arguments.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((targetType == null) ? 0 : targetType.hashCode());
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
			RegistrationKey other = (RegistrationKey) obj;
			if (arguments == null) {
				if (other.arguments != null)
					return false;
			} else if (!arguments.equals(other.arguments))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (targetType == null) {
				if (other.targetType != null)
					return false;
			} else if (!targetType.equals(other.targetType))
				return false;
			return true;
		}
	}
}