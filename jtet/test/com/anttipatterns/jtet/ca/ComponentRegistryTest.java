package com.anttipatterns.jtet.ca;

import static com.anttipatterns.jtet.ca.ComponentRegistry.argTypes;
import static com.anttipatterns.jtet.ca.ComponentRegistry.arguments;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;

import org.junit.Before;
import org.junit.Test;

import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterRegistration;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgType;
import com.anttipatterns.jtet.ca.ComponentRegistry.RegistrationKey;

public class ComponentRegistryTest {	
	private ComponentRegistry componentRegistry;

	@Before
	public void setUp() {
		componentRegistry = new ComponentRegistry();
	}
	
	@Test
	public void simpleAreAssignableFrom() {
		ArgType to = argTypes(Object.class);
		ArgType from = argTypes(String.class);
		
		assertTrue(to.areAssignableFrom(from));
		assertFalse(from.areAssignableFrom(to));
	}
	
	@Test
	public void arityMismatch() {
		ArgType to = argTypes(Object.class, Object.class);
		ArgType from = argTypes(Object.class);

		assertFalse(to.areAssignableFrom(from));
		assertFalse(from.areAssignableFrom(to));
	}

	@Test
	public void multiArgAssignable() {
		ArgType to = argTypes(
			Object.class, Object.class, Object.class, Object.class,
			Object.class, Object.class, Object.class, Number.class
		);
		ArgType objectOnly = argTypes(
			Object.class, Object.class, Object.class, Object.class,
			Object.class, Object.class, Object.class, Object.class
		);
		ArgType exact = argTypes(
			Object.class, Object.class, Object.class, Object.class,
			Object.class, Object.class, Object.class, Number.class
		);
		ArgType wide = argTypes(
			String.class, String.class, String.class, String.class,
			Exception.class, Double.class, Double.class, Double.class
		);
		assertFalse(to.areAssignableFrom(objectOnly));
		assertTrue(to.areAssignableFrom(exact));
		assertTrue(to.areAssignableFrom(wide));
	}

	@Test
	public void interfaceAssignable() {
		ArgType iface = argTypes(Serializable.class);
		ArgType cls = argTypes(Exception.class);
		ArgType obj = argTypes(Object.class);
		assertTrue(iface.areAssignableFrom(cls));
		assertTrue(obj.areAssignableFrom(iface));
		assertFalse(iface.areAssignableFrom(obj));
	}

	class C1 {}
	class C2 extends C1 {}
	
	interface I1 {}
	interface I2 extends I1 {}
	interface I3 {} 
	class C3 extends C1 implements I2, I3 {}
	
	@Test
	public void classCastDistances() {
		assertEquals(0, ComponentRegistry.getCastDistance(Object.class, Object.class));
		assertEquals(1, ComponentRegistry.getCastDistance(C2.class, C1.class));
		assertEquals(-1, ComponentRegistry.getCastDistance(C1.class, C2.class));

		assertEquals(4, ComponentRegistry.getCastDistance(C3.class, C1.class));
		assertEquals(-4, ComponentRegistry.getCastDistance(C1.class, C3.class));
	}
	
	@Test
	public void cachedClassCastDistances() {
		SimpleEntry<Class<?>, Class<?>> entry = new AbstractMap.SimpleEntry<>(C2.class, C1.class);
		ComponentRegistry.distances.remove(entry);
		assertFalse(ComponentRegistry.distances.containsKey(entry));
		
		assertEquals(1, ComponentRegistry.getCastDistance(C2.class, C1.class));
		assertTrue(ComponentRegistry.distances.containsKey(entry));

		assertEquals(1, ComponentRegistry.getCastDistance(C2.class, C1.class));
		ComponentRegistry.distances.put(entry, 42);
		assertEquals(42, ComponentRegistry.getCastDistance(C2.class, C1.class));
		
		ComponentRegistry.distances.remove(entry);
		assertFalse(ComponentRegistry.distances.containsKey(entry));
	}
	
