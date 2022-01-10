package com.apachee.core;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;

import java.util.*;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.percentile_approx;

public class SQLProgrammingGuide {

    private static SparkSession spark = SparkSession
            .builder()
            .appName(SQLProgrammingGuide.class.getName())
            .config("spark.some.config.option", "some-value")
            .master("local")
            .getOrCreate();

    @Test
    public void mysqljdbc(){
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "Richr00t");
        Dataset<Row> jdbcDF = spark.read()
                .jdbc("jdbc:mysql://localhost:3306/customer", "test1", properties);
        jdbcDF.registerTempTable("te");
        spark.sql("select * from te where id>3").show();
    }

    @Test
    public void demo(){
        JavaRDD<String> personRDD = spark.sparkContext()
                .textFile("../data/test/person.txt", 1)
                .toJavaRDD();
        Dataset<Row> personDF = spark.createDataFrame(personRDD, Person.class);
        Dataset<String> map = personDF.map((MapFunction<Row, String>) row -> row.getString(1), Encoders.STRING());
        personDF.show();
    }

    @Test
    public void getstarted() {
        Dataset<Row> df = spark.read().json("../data/test/demo.json");
        df.show();
        df.printSchema();
        df.select("name").show();
        df.select(col("name"), col("age").plus(1)).show();
        df.filter(col("age").gt(21)).show();
        df.groupBy("age").count().show();
        df.registerTempTable("people");
        Dataset<Row> sqlDF = spark.sql("select * from people");
        sqlDF.show();
        df.createOrReplaceGlobalTempView("people");
        spark.sql("select * from global_temp.people").show();
        spark.newSession().sql("select * from global_temp.people");
    }

    @Test
    public void createDataset() {

        Person person = new Person();
        person.setAge(32);
        person.setName("Andy");

        Encoder<Person> personEncoder = Encoders.bean(Person.class);
        Dataset<Person> javaBeanDS = spark.createDataset(Collections.singletonList(person), personEncoder);
        javaBeanDS.show();

        Encoder<Long> longEncoder = Encoders.LONG();
        Dataset<Long> primitiveDS = spark.createDataset(Arrays.asList(1L, 2L, 3L), longEncoder);
        Dataset<Long> transformedDS = primitiveDS.map(
                (MapFunction<Long, Long>) value -> value + 1L,
                longEncoder
        );
        transformedDS.collect();

        String path = "../data/test/person.json";
        Dataset<Person> peopleDS = spark.read().json(path).as(personEncoder);
        peopleDS.show();
    }

    @Test
    public void inferfingSchemaUserReflection() {
        JavaRDD<Person> peopleRDD = spark.read()
                .textFile("../data/test/person.txt")
                .javaRDD()
                .filter(line -> line.split(",").length > 1)
                .map(line -> {
                    String[] parts = line.split(",");
                    Person person = new Person();
                    person.setName(parts[0]);
                    person.setAge(Integer.parseInt(parts[1].trim()));
                    return person;
                });
        Dataset<Row> peopleDF = spark.createDataFrame(peopleRDD, Person.class);
        peopleDF.createOrReplaceTempView("people");
        Dataset<Row> teenagersDF = spark.sql("select name from people where age between 13 and 19");

        Encoder<String> stringEncoder = Encoders.STRING();
        Dataset<String> teenagerNamesByIndexDF = teenagersDF.map(
                (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                stringEncoder
        );
        teenagerNamesByIndexDF.show();

        Dataset<String> teenagerNamesByFieldDF = teenagersDF.map(
                (MapFunction<Row, String>) row -> "Name: " + row.<String>getAs("name"),
                stringEncoder
        );
        teenagerNamesByFieldDF.show();
    }

    @Test
    public void programmaticallyspecifyingSchema() {
        JavaRDD<String> peopleRDD = spark.sparkContext()
                .textFile("../data/test/person.txt", 1)
                .toJavaRDD();
        String schemaString = "name age";
        List<StructField> fields = new ArrayList<>();
        for (String fieldName : schemaString.split(" ")) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }
        StructType schema = DataTypes.createStructType(fields);
        // Convert records of the RDD (people) to Rows
        JavaRDD<Row> rowRDD = peopleRDD.map((Function<String, Row>) record -> {
            String[] attributes = record.split(",");
            return RowFactory.create(attributes[0], attributes[1].trim());
        });

        Dataset<Row> peopleDataFrame = spark.createDataFrame(rowRDD, schema);
        peopleDataFrame.createOrReplaceTempView("people");
        Dataset<Row> results = spark.sql("select * from people");

        Dataset<String> namesDS = results.map(
                (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                Encoders.STRING()
        );
        namesDS.show();
    }

    @Test
    public void genericLoadAndSaveFunction(){
        // 配置默认存储格式: spark.sql.sources.default
        Dataset<Row> peopleDF = spark.read().json("../data/test/person.json");
        peopleDF.select("name", "age").write().parquet("namesAndAges.parquet");
        Dataset<Row> usersDF = spark.read().load("../data/test/users.parquet");
        usersDF.select("name", "favorite_color").write().save("namesAndFavColors.parquet");
        Dataset<Row> peopleDFCsv = spark.read().format("csv")
                .option("sep", ";")
                .option("inferSchema", "true")
                .option("header", "true")
                .load("../data/test/people.csv");
        usersDF.write().format("orc")
                .option("orc.bloom.filter.columns", "favorite_color")
                .option("orc.dictionary.key.threshold", "1.0")
                .option("orc.column.encoding.direct", "name")
                .save("../data/test/users_with_options.orc");
        usersDF.write().format("parquet")
                .option("parquet.bloom.filter.enabled#favorite_color", "true")
                .option("parquet.bloom.filter.expected.ndv#favorite_color", "1000000")
                .option("parquet.enable.dictionary", "true")
                .option("parquet.page.write-checksum.enabled", "false")
                .save("users_with_options.parquet");
        Dataset<Row> sql = spark.sql("select * from parquet.`../data/test/users.parquet`");

        peopleDF.write().bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed");
        usersDF.write()
                .partitionBy("favorite_color")
                .format("parquet")
                .save("namesPartByColor.parquet");
        usersDF
                .write()
                .partitionBy("favorite_color")
                .bucketBy(42, "name")
                .saveAsTable("users_partitioned_bucketed");
        // 外部分区表需要 MSCK REPAIR TABLE
    }

    @Test
    public void genericFileSourceOption(){
        /**
         * dir1/
         *  ├── dir2/
         *  │    └── file2.parquet (schema: <file: string>, content: "file2.parquet")
         *  └── file1.parquet (schema: <file, string>, content: "file1.parquet")
         *  └── file3.json (schema: <file, string>, content: "{'file':'corrupt.json'}")
         */

        // 注意这里的目录默认不会递归
        //ignore corrupt files
        spark.sql("set spark.sql.files.ignoreCorruptFiles=true");
        spark.sql("set spark.sql.files.ignoreMissingFiles=true");
        Dataset<Row> testCorruptDF = spark.read().parquet(
                "dir1/",
                "dir2/"
        );
        // 只会显示出*.parquet
        testCorruptDF.show();

        Dataset<Row> testGlobFilterDF = spark.read().format("parquet")
                .option("pathGlobFilter", "*.parquet") // json file should be filtered out
                .load("examples/src/main/resources/dir1");
        testGlobFilterDF.show();

        // 递归查找
        Dataset<Row> recursiveLoadedDF = spark.read().format("parquet")
                .option("recursiveFileLookup", "true")
                .load("examples/src/main/resources/dir1");
        recursiveLoadedDF.show();
        // +-------------+
        // |         file|
        // +-------------+
        // |file1.parquet|
        // |file2.parquet|
        // +-------------+

        // spark.sql.session.timeZone
        Dataset<Row> beforeFilterDF = spark.read().format("parquet")
                // Only load files modified before 7/1/2020 at 05:30
                .option("modifiedBefore", "2020-07-01T05:30:00")
                // Only load files modified after 6/1/2020 at 05:30
                .option("modifiedAfter", "2020-06-01T05:30:00")
                // Interpret both times above relative to CST timezone
                .option("timeZone", "CST")
                .load("examples/src/main/resources/dir1");
        beforeFilterDF.show();
        // +-------------+
        // |         file|
        // +-------------+
        // |file1.parquet|
        // +-------------+
    }


    @Test
    public void parquetFiles(){
        Dataset<Row> peopleDF = spark.read().json("../data/test/person.json");
        peopleDF.write().parquet("../data/test/people.parquet");
        Dataset<Row> parquetFileDF = spark.read().parquet("../data/test/people.parquet");
        parquetFileDF.createOrReplaceTempView("parquetFile");
        Dataset<Row> namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19");
        Dataset<String> namesDS = namesDF.map(
                (MapFunction<Row, String>) row -> "Name: " + row.getString(0),
                Encoders.STRING());
        namesDS.show();

        // partition infer
        /**
         * path
         * └── to
         *     └── table
         *         ├── gender=male
         *         │   ├── ...
         *         │   │
         *         │   ├── country=US
         *         │   │   └── data.parquet
         *         │   ├── country=CN
         *         │   │   └── data.parquet
         *         │   └── ...
         *         └── gender=female
         *             ├── ...
         *             │
         *             ├── country=US
         *             │   └── data.parquet
         *             ├── country=CN
         *             │   └── data.parquet
         *             └── ...
         */
        // parquet的schema的自动推断，如果是不同的schema,会合并，默认是关闭的 option("mergeSchema", "true")开启
        // 分区可以通过配置basedir来指定推断的目录

        List<Square> squares = new ArrayList<>();
        for (int value = 1; value <= 5; value++) {
            Square square = new Square();
            square.setValue(value);
            square.setSquare(value * value);
            squares.add(square);
        }

            // Create a simple DataFrame, store into a partition directory
        Dataset<Row> squaresDF = spark.createDataFrame(squares, Square.class);
        squaresDF.write().parquet("data/test_table/key=1");

        List<Cube> cubes = new ArrayList<>();
        for (int value = 6; value <= 10; value++) {
            Cube cube = new Cube();
            cube.setValue(value);
            cube.setCube(value * value * value);
            cubes.add(cube);
        }

        // Create another DataFrame in a new partition directory,
        // adding a new column and dropping an existing column
        Dataset<Row> cubesDF = spark.createDataFrame(cubes, Cube.class);
        cubesDF.write().parquet("data/test_table/key=2");

        // Read the partitioned table
        Dataset<Row> mergedDF = spark.read().option("mergeSchema", true).parquet("data/test_table");
        mergedDF.printSchema();

        // The final schema consists of all 3 columns in the Parquet files together
        // with the partitioning column appeared in the partition directory paths
        // root
        //  |-- value: int (nullable = true)
        //  |-- square: int (nullable = true)
        //  |-- cube: int (nullable = true)
        //  |-- key: int (nullable = true)

        /**
         * 从hive读取数据然后写入parquet,这个过程spark为了更高的性能会使用自己的引擎
         * 但是hive和parquet存在两点不同，hive大小写不敏感，并且所有字段都可以为空
         * 要解决这两个冲突，有如下两个处理规则:
         * 1.字段名称相同的字段类型必须相同，忽略空或非空属性，为兼容parquet,需要重视是否为空
         * 2.字段只包括hive metastore中的部分，只出现再parquet中的被忽略
         */

        // Spark SQL caches Parquet metadata for better performance.刷新表已保持hive表元数据同步
        spark.catalog().refreshTable("my_table");

        // 字段加密, spark3.2版本开始
//        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext().getConf());
        SparkContext sc = spark.sparkContext();
        sc.hadoopConfiguration().set("parquet.encryption.kms.client.class" ,
                "org.apache.parquet.crypto.keytools.mocks.InMemoryKMS");

        // Explicit master keys (base64 encoded) - required only for mock InMemoryKMS
        sc.hadoopConfiguration().set("parquet.encryption.key.list" ,
                "keyA:AAECAwQFBgcICQoLDA0ODw== ,  keyB:AAECAAECAAECAAECAAECAA==");

        // Activate Parquet encryption, driven by Hadoop properties
        sc.hadoopConfiguration().set("parquet.crypto.factory.class" ,
                "org.apache.parquet.crypto.keytools.PropertiesDrivenCryptoFactory");

        // Write encrypted dataframe files.
        // Column "square" will be protected with master key "keyA".
        // Parquet file footers will be protected with master key "keyB"
        squaresDF.write().
                option("parquet.encryption.column.keys" , "keyA:square").
                option("parquet.encryption.footer.key" , "keyB").
                parquet("/path/to/table.parquet.encrypted");

        // Read encrypted dataframe files
        Dataset<Row> df2 = spark.read().parquet("/path/to/table.parquet.encrypted");

    }
}
