package com.anttipatterns.jtet.ca;

import junit.framework.TestCase;

import org.junit.Test;

public class ComponentRegistryTest extends TestCase {
	private ComponentRegistry registry;
	private C1 c1;
	private C2 c2;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		c1 = new C1();
		c2 = new C2();

		registry = new ComponentRegistry();
	}
	
	@Test
	public void testAdapterKeys() {
		assertEquals(
			ComponentRegistry.key(String.class, "foo"),
			ComponentRegistry.key(String.class, "fo" + "o")
		);

		assertTrue(
			!ComponentRegistry.key(String.class, "foo").equals(
					ComponentRegistry.key(String.class, "bar"))
		);

		assertTrue(
			!ComponentRegistry.key(Integer.class, "foo").equals(
					ComponentRegistry.key(String.class, "foo"))
		);

		assertEquals(
				ComponentRegistry.key(String.class, "foo").hashCode(),
				ComponentRegistry.key(String.class, "fo" + "o").hashCode()
			);

			assertTrue(
				ComponentRegistry.key(String.class, "foo").hashCode() !=
						ComponentRegistry.key(String.class, "bar").hashCode()
			);

			assertTrue(
				ComponentRegistry.key(Integer.class, "foo").hashCode() !=
						ComponentRegistry.key(String.class, "foo").hashCode()
			);

	}
	
	@Test
	public void testSimpleAdapt() {
		registry.registerAdapter(x -> "converted: " + x.toString(), Integer.class, String.class);
		assertEquals("converted: 1", registry.queryAdapter(String.class, 1));
	}

	@Test
	public void testMultipleAdapters() {
		registry.registerAdapter(x -> "from int: " + x.toString(),  Integer.class, String.class);
		registry.registerAdapter(x -> "from long: " + x.toString(), Long.class, String.class);
		registry.registerAdapter(x -> (long)(42 + x),               Integer.class, Long.class);

		assertEquals("from int: 42", registry.queryAdapter(String.class, 42));
		assertEquals("from long: 42", registry.queryAdapter(String.class, 42l));
		assertEquals(new Long(84l), registry.queryAdapter(Long.class, 42));
		assertNull(registry.queryAdapter(Integer.class, 42l));		
	}

	@Test
	public void testDerivedSource() {
		registry.registerAdapter(x -> "from number: " + x.toString(), Number.class, String.class);
		assertEquals("from number: 1", registry.queryAdapter(String.class, 1));
		assertEquals("from number: 42", registry.queryAdapter(String.class, 42l));
		assertEquals("from number: 1.5", registry.queryAdapter(String.class, 1.5));

		assertNull(registry.queryAdapter(Integer.class, 42));
		assertNull(registry.queryAdapter(String.class, ""));
	}
	
	interface I1 {
		
	}

	interface I2 {
		
	}

	interface I3 {
		
	}

	class C1 implements I1, I2 {
		
	}

	class C2 extends C1 implements I3 {
		
	}
	
	@Test
	public void testDerivedOrder() {		
		registry.registerAdapter(
			x -> "from I3: " + x.getClass().getSimpleName(),
			I3.class, String.class);
		
		registry.registerAdapter(
			x -> "from I2: " + x.getClass().getSimpleName(),
			I2.class, String.class);		
		
		registry.registerAdapter(
			x -> "from I1: " + x.getClass().getSimpleName(),
			I1.class, String.class);		
		
		assertEquals("from I1: C1", registry.queryAdapter(String.class, c1));
		assertEquals("from I3: C2", registry.queryAdapter(String.class, c2));
	}

	@Test
	public void testDeepDerivation() {
		registry.registerAdapter(
			x -> "from I1: " + x.getClass().getSimpleName(),
			I1.class,
			String.class
		);
		
		assertEquals("from I1: C2", registry.queryAdapter(String.class, c2));
	}
	
	@Test
	public void testComponentLookupException() {
		try {
			registry.getAdapter(String.class, 1);
			assertTrue("ComponentLookupException should have been thrown", false);
		} catch (ComponentLookupException e) {
			
		}
	}
}
