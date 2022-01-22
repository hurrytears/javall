package com.apachee.sql;

import com.apachee.core.Cube;
import com.apachee.core.Person;
import com.apachee.core.Square;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.apache.spark.sql.functions.col;

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

    @Test
    public void orcFiles(){
        // Schema Merging == schema evolution

    }

    @Test
    public void jsonFiles(){
        // A JSON dataset is pointed to by path.
        // The path can be either a single text file or a directory storing text files
        Dataset<Row> people = spark.read().json("examples/src/main/resources/people.json");

        // The inferred schema can be visualized using the printSchema() method
        people.printSchema();
        // root
        //  |-- age: long (nullable = true)
        //  |-- name: string (nullable = true)

        // Creates a temporary view using the DataFrame
        people.createOrReplaceTempView("people");

        // SQL statements can be run by using the sql methods provided by spark
        Dataset<Row> namesDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");
        namesDF.show();
        // +------+
        // |  name|
        // +------+
        // |Justin|
        // +------+

        // Alternatively, a DataFrame can be created for a JSON dataset represented by
        // a Dataset<String> storing one JSON object per string.
        List<String> jsonData = Arrays.asList(
                "{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
        Dataset<String> anotherPeopleDataset = spark.createDataset(jsonData, Encoders.STRING());
        Dataset<Row> anotherPeople = spark.read().json(anotherPeopleDataset);
        anotherPeople.show();
        // +---------------+----+
        // |        address|name|
        // +---------------+----+
        // |[Columbus,Ohio]| Yin|
        // +---------------+----+

        // 有各种options的花式读取，容错，读不读注释啊，分隔符啊这些
    }

    @Test
    public void csvFile(){
        // A CSV dataset is pointed to by path.
        // The path can be either a single CSV file or a directory of CSV files
        String path = "examples/src/main/resources/people.csv";

        Dataset<Row> df = spark.read().csv(path);
        df.show();
        // +------------------+
        // |               _c0|
        // +------------------+
        // |      name;age;job|
        // |Jorge;30;Developer|
        // |  Bob;32;Developer|
        // +------------------+

        // Read a csv with delimiter, the default delimiter is ","
        Dataset<Row> df2 = spark.read().option("delimiter", ";").csv(path);
        df2.show();
        // +-----+---+---------+
        // |  _c0|_c1|      _c2|
        // +-----+---+---------+
        // | name|age|      job|
        // |Jorge| 30|Developer|
        // |  Bob| 32|Developer|
        // +-----+---+---------+

        // Read a csv with delimiter and a header
        Dataset<Row> df3 = spark.read().option("delimiter", ";").option("header", "true").csv(path);
        df3.show();
        // +-----+---+---------+
        // | name|age|      job|
        // +-----+---+---------+
        // |Jorge| 30|Developer|
        // |  Bob| 32|Developer|
        // +-----+---+---------+

        // You can also use options() to use multiple options
        java.util.Map<String, String> optionsMap = new java.util.HashMap<String, String>();
        optionsMap.put("delimiter",";");
        optionsMap.put("header","true");
        Dataset<Row> df4 = spark.read().options(optionsMap).csv(path);

        // "output" is a folder which contains multiple csv files and a _SUCCESS file.
        df3.write().csv("output");

        // Read all files in a folder, please make sure only CSV files should present in the folder.
        String folderPath = "examples/src/main/resources";
        Dataset<Row> df5 = spark.read().csv(folderPath);
        df5.show();
        // Wrong schema because non-CSV files are read
        // +-----------+
        // |        _c0|
        // +-----------+
        // |238val_238|
        // |  86val_86|
        // |311val_311|
        // |  27val_27|
        // |165val_165|
        // +-----------+
    }

    @Test
    public void hiveTable(){
        // spark中不包含hive依赖，如果类路径下有，会自动加载。如果要访问hive数据，使用序列化和反序列化，
        // 则必须包含hive依赖。hive的配置需要把hive-site.xml,core-site.xml,hdfs-site.xml放在conf目录
        // 使用hive,sparkSession必须有hive支持。
        // 没有部署hive也能使用spark的hive功能，在这种情况下，context会自动创建一个元数据库放在当前目录下，
        // 当前目录默认是spark.sql.warehouse.dir/spark-warehouse, spark2.0.0版本之后，这个目录通过
        // spark.sql.warehouse.dir指定，保证用户对目录有使用权限
        // warehouseLocation points to the default location for managed databases and tables
        String warehouseLocation = new File("spark-warehouse").getAbsolutePath();
        SparkSession sparkHive = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                .config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport() // 本来无hive依赖也可以，但是windows有bug,就是不行
                .getOrCreate();

        sparkHive.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive");
        sparkHive.sql("LOAD DATA LOCAL INPATH 'examples/src/main/resources/kv1.txt' INTO TABLE src");

        // Queries are expressed in HiveQL
        sparkHive.sql("SELECT * FROM src").show();
        // +---+-------+
        // |key|  value|
        // +---+-------+
        // |238|val_238|
        // | 86| val_86|
        // |311|val_311|
        // ...

        // Aggregation queries are also supported.
        sparkHive.sql("SELECT COUNT(*) FROM src").show();
        // +--------+
        // |count(1)|
        // +--------+
        // |    500 |
        // +--------+

        // The results of SQL queries are themselves DataFrames and support all normal functions.
        Dataset<Row> sqlDF = sparkHive.sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key");

        // The items in DataFrames are of type Row, which lets you to access each column by ordinal.
        Dataset<String> stringsDS = sqlDF.map(
                (MapFunction<Row, String>) row -> "Key: " + row.get(0) + ", Value: " + row.get(1),
                Encoders.STRING());
        stringsDS.show();
        // +--------------------+
        // |               value|
        // +--------------------+
        // |Key: 0, Value: val_0|
        // |Key: 0, Value: val_0|
        // |Key: 0, Value: val_0|
        // ...

        // You can also use DataFrames to create temporary views within a SparkSession.
        List<Record> records = new ArrayList<>();
        for (int key = 1; key < 100; key++) {
            Record record = new Record();
            record.setKey(key);
            record.setValue("val_" + key);
            records.add(record);
        }
        Dataset<Row> recordsDF = sparkHive.createDataFrame(records, Record.class);
        recordsDF.createOrReplaceTempView("records");

        // Queries can then join DataFrames data with data stored in Hive.
        spark.sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show();
        // +---+------+---+------+
        // |key| value|key| value|
        // +---+------+---+------+
        // |  2| val_2|  2| val_2|
        // |  2| val_2|  2| val_2|
        // |  4| val_4|  4| val_4|
        // ...
    }

    @Test
    public void jdbc(){
        String url = "jdbc:postgresql://localhost:5432/fast";
        String user = "gpadmin";
        String password = "******";
        // Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
        // Loading data from a JDBC source
        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "fast_tool.cfg_scene_guyi_mapping_grid10")
                .option("user", user)
                .option("password", password)
                .load();

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", user);
        connectionProperties.put("password", password);
        Dataset<Row> jdbcDF2 = spark.read()
                .jdbc(url, "fast_tool.cfg_scene_guyi_mapping_grid10", connectionProperties);

        // Saving data to a JDBC source
        jdbcDF.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", "fast_tool.spark_test_save")
                .option("user", user)
                .option("password", password)
                .save();

        jdbcDF2.write()
                .jdbc(url, "fast_tool.spark_test_write", connectionProperties);

        // Specifying create table column data types on write
        // 变更写入的字段类型
        jdbcDF.write()
                .option("createTableColumnTypes", "scene_type int, city int")
                .jdbc(url, "fast_tool.spark_test_createColumn", connectionProperties);
    }
}
