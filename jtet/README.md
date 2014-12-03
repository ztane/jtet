# JTet - an intelligent pyramid-like web framework for Java

So far most of the code is not much more than proof of concept

## Request and Response

The Request mostly corresponds to the ServletRequest. The
Response instead is an object that holds the response contents
and its headers; it is supposed to contain the data that that will
be written to the ServletResponse.

## Views

Views are methods that accept a Request and return a value that
will be rendered into a Response. The views are usually registered
by the `ViewConfig` annotation.

## URL dispatch

JTet supports URL dispatch pattern familirized by many Python frameworks,
such as Pylons, Pyramid and Django. In URL dispatch, the request URL
is matched against precompiled route patterns in order. The first route 
matching the request URL will be used. The first view attached to the 
given route is then executed, and its return value returned to the client
as the response.

### TestApplication.java

	package com.anttipatterns.jtet.test;
	
	import com.anttipatterns.jtet.Application;
	import com.anttipatterns.jtet.config.Configurator;
	
	public class TestApplication {
		public static void main(String[] args) {
			Configurator config = new Configurator();
			config.addRoute("index").pattern("/");
			config.addRoute("another").pattern("/another/{tomatch}/");

			config.scan("com.anttipatterns.jtet.test.views");
			
			Application application = config.createApplication();
			application.serve(54321);
		}
	}

### ViewTest.java

	package com.anttipatterns.jtet.test.views;

	import com.anttipatterns.jtet.request.Request;
	import com.anttipatterns.jtet.view.ViewConfig;
	
	public class ViewTest {
		@ViewConfig(routeName="index")
		public static String index(Request request) {
			return "Hello world. <a href='/another/one/'>Go here</a>";
		}

		@ViewConfig(routeName="another")
		public static String another(Request request) {
			return "Another one";
		}
	}

The `another` view method will be invoked for any url matching the regular expression `^/another/[^/]+/`.

## Tweens

Tweens are a piece of code akin to Servlet filters that can filter the incoming request and/or outgoing response, or act upon extensions. A tween implements the 
[ITween](jtet/src/com/anttipatterns/jtet/tween/ITween.java) functional interface, whose `wrap` method will be invoked by the `Configurator` when the 
application is created.

Tweens are registered with the `addTween` method of the `Configurator`; when the application is created, all tweens, from innermost to outermost, are wrapped around
the inner `IHandler`s automatically.

## Request and Registry properties

One can easily add custom data items to any `Request` or `Registry`. The `IRequest` interface and the `Registry` class implement the `IPropSupport` interface.
`IPropSupport` makes it possible to have arbitrary keyed typesafe properties on the containing classes. For example, the `TransactionTween` registers the
active `Session` into the active `IRequest` under the key `Persistence.SESSION`. The property key is declared as 

        public static final Key<IRequest, ISession> SESSION = key(ISession.class, "session");

And it allows one to store only `? extends ISession` into `? extends PropSupport<? extends IRequest>` for maximum type safety.

To get the session one can use

        ISession session = request.queryProp(Persistence.SESSION);

which will return `null` if the key was not set, or 

        ISession session = request.getProp(Persistence.SESSION);

which will throw the checked `NoSuchPropertyException` on error.

## Persistence

JTet supports JPA persistence with JINQ out of box. Currently you need to provide the `persistence.xml`, but later it should be possible to configure this procedurally too. You 
configure the persistence with `config.configurePersistence(persistenceUnitName)` Additionally you need to register the `TransactionTween` tween to commit/rollback transactions 
automatically:

        config.configurePersistence("com.anttipatterns.jtet.example");
        config.addTween(new TransactionTween());

For example, suppose you have the JPA persistent entity containing a fictional `Entry`

        @Entity
        @Table(name = "entry")
        public class Entry {
        	private long id;
        	private String name;
        
        	public Entry() { }
        	
        	@Id
        	@GeneratedValue
        	@Column(name="id")
        	public long getId() {
        		return this.id;
        	}
        
        	public void setId(long id) {
        		this.id = id;
        	}
        
        	@Column(name="name", nullable=false)
        	public String getName() {
        		return this.name;
        	}
        
        	public void setName(String name) {
        		this.name = name;
        	}
        }

Then in the view after you acquire the `ISession`, you can easily query the Entries using JINQ:

        ISession session = request.queryProp(Persistence.SESSION);

        List<String> entryNames = 
            session.query(Entry.class)
                   .where(e -> e.getName().startsWith("foo"))
                   .sortedBy(e -> e.getCourseId())
                   .select(e -> e.getCourseName())
                   .toList();

You can persist new instances by calling `session.add(...)`:

        session.add(new Entry("My newly added entry"));


## Component Architecture and adapters

JTet will heavilily utilize a software pattern called dynamic adapters. Dynamic adapters are essentially a case of 
multiple dispatch where a most suitable adapter function is selected based on the types of the given arguments
and the requested return type.

For example, the JTet machinery can request for an adapter that adapts a `Request(), 42` tuple into an `IResponse`,
and the best-matching adapter will be invoked dynamically based on the runtime types of the arguments. There might
be a catch-all `IRequest, Object -> IResponse` and `IRequest, Number -> IResponse`; with the latter preferred for 
the argument `42` as it has lower casting distance to `Number` than to `Object`.

At its simplest, the ComponentRegistry can be used as follows:

	componentRegistry = new ComponentRegistry();

	componentRegistry.registerAdapter(
		String.class,                                             // the adapter return type
		argTypes(Integer.class, String.class),                    // 2-arity argument types
		(x, y) -> "The string is " + y + ", the integer is " + x, // the adapter
		null                                                      // optional name
	);
 
Then the registry can be queried for adaptation of arguments:

	String result = componentRegistry.queryAdapter(
		String.class,                                             // request a String as return value
		arguments(42, "foobar"),                                  // the given arguments
		null                                                      // default value returned if no adapter is found
	);
        // returns "The string is foobar, the integer is 42"


The `ComponentRegistry` has some limitations with regards to number of arguments accepted for multidispatch, 
as Java does not support variable argument generics. Thus the adapters can only have arity from 0 to 8 - this ought
to be enough for almost everyone as in the Zope world where the adapter pattern is adopted, 5 seems to be the 
maximum widely used number.

