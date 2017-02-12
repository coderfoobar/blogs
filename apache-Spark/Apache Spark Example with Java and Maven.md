
# Apache Spark Example with Java and Maven

> Create Time : 2017年2月12日   Ref : http://www.robertomarchetto.com/spark_java_maven_example

Apache Spark has become the next cool `Big Data Technology`. Apache Spark is designed to work seamlessly with either Hadoop or as a standalone application . The big advantae of Spark standalone is the ease of use especially for models evaluation.

## Project Structure

This example consists of a `pom.xml` file and a `WorkCountTask.java` file. An additional test file with some random data is also present with the name of `loremipsum.txt` . The files can be found on GitHub : https://github.com/melphi/spark-examples/tree/master/first-example.

```txt
spark-examples
|-- pom.xml
·-- src
    |-- main/java/org/spark-example/WordCountTask.java
    ·-- test/java/WordCountTaskTest.java
    ·-- test/resources/loremipsum.txt
```

## Maven Configuration

This is the Maven `pom.xml` coniguration file , as you can see we need to import the `spark-core` library.The optional `maven-compile-plugin` is used to compile the project directly from Maven.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.sparkexamples</groupId>
    <artifactId>first-example</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- Spark -->
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.11</artifactId>
            <version>2.1.0</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.22</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## Java Application 

The `WorkCountTask.java` is a simple `Java Spark` which counts the number of words of the file passed as input argument.
The full source is here : https://github.com/melphi/spark-examples/blob/master/first-example/src/main/java/org/sparkexample/WordCountTask.java

```Java
package org.sparkexample;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * WordCountTask class, we will call this class with the test WordCountTest.
 */
public class WordCountTask {
    /**
     * We use a logger to print the output. Sl4j is a common library which works with log4j, the
     * logging system used by Apache Spark.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTask.class);

    /**
     * This is the entry point function when the task is called with spark-submit.sh from command
     * line. In our example we will call the task from a WordCountTest instead.
     * See {@see http://spark.apache.org/docs/latest/submitting-applications.html}
     */
    public static void main(String[] args) {
        checkArgument(args.length > 1, "Please provide the path of input file as first parameter.");
        new WordCountTask().run(args[1]);
    }

    /**
     * The task body
     */
    public void run(String inputFilePath) {
    /*
     * This is the address of the Spark cluster. We will call the task from WordCountTest and we
     * use a local standalone cluster. [*] means use all the cores available.
     * See {@see http://spark.apache.org/docs/latest/submitting-applications.html#master-urls}.
     */
        String master = "local[*]";

    /*
     * Initialises a Spark context.
     */
        SparkConf conf = new SparkConf()
                .setAppName(WordCountTask.class.getName())
                .setMaster(master);
        JavaSparkContext context = new JavaSparkContext(conf);

    /*
     * Performs a work count sequence of tasks and prints the output with a logger.
     */
        context.textFile(inputFilePath)
                .flatMap(text -> Arrays.asList(text.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b)
                .foreach(result -> LOGGER.info(
                        String.format("Word [%s] count [%d].", result._1(), result._2)));
    }
}
```

## Run as standalone local application

Running the application locally is just a matter of running `mvn test` from the project folder.
This will run an embedded version of Spark for testing purpose.

```Java
/**
 * test/java
 **/
package org.sparkexample;

import org.junit.Test;
import java.net.URISyntaxException;

public class WordCountTaskTest {
    @Test
    public void test() throws URISyntaxException {
        String path = this.getClass().getResource("/").getPath();
        System.out.println(path);
        String inputFile = path+"loremipsum.txt";
        new WordCountTask().run(inputFile);
    }
}
```

