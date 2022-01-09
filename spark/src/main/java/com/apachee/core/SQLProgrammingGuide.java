package com.apachee.core;

import org.apache.commons.lang3.builder.ToStringExclude;
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
    public void startjdbc(){
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


}
