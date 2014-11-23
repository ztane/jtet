package com.anttipatterns.jtet.ca;

import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity0;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity1;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity2;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity3;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity4;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity5;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity6;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity7;
import com.anttipatterns.jtet.ca.ComponentRegistry.AdapterArity8;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity0;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity1;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity2;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity3;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity4;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity5;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity6;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity7;
import com.anttipatterns.jtet.ca.ComponentRegistry.ArgTypeArity8;

public interface IComponentRegistry extends IComponentQuery {

	public abstract <Result> void registerAdapter(
			Class<Result> resultClass,
			ArgTypeArity0 argTypes,
			AdapterArity0<Result> adapter,
			String name);

	public abstract <Result, A1> void registerAdapter(
			Class<Result> resultClass, ArgTypeArity1<A1> argTypes,
			AdapterArity1<Result, A1> adapter,
			String name);

	public abstract <Result, A1, A2> void registerAdapter(
			Class<Result> resultClass, ArgTypeArity2<A1, A2> argTypes,
			AdapterArity2<Result, A1, A2> adapter,
			String name);

	public abstract <Result, A1, A2, A3> void registerAdapter(
			Class<Result> resultClass, ArgTypeArity3<A1, A2, A3> argTypes,
			AdapterArity3<Result, A1, A2, A3> adapter,
			String name);

	public abstract <Result, A1, A2, A3, A4> void registerAdapter(
			Class<Result> resultClass, ArgTypeArity4<A1, A2, A3, A4> argTypes,
			AdapterArity4<Result, A1, A2, A3, A4> adapter,
			String name);

	public abstract <Result, A1, A2, A3, A4, A5> void registerAdapter(
			Class<Result> resultClass,
			ArgTypeArity5<A1, A2, A3, A4, A5> argTypes,
			AdapterArity5<Result, A1, A2, A3, A4, A5> adapter,
			String name);

	public abstract <Result, A1, A2, A3, A4, A5, A6> void registerAdapter(
			Class<Result> resultClass,
			ArgTypeArity6<A1, A2, A3, A4, A5, A6> argTypes,
			AdapterArity6<Result, A1, A2, A3, A4, A5, A6> adapter,
			String name);

	public abstract <Result, A1, A2, A3, A4, A5, A6, A7> void registerAdapter(
			Class<Result> resultClass,
			ArgTypeArity7<A1, A2, A3, A4, A5, A6, A7> argTypes,
			AdapterArity7<Result, A1, A2, A3, A4, A5, A6, A7> adapter,
			String name);

	public abstract <Result, A1, A2, A3, A4, A5, A6, A7, A8> void registerAdapter(
			Class<Result> resultClass,
			ArgTypeArity8<A1, A2, A3, A4, A5, A6, A7, A8> argTypes,
			AdapterArity8<Result, A1, A2, A3, A4, A5, A6, A7, A8> adapter,
			String name);
	
	public abstract <UtilityInterface> void registerUtility(
			Class<UtilityInterface> theInterface,
			UtilityInterface utility,
			String name
	);
}