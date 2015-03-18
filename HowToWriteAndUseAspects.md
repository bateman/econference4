**Index**


# What is AspectJ #

AspectJ is the Java implementation of the Aspect-oriented programming paradigm. Using aspects it is possible to define cross-cutting concerns useful in developing large-scale software.

## How to install AspectJ in Eclipse ##

First of all, it's necessary to download and install in eclipse the AJDT plugin. You can do this using the apposite function available in eclipse (Help -> Install New Software). Search for update site [here](http://www.eclipse.org/ajdt/downloads/) and take care to install all the plugins available.

# Aspects in eConference #

All the aspects relative to the entire project are sited in the it.uniba.di.cdg.aspects bundle. Then you have to edit this bundle if you want to expand the aspects possibilities of eConference .

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/aspectspackage.png' /> </p>
<p align='center'><b>Figure 1. Package of the plugin containing aspects</b></p>

## Creating a new Aspect ##

Go into it.di.uniba.cdg.aspects plugin source folder and create a new aspect. You'll have a new file with aj extension that will contain the aspect you want to develop.

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/createaspect.png' /> </p>
<p align='center'><b>Figure 2. Creating a new aspect</b></p>

For example, we want to create a tracing aspect for all the methods of the project. Then it's necessary to define a pointcut that, through a regular expression, involves the methods we want to track.