```txt
/**
 * test/resources/loremipsum.txt
 **/
 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec egestas tellus. Nunc efficitur nunc nunc. Fusce quis tortor sapien. Cras finibus nisl eu eros tincidunt, eget laoreet velit porta. Morbi pellentesque volutpat mollis. Quisque maximus tellus ut magna vulputate, at pharetra turpis ultricies. Donec eu quam justo. Suspendisse sit amet sollicitudin orci. Vivamus pulvinar sem in risus pulvinar dignissim. Nulla sit amet laoreet eros. Nullam sit amet erat dignissim, vulputate sapien at, tincidunt enim. Etiam nunc neque, condimentum eu dui at, vestibulum ornare odio.
Fusce sed dolor pulvinar, euismod mauris eu, elementum purus. In gravida sollicitudin quam nec ultricies. Aenean vel nisl eget metus lobortis luctus a at erat. Suspendisse ut ipsum quam. Mauris id justo non ligula aliquam tristique. Phasellus volutpat quam at neque fringilla, sed condimentum diam maximus. Proin ut quam aliquet, convallis elit at, dignissim sem. Nam eu arcu purus.
Cras et ligula ac mauris fringilla semper. Mauris interdum magna rhoncus pretium varius. Nulla fermentum est erat, eu interdum erat sodales nec. Quisque ornare suscipit eros, at tempus diam dapibus tristique. Morbi malesuada nibh ac justo faucibus volutpat. Curabitur nec lacus non neque euismod pharetra. Suspendisse odio ipsum, sodales vitae sapien ut, porta feugiat enim. Aliquam erat volutpat. Fusce elementum posuere dolor id auctor. Donec in ante pulvinar, malesuada purus non, tincidunt dui. Maecenas mollis in augue vitae vulputate. Donec condimentum fringilla auctor.
Aenean efficitur metus justo, posuere placerat urna efficitur eget. Nullam et est eu nibh dapibus fringilla. Praesent lobortis tincidunt odio, nec dapibus odio faucibus sit amet. In faucibus, magna eu tincidunt consequat, velit risus bibendum ligula, nec aliquam nisl dui sodales ligula. Integer at dapibus metus, id pellentesque mauris. Vivamus eleifend nisi id mollis dapibus. Donec ut ex sed mauris consectetur feugiat. Quisque viverra quam purus, eu ornare massa iaculis vitae. Praesent fringilla dui nec arcu feugiat, ac posuere dui ullamcorper. Suspendisse nec velit a ipsum euismod malesuada eu non nibh. Mauris aliquam quis quam sit amet condimentum.
Donec a sem dapibus, pretium elit at, fermentum dui. Etiam arcu ex, imperdiet tempor ex a,
convallis condimentum erat. Aliquam ullamcorper ultricies eros, vitae cursus ligula viverra in. Quisque et viverra sem, eget vehicula metus. Nam rutrum leo quam, a vestibulum diam auctor at. Integer diam leo, consectetur eget rhoncus ac, facilisis sit amet tellus. Duis mattis placerat vulputate. Nunc eu aliquet tellus, in varius erat. Pellentesque elementum cursus dolor, condimentum consectetur enim sagittis ac. Donec vehicula ut mauris non porttitor. Vivamus rutrum nunc et egestas vulputate. Proin nec tempor velit. Aliquam eget augue mollis, cursus arcu sed, tincidunt nulla. Aenean feugiat arcu eu mauris cursus gravida.#
```

## Set up Spark environment

`Apache Spark` can run as standalone service or installer in an Hadoop environment . Spark can be downloaded at https://spark.apache.org/downloads.html. 
For this example we will use the last available reslease for the package type "Prebuilt for Hadoop 2.7", however any other version should work without any change.

Once downloaded and decompressed the package is ready to be used (an Hadoop installation is not required for the standalone execution ), the only requirement is to have a Java virtual machine installed.

## Run the Java Application on Apache Spark cluster

The Java Application needs to be compiled first before executing it on `Apache Spark`.To compile the `Java Application` from `Maven`:

1. Open teh command line and move to the root maven project with `cd /<path to the project root>`.

2. Execute the command `mvn clean install -DskipTests`.Maven must be installed one the system path. Otherwise the command `mvn` will not be recognized . Refer to the maven documentation on how to set up maven properly.

3. Maven will build the java file and sve it on the target directory `/<path to the project root>/target/first-example-1.0-SNAPSHOT.jar`.

Once we have build the Java Application `first-example-1.0-SNAPSHOT.jar` we can execute it locally on `Apache Spark`, this makes the entire testing process very easy.

On a command shell move the the spark installation directory and use the following command:
```
./bin/spark-submit  --class org.sparkexample.WorkCountTask \
target/first-example-1.0-SNAPSHOT.jar /<path to output directory> /<path to a demo input file>
```

where 
* `--class org.sparkexample.WordCountTask` is the main Java class with the `public static void main(String[] args)` methos.
* `/<path to output directory>` is the directory where the result should be saved.
* `/<path to a demo input file>` is a demo input file which contains some words. A example input file as up `loremipsum.txt`.

If everything is fine , the output should be similar to the folling code seg and the word count result should be shown on console.

```txt
17/02/12 04:38:03 INFO ShuffleBlockFetcherIterator: Started 0 remote fetches in 17 ms
17/02/12 04:38:03 INFO WordCountTask: Word [package] count [1].
17/02/12 04:38:03 INFO WordCountTask: Word [For] count [3].
17/02/12 04:38:03 INFO WordCountTask: Word [Programs] count [1].
17/02/12 04:38:03 INFO WordCountTask: Word [processing.] count [1].
17/02/12 04:38:03 INFO WordCountTask: Word [Because] count [1].
17/02/12 04:38:03 INFO WordCountTask: Word [The] count [1].
``` 

All the files used in the turtorial can be found at :
https://github.com/melphi/spark-examples/tree/master/first-example.


