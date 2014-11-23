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
public class ComponentRegistry implements IComponentRegistry {
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
		public RegistrationKey<AdapterRegistration> registrationKey;
		public Object adapt(Object... arguments) {
			try {
				return handle.invokeWithArguments(arguments);
			} catch (Throwable e) {
				throw new RuntimeException("Unable to adapt, exception was thrown", e);
			}
		}
	};

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity0, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity0)
	 */
	@Override
	public <Result> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity0 argTypes,
		AdapterArity0<Result> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 0);
	}
	
	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity1, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity1)
	 */
	@Override
	public <Result, A1> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity1<A1> argTypes,
		AdapterArity1<Result, A1> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity2, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity2)
	 */
	@Override
	public <Result, A1, A2> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity2<A1, A2> argTypes,
		AdapterArity2<Result, A1, A2> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 2);
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity3, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity3)
	 */
	@Override
	public <Result, A1, A2, A3> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity3<A1, A2, A3> argTypes,
			AdapterArity3<Result, A1, A2, A3> adapter,
			String name
		) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 3);
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity4, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity4)
	 */
	@Override
	public <Result, A1, A2, A3, A4> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity4<A1, A2, A3, A4> argTypes,
		AdapterArity4<Result, A1, A2, A3, A4> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 4);
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity5, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity5)
	 */
	@Override
	public <Result, A1, A2, A3, A4, A5> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity5<A1, A2, A3, A4, A5> argTypes,
		AdapterArity5<Result, A1, A2, A3, A4, A5> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 5);
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity6, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity6)
	 */
	@Override
	public <Result, A1, A2, A3, A4, A5, A6> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity6<A1, A2, A3, A4, A5, A6> argTypes,
		AdapterArity6<Result, A1, A2, A3, A4, A5, A6> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 6);
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity7, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity7)
	 */
	@Override
	public <Result, A1, A2, A3, A4, A5, A6, A7> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity7<A1, A2, A3, A4, A5, A6, A7> argTypes,
		AdapterArity7<Result, A1, A2, A3, A4, A5, A6, A7> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 7);
	}

	/* (non-Javadoc)
	 * @see com.anttipatterns.jtet.ca.IComponentRegistry#registerAdapter(java.lang.Class, com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity8, com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity8)
	 */
	@Override
	public <Result, A1, A2, A3, A4, A5, A6, A7, A8> void registerAdapter(
		Class<Result> resultClass,
		ArgTypeArity8<A1, A2, A3, A4, A5, A6, A7, A8> argTypes,
		AdapterArity8<Result, A1, A2, A3, A4, A5, A6, A7, A8> adapter,
		String name
	) {
		doRegisterAdapter(resultClass, argTypes, adapter, name, 8);
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
		
		RegistrationKey<AdapterRegistration> key = new RegistrationKey<>(AdapterRegistration.class, resultClass, argTypes, name);
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
		RegistrationKey<AdapterRegistration> key = new RegistrationKey<>(AdapterRegistration.class, resultClass, argTypes, name);
		synchronized (this) {
			registrations.remove(key);
			clearCaches();
		}
	}

	private synchronized void clearCaches() {
		noMatchingComponents.clear();
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
	
	@Override
	public <T> T queryAdapter(Class<T> targetClass, Arguments args, String name, T defaultValue) {
		return doQueryAdapter(targetClass, args, name, defaultValue, false);
	}
	
	@Override
	public <T> T queryAdapter(Class<T> targetClass, Arguments args, T defaultValue) {
		return doQueryAdapter(targetClass, args, "", defaultValue, false);
	}
	
	@Override
	public <T> T getAdapter(Class<T> targetClass, Arguments args, String name) {
		return doQueryAdapter(targetClass, args, name, null, true);
	}
	
	@Override
	public <T> T getAdapter(Class<T> targetClass, Arguments args) {
		return doQueryAdapter(targetClass, args, "", null, true);
	}
	
	private <T> T doQueryAdapter(Class<T> targetClass, Arguments args,
		String name, T defaultValue, boolean throwOnError)
	{
		AdapterRegistration registration = lookup(new RegistrationKey<>(AdapterRegistration.class, targetClass, ArgType.of(args), name));
		if (registration == null) {
			if (throwOnError) {
				throw new ComponentLookupException(targetClass, args, name);
			}
			
			return defaultValue;
		}
		
		return targetClass.cast(registration.adapt(args.args));
	}
	
	private Map<RegistrationKey<?>, Object> registrations = new HashMap<>();
	private Map<RegistrationKey<?>, Object> cache = new HashMap<>();
	private Set<RegistrationKey<?>> noMatchingComponents = new HashSet<>();
	private class NoMatchingAdapterException extends RuntimeException { private static final long serialVersionUID = 1L; }
	
	@SuppressWarnings("unchecked")
	private synchronized <T> T lookup(RegistrationKey<T> lookupKey) {
		if (noMatchingComponents.contains(lookupKey)) {
			return null; 
		}

		try {
			return (T)cache.computeIfAbsent(lookupKey, (key) -> {	
				T registration = (T)registrations.get(key);
				if (registration != null) {
					cache.put(key, registration);
					return registration;
				}
				
				registration = (T)findBestMatching(key);
				if (registration == null) {
					throw new NoMatchingAdapterException();
				}
				
				return registration;
			});
		} catch (NoMatchingAdapterException e) {
			noMatchingComponents.add(lookupKey);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T findBestMatching(RegistrationKey<T> key) {
		RegistrationKey<?> winning = null;
		T winningReg = null;
		for (Entry<RegistrationKey<?>, Object> entry: registrations.entrySet()) {
			if (entry.getKey().providesFor(key)) {
				if (winning == null || key.isFirstBetterMatch(entry.getKey(), winning)) {
					winning = entry.getKey();
					winningReg = (T)entry.getValue();
				}
			}
		}
		
		return winningReg;
	}
	static class RegistrationKey<T> {
		public final Class<T> componentType;
		public final Class<?> targetType;
		public final ArgType argType;
		public final String name;
		public RegistrationKey(Class<T> componentType, Class<?> targetType, ArgType arguments,
				String name) {
			super();
			this.componentType = componentType;
			this.targetType = targetType;
			if (arguments == null) {
				arguments = new ArgType();
			}
			this.argType = arguments;
			if (name == null) {
				name = "";
			}
			this.name = name;
		}
		
		public boolean isFirstBetterMatch(
				RegistrationKey<?> first,
				RegistrationKey<?> second
		) {
			int[] firstDistance = first.argType.getCastDistancesTo(argType);
			int[] secondDistance = second.argType.getCastDistancesTo(argType);
			
			return compare(firstDistance, secondDistance) < 0;
		}
		
		public boolean providesFor(RegistrationKey<?> key) {
			if (key.componentType != componentType) {
				return false;
			}
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

			return argType.areAssignableFrom(key.argType);
		}
		public int argArity() {
			return argType.argTypes.length;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result += componentType.hashCode();
			result = prime * result
					+ ((argType == null) ? 0 : argType.hashCode());
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
			RegistrationKey<?> other = (RegistrationKey<?>) obj;
			if (other.componentType != this.componentType) {
				return false;
			}
			if (argType == null) {
				if (other.argType != null)
					return false;
			} else if (!argType.equals(other.argType))
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

	static class UtilityRegistration {
		RegistrationKey<UtilityRegistration> key;
		Object value;
	}
	
	@Override
	public <T> T getUtility(Class<T> utility) {
		return doQueryUtility(utility, "", null, true);
	}

	@Override
	public <T> T getUtility(Class<T> utility, String name) {
		return doQueryUtility(utility, name, null, true);
	}

	@Override
	public <T> T queryUtility(Class<T> utility, T defaultValue) {
		return doQueryUtility(utility, "", defaultValue, false);
	}

	@Override
	public <T> T queryUtility(Class<T> utility, String name, T defaultValue) {
		return doQueryUtility(utility, name, defaultValue, false);
	}

	public <T> T doQueryUtility(Class<T> utility, String name, T defaultValue, boolean doThrow) {
		UtilityRegistration registration = lookup(new RegistrationKey<UtilityRegistration>(UtilityRegistration.class, utility, argTypes(), ""));
		if (registration == null && doThrow) {
			throw new ComponentLookupException(utility, argTypes(), name);
		}
		
		if (registration == null) {
			return defaultValue;
		}
		
		return utility.cast(registration.value);
	}
	
	@Override
	public <UtilityInterface> void registerUtility(
			Class<UtilityInterface> theInterface, UtilityInterface utility,
			String name) {
		if (utility == null) {
			doUnregister(theInterface, argTypes(), name);
			return;
		}

		if (name == null) {
			name = "";
		}
		
		RegistrationKey<UtilityRegistration> key 
			= new RegistrationKey<>(UtilityRegistration.class, theInterface, null, name);
		UtilityRegistration registration = new UtilityRegistration();
		registration.value = utility;
		registration.key = key;
		
		synchronized (this) {
			registrations.put(key, registration);
			clearCaches();
		}
	}
}