A pointcut is a program element that picks out join points and exposes data from the execution context of those join points. Pointcuts are used primarily by advice. They can be composed with boolean operators to build up other pointcuts. The complete list of pointcuts and combinators is available [here](http://www.eclipse.org/aspectj/doc/released/progguide/semantics-pointcuts.html)

For this example we want to track all the method executions, then we have to use the "execution" primitive pointcut: i'll call this pointcut "traceThis()".
```
public aspect Tracing {

	public pointcut traceThis() : execution( * *.*(..) ) && !within( it.uniba.di.cdg.aspects..*) ;
	
	before() : traceThis() {
		System.out.println("Entering :" + thisJoinPoint);
	}
}
```

As you can see, in the inner brackets of the execution primitive pointcut, we have a method pattern. The pattern defined in the example covers all the methods of all the classes of the entire project; for this reason, it is useful to exclude  the aspects methods (we don't want to trace them) then the execution primitive is combined with a "!within" for this task. The inner pattern of the "!within" primitive matches the aspects' package.

## Defining the behaviour ##

Once defined the pointcut, we have to define the behaviour of the aspect itself. In this case, using the "before()" keyword, we want the aspect to trigger before the execution of the methods defined in the "traceThis()" pointcut pattern expression, printing the method name the system is going to enter.

At the same time we may use other keywords as "after()" or "around()", depending on the use to do of our aspects.

## Markers ##

After defining the aspect and saving the file we were editing, the jdt weaving process takes place and all the methods involved by the aspects are marked with special marker assigned by AJDT. It is representend in different ways according to the type of keyword defined in the behaviour part of the aspect (before, after, around). Unfortunately, due to the bundle architecture of eConference, the markers are only visible on the methods afflicted, but not on the aspects (where is present a warning advice instead).

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/markers.png' /> </p>
<p align='center'><b>Figure 3. Markers on methods</b></p>

You can edit the marker symbol to identify immediately different kind of aspects, or you can use the cross references tab to backtrack the aspects reference tree.

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/crossreference.png' /> </p>
<p align='center'><b>Figure 4. Cross references.</b></p>

# Aspects and annotations #

In the previous example, we defined a very simple aspect using a poincut defined for all the methods of the entire project, but it is also possible to use the java @annotations to trigger the aspects only for the methods annotated in a certain way, rather than using regular expressions.

## Creating an annotation ##

First of all, we must create an annotation. Looking at the eConference project, it's possible to find this kind of annotations (created in the aspect package) :
```

@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface SwtAsyncExec {
    // Nothing
}
 
```
Once defined this annotation, we can go into the aspect definition, and use the annotation just created in the method pattern definition.
For example we want to track all the methods using this kind of annotation (SwtAsyncExec). All we have to do is this:

```
public aspect Tracing {

	public pointcut traceThis() : execution(@SwtAsyncExec * *.*(..) ) ;
	
	before() : traceThis() {
		System.out.println("Entering :" + thisJoinPoint);
	}
}

```

By using this aspect, the console log will show up the message defined in the behavior of the traceThis() pointcut only for those methods annotated with SwtAsyncExec.

## Annotations and aspects in eConference ##

All the aspects implemented in eConference use annotations to individuate the methods in the pointcut expressions. Those are used especially for applying multi-threading execution for graphics.

The SwtThreadSafety aspect deals with multi-threading in eConference:

```
public aspect SwtThreadSafety {
    /**
     * Captures synchronous methods: typically those that do not return anything.
     */
    public pointcut uiSyncCalls() : execution( @SwtSyncExec * *.*(..));

    /**
     * Captures asynchronous methods: methods that just update the UI and do not return 
     * any values are good candidates.
     */
    public pointcut uiAsyncCalls() : execution( @SwtAsyncExec * *.*(..) );

    Object around() : uiSyncCalls() {
    	RunnableWithReturn r = new RunnableWithReturn() {
            @Override
			public void run() {
                try {
                    _returnValue = proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException( e ); // Soft the exception ...
                }
            }
        };
        
        getDisplay().syncExec( r ); 
        return r.getReturnValue(); 
    }

    void around() : uiAsyncCalls() {
        Runnable r = new Runnable() {
            @Override
			public void run() {
                try {
                    proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                    throw new RuntimeException( e ); // Soft the exception ...
                }
            }
        };
        
        getDisplay().asyncExec( r ); 
    }

```

If you want to use multi-threading in a plugin extension of eConference you must consider the different nature of the pointcuts described above:

  * SwtAsyncExec: it suits for those methods that use SWT, but don't return anything
  * SwtSyncExec: is suits for those methods that use SWT returning objects

The application in eConference of this aspect is still experimental, because a little change of the annotations on the methods may break the normal execution of the graphic interface.

An example of methods using the aspect described above is this:

```

    @SwtAsyncExec
    public void appendMessage( String text ) {
        String textToAppend = text + "\n";
        messageBoardText.append( textToAppend );
        cacheMessage( getModel().getCurrentThread(), textToAppend );
        scrollToEnd();
    }

    /* (non-Javadoc)
     * @see it.uniba.di.cdg.xcore.ui.views.ITalkView#getChatLogText()
     */
    @SwtSyncExec
    public String getChatLogText() {
        return messageBoardText.getText();
    }

```

Those methods belong to the TalkView class of the it.uniba.di.cdg.xcore.ui.views package: the first has an asynchronous nature because it deals with the action of appending messages on the chat board, the latter has a synchronous nature because it deals with the capturing of the entire chat board, then it must be executed consequently with the thread running ui.

## Methods affected by annotations ##

Using Equinox Aspects and, conseguently, load-time weaving, it's impossibile to trace from the aspect's view the methods covered by the aspects defined in the it.uniba.di.cdg.aspects plugin. Those tables may help tracing them.

&lt;wiki:gadget url="http://econference4.googlecode.com/svn/wiki/XML/SwtThreadSafetyTable.xml" height="550" width="800" /&gt;

&lt;wiki:gadget url="http://econference4.googlecode.com/svn/wiki/XML/ThreadSafetyAspectTable.xml" height="1400" width="800" /&gt;

## Locking threads through aspects ##

In the previous content we defined the multi-threading solution adopted in eConference, but multi-threading carries synchronism problems.
The solution for this kind of problems is the definition of another aspect that locks and unlocks threads in the right moment.

In eConference this aspect is named "ThreadSafetyAspect" and uses two annotations to do its work:

  * GetSafety

  * SetSafety

```
public aspect ThreadSafetyAspect perthis(readOperations() || writeOperations()) {

	protected pointcut writeOperations() : execution( @SetSafety * *.*(..) );
	protected pointcut readOperations() : execution(@GetSafety * *.*(..));

	private ReadWriteLock _lock = new ReentrantReadWriteLock();

    before() : readOperations(){
        _lock.readLock().lock();
    }
    
    after() : readOperations() {
    	_lock.readLock().unlock();
    }
    
    before() : writeOperations() {
    	_lock.writeLock().lock();
    }
    
    after() : writeOperations() {
    	_lock.writeLock().unlock();
    }
}
```

As it's possible to see in the previous code, the aspect simply locks thread execution for reading for all the methods marked with the "GetSafety" annotation, and for writing for those marked with "SetSafety". The thread resources are released after the execution of the methods.

Usually those annotations are used when modifying and using resources (i.e. getters and setters). We can take a look at methods using them in the model packages of it.uniba.di.cdg.xcore.econference bundle.

## Logging exceptions through aspects ##

In eConference the aspects are used not only for synchronization and multithreading, but also for logging of the exception. Simply by using one aspect, system catches all the exceptions thrown during execution logging them to a file, sited in the "logs" folder of eConference, named with the package name plus "exceptionTrace.log" string. The aspect doing this work is this:

```

public aspect ExceptionLoggingAspect {

	private Logger logger;
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	private FileHandler handler;

	pointcut exceptionTraced()
		: execution(* *.*(..)) && !within( it.uniba.di.cdg.aspects..*);

	after() throwing(Throwable ex) : exceptionTraced() {
		this.logger = Logger.getLogger(thisJoinPoint.getThis().getClass()
				.getName());
		try {
			File f = new File("logs");
			if (!f.exists()) {
				f.mkdir();
			}
			handler = new FileHandler("logs/"
					+ thisJoinPoint.getThis().getClass().getPackage().getName()
					+ ".exceptionTrace.log", true);

			handler.setFormatter(new SimpleFormatter());
			this.logger.addHandler(handler);
		} catch (IOException e) {
			System.out.println("Cannot write log file");
		}

		if (lastLoggedException.get() != ex) {
			lastLoggedException.set(ex);
			Signature sig = thisJoinPointStaticPart.getSignature();
			logger.log(Level.INFO,
					"Exception trace aspect [" + sig.toShortString() + "]", ex);
		}

	}
}


```

As you can see, the pointcut defined in the aspect covers all the methods of the entire project(except aspects bundle), then if you are going to create a new class or method in the existent bundles, you don't need to link references with the "ExceptionLoggingAspect", it does automatically this work for you.

# Adding aspects to a new bundle #

What if you are adding a new bundle to eConference and you need to use the aspect bundle? There are some easy steps to do this.

First of all, convert your bundle to a Aspectj project by clicking the right-mouse button and selecting the "Configure"->"Convert to Aspectj project".

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/converttoaspectj.png' /> </p>
<p align='center'><b>Figure 5. Convert bundle to AspectJ project.</b></p>

Now it's time to add the aspect path to your project; you can do this simply clicking the right-mouse button on your bundle, selecting "AspectJ tools" -> "Configure AspectJ build-path..."

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/includeaspectpath.png' /> </p>
<p align='center'><b>Figure 6. Adding the aspect path to your bundle.</b></p>

Then, you have to add the aspect bundle project by clicking on the "Add Project..." button in the "Aspect path" tab.

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/includeaspectpath2.png' /> </p>
<p align='center'><b>Figure 7. Adding the aspect project bundle to your bundle aspect path.</b></p>

The last step to make aspects affecting your bundle is to augment your manifest adding the "it.uniba.di.cdg.aspects" bundle to the dependencies.

<p align='center'>
<img src='http://econference4.googlecode.com/svn/wiki/img/aspectj/augmentmanifest.png' /> </p>
<p align='center'><b>Figure 8. Augmenting the manifest adding the right dependency.</b></p>

If you followed all those steps, then your bundle will be able to use aspects implemented in the "it.uniba.di.cdg.aspects" plugin project.

# Build-Time versus Load-Time weaving #

The weaving is the process that links cross-cutting concerns defined in the aspects with the classes involved in the aspects definition.
Because of the particular bundle architecture of eConference, the build time weaving works only into the Eclipse enviroment, then it's necessary to define a aop.xml file to let eConference using aspects when exported.
This aop.xml file contains the definition of the aspects used in the project and sites in the META-INF directory of the it.uniba.di.cdg.aspects bundle.

When adding an aspect, then, we have to augment the aop.xml including the aspect definition we want to add.

For example, before adding the aspect for tracing, we had this aop.xml definition:

```
<aspectj>
     <aspects>
         <aspect name="it.uniba.di.cdg.aspects.AsynchronousExecution"/>
         <aspect name="it.uniba.di.cdg.aspects.SwtThreadSafety"/>
         <aspect name="it.uniba.di.cdg.aspects.ThreadSafetyAspect"/>
         <aspect name="it.uniba.di.cdg.aspects.AnnotatedAsynchronousExecution"/>
     </aspects>
</aspectj>
```

Once defined the aspect into the project, we have to add this line:
```
<aspect name="it.uniba.di.cdg.aspects.Tracing"/>
```
to the preexisting aop.xml :
```
<aspectj>
	<aspects>
		<aspect name="it.uniba.di.cdg.aspects.AsynchronousExecution" />
		<aspect name="it.uniba.di.cdg.aspects.SwtThreadSafety" />
		<aspect name="it.uniba.di.cdg.aspects.ThreadSafetyAspect" />
		<aspect name="it.uniba.di.cdg.aspects.AnnotatedAsynchronousExecution" />
		<aspect name="it.uniba.di.cdg.aspects.Tracing" />
	</aspects>
</aspectj>
```
Now we can export our product being sure that aspects are weaved in the project.