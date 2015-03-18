**Index**


# How to write a test using JUnit #
JUnit framework is an open source project developed by Kent Beck; you can download the last version at http://github.com/KentBeck/junit/downloads. As of this writing (Jan. 2011) the last stable version released is 4.8.2.


## Why use JUnit? ##
When a developer need to test functioning of a class, he must write another class that, by convention, it's called with the same name of the class followed by suffix “Test”.
Suppose that we have a class named `MathOp` that carry out operation of addition and subtraction; suppose that we want write a unit test for this class.

```
public class MathOp {
    public MathOp() {
    }
 
    public int add(int a, int b) {
        return a + b;
    }
 
    public int sub(int a, int b) {
        return a – b;
    }
}
```
First, we must create another class that we call, by convention, `MathOpTest`.
To use JUnit library is necessary to add it to the build path of the project. (You can do it adding library from the preference of the Eclipse project).


## Import JUnit class ##
To use the constructs of JUnit to do assertion on method's result, it's necessary to import, first of all, the library and import statically the class of package org.junit.Assert:
```
import org.junit.*;
import static org.junit.Assert.*;
```

## Test class ##
Now we are ready to write the test class. To indicate to JUnit which are methods that make tests it’s necessary to mark them with annotation @Test.
In this way, JUnit will recognize methods that contain assertion.
```
	import org.junit.*;
	import static org.junit.Assert.*;
	 
	public class MathOpTest {
	    @Test
	    public void testAdd(){
	        MathOp a = new MathOp();
	        assertEquals(4, a.add(3, 1));	    }
	 
	    @Test
	    public void testSub(){
	        MathOp a = new MathOp();
	        assertEquals(-4, a.sub(-3, 1));
	    }
	}
```