	@Test
	public void testIntCompare() {
		assertEquals(0, ComponentRegistry.compare(
			new int[] {1, 2, 3},
			new int[] {1, 2, 3}
		));
		assertEquals(1, ComponentRegistry.compare(
			new int[] {4, 2, 3},
			new int[] {1, 2, 3}
		));
		assertEquals(-1, ComponentRegistry.compare(
			new int[] {1, 2, 3},
			new int[] {1, 2, 4}
		));
		assertEquals(1, ComponentRegistry.compare(
			new int[] {1, 2, 4},
			new int[] {1, 2, 1}
		));		
		assertEquals(-1, ComponentRegistry.compare(
			new int[] {1, 2, 4},
			new int[] {1, 2, 4, 5}
		));
		assertEquals(1, ComponentRegistry.compare(
			new int[] {1, 2, 4, -1},
			new int[] {1, 2, 4}
		));
	}
	
	@Test
	public void testKeys() {
		RegistrationKey<AdapterRegistration> unnamedIntStringToString = makeAdapterRegistrationKey(
			String.class, argTypes(CharSequence.class, Number.class), null
		);
		
		assertTrue(unnamedIntStringToString.providesFor(
			makeAdapterRegistrationKey(String.class, 
								argTypes(CharSequence.class, Number.class),
								null
			)
		));
		assertTrue(unnamedIntStringToString.providesFor(
				makeAdapterRegistrationKey(String.class, 
									argTypes(CharSequence.class, Number.class),
									""
				)
		));

		assertFalse(unnamedIntStringToString.providesFor(
				makeAdapterRegistrationKey(String.class, 
									argTypes(CharSequence.class, Number.class),
									"otherName"
				)
		));

		// return type widening
		assertTrue(unnamedIntStringToString.providesFor(
				makeAdapterRegistrationKey(Object.class, 
									argTypes(CharSequence.class, Number.class),
									""
				)
		));

		// to interface
		assertTrue(unnamedIntStringToString.providesFor(
				makeAdapterRegistrationKey(Serializable.class, 
									argTypes(CharSequence.class, Number.class),
									""
				)
		));
		
		// argument downcasting
		assertTrue(unnamedIntStringToString.providesFor(
				makeAdapterRegistrationKey(Serializable.class, 
									argTypes(String.class, Double.class),
									""
				)
		));

		// arity mismatch
		assertFalse(unnamedIntStringToString.providesFor(
				makeAdapterRegistrationKey(Serializable.class, 
									argTypes(String.class, Double.class, Object.class),
									""
				)
		));
	}
	
	@Test
	public void unmatchingAdapters() {
		RegistrationKey<AdapterRegistration> key = makeAdapterRegistrationKey(
			Number.class, argTypes(Integer.class, String.class), "foobar");

		assertFalse(key.providesFor(makeAdapterRegistrationKey(
			Number.class, argTypes(Integer.class, String.class), null)));

		assertFalse(key.providesFor(makeAdapterRegistrationKey(
			Number.class, argTypes(Integer.class, String.class), "barfoo")));

		assertTrue(key.providesFor(makeAdapterRegistrationKey(
			Number.class, argTypes(Integer.class, String.class), "foobar")));

		// cannot downcast number to double!
		assertFalse(key.providesFor(makeAdapterRegistrationKey(
			Double.class, argTypes(Integer.class, String.class), "foobar")));

		assertFalse(key.providesFor(makeAdapterRegistrationKey(
			Number.class, argTypes(Number.class, String.class), "foobar")));
		
		assertFalse(key.providesFor(makeAdapterRegistrationKey(
			Number.class, argTypes(Integer.class, CharSequence.class), "foobar")));
	}
	
	private  RegistrationKey<AdapterRegistration> makeAdapterRegistrationKey(Class<?> target,
			ArgType argTypes, String name) {
		return new RegistrationKey<>(AdapterRegistration.class, target, argTypes, name);
	}

	public String adapt(int i, String s) {
		return s + ":" + i;
	}
	
	@Test
	public void testSimpleLookup() {
		componentRegistry.registerAdapter(
			String.class,
			argTypes(Integer.class, String.class), 
			(x, y) -> y + ":" + x,
			null
		);
		
		String result = componentRegistry.queryAdapter(
			String.class,
			arguments(42, "foobar"),
			null
		);
		assertEquals("foobar:42", result);
	}
}
