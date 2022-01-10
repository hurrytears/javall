package com.apachee.core;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;

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
    }


}