## The assertions ##
With JUnit it’s possible to do many type of test with different assertion (http://junit.sourceforge.net/javadoc_40/org/junit/Assert.html). At the execution moment, a test is passed if all conditions specified are true.

```
assertEquals(expected, actual), assertArrayEquals(expected, actual) : requires that the value ‘expected’ is equal to ‘actual’.
assertTrue(cond): required that ‘cond’ has value ‘true’
assertFalse(cond): required that ‘cond’ has value ‘false’
assertNull(obj): required that ‘obj’ is a null reference
assertNotNull(obj): required that ‘obj’ isn't null reference
```

## The @Before and @After annotations ##
In the previous example, in all test case is instanced an object `MathOp`. JUnit provides developers a feature to execute instructions before they are carried out tests. setUp() method have the annotation @Before that is used to indicate to JUnit that the method is to run before the execution of other test case. In this way, the code to initialize objects and resources (for example a connection to a DBMS) is written in one point of test case.
Similary to @Before, @After indicates to JUnit to execute the method after the execution of all test cases.
```
	import org.junit.*;
	import static org.junit.Assert.*;
	 
	public class MathOpTest {
	    private MathOp a;
	 
	    @Before
	    public void setUp(){
	        a = new MathOp();
	    }
	 
	    @Test
	    public void testAdd(){
	        assertEquals(4, a.add(3, 1));
	    }
	 
	    @Test
	    public void testSub(){
	        assertEquals(-4, a.sub(-3, 1));
	    }
	}
```

## Launch the tests ##
The test can be launched from Eclipse selecting the test class and clicking on“Run as” -> “JUnit Test”.
It's possible to use the command line o the junit task and junit report of Apache Ant.

Figure 1 show a screenshot of the test results in Eclipse:

<p align='center'><img src='http://econference4.googlecode.com/svn/wiki/img/junit/JunitResultTests.png' /> <br>
<p align='center'><b>Figure 1. Result of JUnit execution</b></p>

The two tests have positive end.<br>
If there are errors, this are showed in the section named Errors, while the condition not verified will be counted in the section named Failured.<br>
<br>
<br>
<h2>Setting time limit</h2>
The performance verification is one of the most difficult issues in JUnit. JUnit 4 not completely solves the problem but offers an utile help: in fact, the test method can be signed with a timeout parameter. If the tests are running in a time over, it fails. For example, in the next code the test fails if it needs more than 500 ms to be ended:<br>
<pre><code>	@Test(timeout=500) public void retrieveAllElementsInDocument() {<br>
	    doc.query("//*");<br>
	}<br>
</code></pre>
Apart from simple benchmarking, timed tests are also useful for the testing of network operations. If you are trying to connect to a slow or unreachable database, you can bypass the test so as not to slow down the following tests:<br>
<pre><code>	@Test(timeout=2000)<br>
	  public void remoteBaseRelativeResolutionWithDirectory()<br>
	   throws IOException, ParsingException {<br>
	      builder.build("http://www.qualcosa.org/xml");<br>
	}<br>
</code></pre>

<h2>Ignored Tests</h2>
@Ignore annotation allows marking the test methods that you want skip.<br>
Suppose that we have a test method that need more time to be executed: not necessary we must to speed up it, simply its work could be more complex. For example, same test that trying to connect to remote server are in this case test category. To not slowing other methods, it's possible to annotate this test to ignore it.<br>
<pre><code>	@Ignore public void testUTF32BE()<br>
	      throws ParsingException, IOException, XIncludeException {<br>
	 <br>
	        File input = new File(<br>
	          "data/xinclude/input/UTF32BE.xml"<br>
	        );<br>
	        Document doc = builder.build(input);<br>
	        Document result = XIncluder.resolve(doc);<br>
	        Document expectedResult = builder.build(<br>
	          new File(outputDir, "UTF32BE.xml")<br>
	        );<br>
	        assertEquals(expectedResult, result);<br>
	}<br>
</code></pre>
During the execution of all test cases, this method will be ignored.<br>
<br>
<h2>Example of a JUnit test for a procedure</h2>
When we need to test procedure, the way you create a test case is always the same.<br>
For example, consider a class that has a method that connects to a server:<br>
<pre><code>public class Client{<br>
	<br>
        public void connect(String host, integer port){<br>
	….<br>
        }<br>
        <br>
        private boolean isConnected(){<br>
	..............<br>
        }<br>
<br>
} <br>
<br>
</code></pre>

If we want to test the connection, test class will look like this:<br>
<pre><code><br>
public class ConnectionTest{<br>
    @Test<br>
    public void testConnection(){<br>
		Client client = new Client();<br>
                client.connect(“host”,5566);<br>
                assertEquals(client.isConnected(), true);<br>
	}<br>
}<br>
<br>
</code></pre>

<h1>Sintax for test cases to use in eConference4</h1>
In Econference4 project, to write a test case you must respect the classic syntax that we have reported in previous paragraph.<br>
In the actual version of eConference4 you can find different syntax in more test cases because the version of JUnit used until recently is the JUnit3. But eConference4 now implement JUnit4 that support old (JUnit3) and new syntax. Whereas the syntax of JUnit4 is more simply than syntax of JUnit 3, you have to use only JUnit4 syntax.<br>
<br>
<br>
<h2>Test's methods</h2>
All previous versions of JUnit used coding conventions and reflection to mark a method how tests. For example, the following test verifies that 1 + 1 equals 2:<br>
<pre><code>	import junit.framework.TestCase;<br>
	 <br>
	public class AdditionTest extends TestCase {<br>
	 <br>
	  private int x = 1;<br>
	  private int y = 1;<br>
	 <br>
	  public void testAddition() {<br>
	    int z = x + y;<br>
	    assertEquals(2, z);<br>
	  }<br>
	}<br>
</code></pre>
Conversely, in JUnit4 the tests are marked by @Test annotation, as follows:<br>
<pre><code>	import org.junit.Test;<br>
	import junit.framework.TestCase;<br>
	 <br>
	public class AdditionTest extends TestCase {<br>
	 <br>
	  private int x = 1;<br>
	  private int y = 1;<br>
	 <br>
	  @Test public void testAddition() {<br>
	    int z = x + y;<br>
	    assertEquals(2, z);<br>
	  }<br>
	}<br>
</code></pre>
In general, in this way you can use your preferred names that are properly related to your application and your programming style.<br>
The <code>TestCase</code> class is present but is no longer necessary to extend them. Simply you have to mark the test methods using @Test annotation and you can insert them in any class.<br>
To use assert method is necessary to import the class junit.Assert, as follow:<br>
<pre><code>	import org.junit.Assert;<br>
	 <br>
	public class AdditionTest {<br>
	 <br>
	  private int x = 1;<br>
	  private int y = 1;<br>
	 <br>
	  @Test public void addition() {<br>
	    int z = x + y;<br>
	    Assert.assertEquals(2, z);<br>
	  }<br>
	}<br>
</code></pre>
You can also use the new static import feature of JDK 5 to make this easier than the old version of JUnit:<br>
<pre><code>	import static org.junit.Assert.assertEquals;<br>
	 <br>
	public class AdditionTest {<br>
	 <br>
	  private int x = 1;<br>
	  private int y = 1;<br>
	 <br>
	  @Test public void addition() {<br>
	    int z = x + y;<br>
	    assertEquals(2, z);<br>
	  }<br>
	}<br>
</code></pre>
This approach makes the testing of protected methods much easier, because the test class can now extend the class containing the protected methods.<br>
<br>
<br>
<h2>The setup and teardown methods</h2>
The run tools of JUnit 3 tests automatically run the method setUp() before each test method. Normally, we use setUp() to initialize the data needed to run the tests, reset any environment variables, and so on. For example, here is a setup () method:<br>
<pre><code>	protected void setUp() {<br>
	 <br>
	    System.setErr(new PrintStream(new ByteArrayOutputStream()));<br>
	 <br>
	    inputDir = new File("data");<br>
	    inputDir = new File(inputDir, "xslt");<br>
	    inputDir = new File(inputDir, "input");<br>
	 <br>
	}<br>
</code></pre>
Also in JUnit 4 it's possible to initialize the data and configure the environment before each test method: however, the method that performs these operations do not need to be named setUp(). He just needs to mark this method with the @Before annotation:<br>
<pre><code>	@Before protected void initialize() {<br>
	 <br>
	    System.setErr(new PrintStream(new ByteArrayOutputStream()));<br>
	 <br>
	    inputDir = new File("data");<br>
	    inputDir = new File(inputDir, "xslt");<br>
	    inputDir = new File(inputDir, "input");<br>
	}<br>
</code></pre>
It is also possible to have different test methods annotated with @Before: in this case, each of these will be running before test method:<br>
<pre><code>	@Before protected void findTestDataDirectory() {<br>
	    inputDir = new File("data");<br>
	    inputDir = new File(inputDir, "xslt");<br>
	    inputDir = new File(inputDir, "input");<br>
	}<br>
<br>
	@Before protected void redirectStderr() {<br>
	    System.setErr(new PrintStream(new ByteArrayOutputStream()));<br>
	}<br>
</code></pre>
Operations of the clean-up work in a similar way. In JUnit 3, we use the method tearDown() : note, in this snippet, we call the garbage collection to force the recovery of memory.<br>
<pre><code><br>
	protected void tearDown() {<br>
	  doc = null;<br>
	  System.gc();<br>
	}<br>
</code></pre>
In JUnit4, you can mark any method with @After annotation:<br>
<pre><code>	@After protected void disposeDocument() {<br>
	  doc = null;<br>
	  System.gc();<br>
	}<br>
</code></pre>
Similarly than the @Before annotation, you can create more clean-up methods marked with @After, each of which will running before all test methods.<br>
In addition, it's not more necessary to call explicitly the methods of initialization and clean-up of super-class.<br>
Until they are overwritten, the "test runner automatically call these methods, if necessary.<br>
@Before methods in super classes are invoked before the first @Before methods of subclasses (note that this behavior reflects the order of appearance of Manufacturers). @After methods are executed in reverse order: first those of the subclasses and then those of the super classes.<br>
<br>
<br>
<h2>Initialization of the test classes</h2>
JUnit 4 introduces a new feature that has no equivalent in JUnit 3: an initialization method, similar to setUp(), that operates at the class level. Any method annotated <code>@BeforeClass</code> will be executed once, after loading the test class, and before the execution of the test methods; any method annotated with <code>@AfterClass</code> will be executed once, after all methods tests were performed.<br>
For example, assume that each test class uses a connection to a database, a network connection, a large data structure, or some other resource that is particularly costly to initialize or download. Rather than recreate these resources before each test, you can initialize and destroy once. This approach will make the execution of some test cases much faster.<br>
<pre><code>	private PrintStream systemErr;<br>
	 <br>
	@BeforeClass protected void redirectStderr() {<br>
	    systemErr = System.err; // Hold on to the original value<br>
	    System.setErr(new PrintStream(new ByteArrayOutputStream()));<br>
	}<br>
	 <br>
	@AfterClass protected void tearDown() {<br>
	    // restore the original value<br>
	    System.setErr(systemErr);<br>
	}<br>
</code></pre>
<h2>How to test the exception</h2>
The test mechanism of the exceptions is one of the major improvements made to JUnit 4. In older versions of JUnit was used a try block that contains the code that should throw the exception, and a call to fail() in the end of the try block. For example, the following code verifies that an <code>ArithmeticException</code> is thrown:<br>
<pre><code>	public void testDivisionByZero() {<br>
	 <br>
	    try {<br>
	        int n = 2 / 0;<br>
	        fail("Divisione per zero.");<br>
	    }<br>
	    catch (ArithmeticException success) {<br>
	        assertNotNull(success.getMessage());<br>
	    }<br>
	}<br>
</code></pre>

In JUnit 4, you can explicitly write the code that generates the exception: annotation declares that it is expected that an exception is raised:<br>
<br>
<pre><code>	@Test(expected=ArithmeticException.class)<br>
	  public void divideByZero() {<br>
	    int n = 2 / 0;<br>
	}<br>
</code></pre>
If the exception is not raised or if it's different from that expected, the test will fail. Note that it may still be useful to the try-catch old-style, for instance if you want to check the error message, any exception details or other properties.<br>
<br>
<br>
<h2>New claims</h2>
JUnit 4 adds two new methods named assert() for comparisons of arrays; follow the definitions:<br>
<ul><li>public static void assertArrayEquals(Object<a href='.md'>.md</a> expecteds, Object<a href='.md'>.md</a> actuals)<br>
</li><li>public static void assertArrayEquals(String message, Object<a href='.md'>.md</a> expecteds, Object<a href='.md'>.md</a> actuals)</li></ul>

These two methods compare arrays in the obvious way: two arrays are equal if they are the same size and if each element is the same as in the corresponding array.<br>
<br>
<br>
<h1>Write a test case for eConference4</h1>
To write a test case for eConference4, we could use old syntax of JUnit3 or new syntax of JUnit4. Obviously it is better for programmers to use easier syntax of version 4 for future test cases, for conform to the same syntax.<br>
Here are the steps every time you need to write a new test case.<br>
<br>
<h2>Separate test cases from code eConference4.</h2>
To practice it is preferable to separate the program code from the code that performs tests on it.<br>
To do this, create a project, if not already present, in which you have to write the test cases related to a project. For example, as shown in the figure 2, there is a project called <b>it.uniba.di.cdg.jabber</b>; for this project was created another project called <b>it.uniba.di.cdg.jabber.test</b> in which we have write the test cases. This project must be provided inside the package with the same name as the project referred to, for example in the project there are the packages:<br>
<pre><code> it.uniba.di.cdg.jabber<br>
 it.uniba.di.cdg.jabber.action<br>
 it.uniba.di.cdg.jabber.internal<br>
 it.uniba.di.cdg.jabber.ui<br>
 it.uniba.di.cdg.smackproviders<br>
 it.uniba.di.cdg.xcore.aspects<br>
</code></pre>
Because until now we have been set up test cases only for packages:<br>
<pre><code>	it.uniba.di.cdg.jabber<br>
	it.uniba.di.cdg.jabber.internal<br>
</code></pre>
they must be present in the project <b>it.uniba.di.cdg.jabber.test</b>.<br>
<br>
You can see it in Figure 2.<br>
<br>
<p align='center'><img src='http://econference4.googlecode.com/svn/wiki/img/junit/SeparateTestCase.png' /> <br>
<p align='center'><b>Figure 2. Test case separate to source code</b></p>

In this package you have to write the code of test cases to which they relate.<br>
<br>
<h2>Add information on the new test case in the other classes for execution to various retail</h2>
When you create a new test case, we must ensure that this can be executed by calling him at package-level or at the project level and at the global level.<br>
So, once you've created the class of the test case, you must insert the link to this class  in <code>AllTests.java</code> conteined in the same package.<br>
<br>
To add this information in class, simply add to the list of the annotation <code>@Suite.SuiteClasses()</code> present in relative <code>AllTest.java</code>, the invocation to the new class.<br>
For example, if you're creating a test class called <code>XmppLoadTest.java</code> in the package <b>it.uniba.di.cdg.jabber</b> in the project <b>it.uniba.di.cdg.jabber.test</b>, you have to add the new class in <code>AllTests.java</code> content in the same package as shown .<br>
<p align='center'><img src='http://econference4.googlecode.com/svn/wiki/img/junit/AddTestCaseToAllTest.png' /> <br>
<p align='center'><b>Figure 2. Result of JUnit execution</b></p>

Instead, if you have created a new package with some class for test case, you need to create a class named <code>AllTest.java</code> into this package how the similar package; after, you must add the file <code>AllTest.class</code> created in the <code>projectname.allpackagetest.AllTest.java</code>. For example, if you have created the package named <code>it.uniba.di.cgd.jabber</code> with <code>AllTest.java</code>, you must insert into <code>it.uniba.di.cgd.jabber.test.allpackagetest.AllTest.java</code> the new class as shown in the figure below.<br>
<p align='center'><img src='http://econference4.googlecode.com/svn/wiki/img/junit/AddTestCaseToAllTest2.png' /> <br>
<p align='center'><b>Figure 3. AllTest for the it.uniba.di.cgd.jabber test  package</b></p>

Then, if you add a new package with a new AllTest at package level, make sure you remember to add it to the AllTest at project level. The project AllTest file is <code>it.uniba.di.cdg.econference.tests.AllTest</code> and belongs to the project named <code>it.uniba.di.cdg.econference-tests</code>. Look at the code snippet below as a clarification.<br>
<br>
<pre><code>package it.uniba.di.cdg.econference.tests;<br>
<br>
import org.junit.runner.JUnitCore;<br>
import org.junit.runner.RunWith;<br>
import org.junit.runners.Suite;<br>
<br>
@RunWith(Suite.class)<br>
@Suite.SuiteClasses({<br>
it.uniba.di.cdg.collaborativeworkbench.boot.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.jabber.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.skype.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.xcore.econference.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.xcore.m2m.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.xcore.network.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.xcore.ui.test.allpackagetests.AllTests.class,<br>
it.uniba.di.cdg.xcore.util.test.allpackagetests.AllTests.class })<br>
		<br>
public class AllTests {<br>
	public static void main(String[] args) {<br>
		JUnitCore.runClasses(new Class[] { AllTests.class });<br>
	}<br>
}<br>
<br>
</code></pre>