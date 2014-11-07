package com.anttipatterns.jtet.utils;

import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Test;

public class ClassUtilsTest extends TestCase {
	class C1 {}
	interface I1 {}
	interface I2 extends I1 {}
	interface I3 extends I2, I1 {}
	interface I4 extends I1, I3 {}
	interface I5 {}
	class C2 extends C1 implements I1 {}
	class C3 extends C2 implements I5 {}

	@Test
	public void testSimple() {
		assertEquals(Collections.emptyList(), ClassUtils.getAllSupers(Object.class));
		assertEquals(Arrays.asList(Object.class), ClassUtils.getAllSupers(C1.class));
	}

	@Test
	public void testInterfaceSimple() {
		assertEquals(Collections.emptyList(), ClassUtils.getAllSupers(I1.class));
		assertEquals(Arrays.asList(I1.class), ClassUtils.getAllSupers(I2.class));
	}
	
	@Test
	public void testInterfaceDeep() {
		assertEquals(Arrays.asList(I1.class, I3.class, I2.class), ClassUtils.getAllSupers(I4.class));
	}
	
	@Test
	public void testExtendsWithIface() {
		assertEquals(Arrays.asList(I1.class, C1.class, Object.class), ClassUtils.getAllSupers(C2.class));
	}

	@Test
	public void testExtendsWithMultiIface() {
		assertEquals(Arrays.asList(I5.class, C2.class, I1.class, C1.class, Object.class), ClassUtils.getAllSupers(C3.class));
	}
}
