package com.apachee.mlib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class Demo1 {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("ml demo1").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        SQLContext jsql = new SQLContext(jsc);

        // Every record of this DataFrame contains the label and
        // features represented by a vector.
        StructType schema = new StructType(new StructField[]{
                new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("features", new VectorUDT(), false, Metadata.empty()),
        });
//        DataFrame df = jsql.createDataFrame(data, schema);

        // Set parameters for the algorithm.
        // Here, we limit the number of iterations to 10.
        LogisticRegression lr = new LogisticRegression().setMaxIter(10);

        // Fit the model to the data.
//        LogisticRegressionModel model = lr.fit(df);

        // Inspect the model: get the feature weights.
//        Vector weights = model.weights();

        // Given a dataset, predict each point's label, and show the results.
//        model.transform(df).show();
    }
}
