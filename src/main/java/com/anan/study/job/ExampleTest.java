package com.anan.study.job;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;

public class ExampleTest {


    public static void main(String[] args){

        SparkConf sc = new SparkConf().setAppName("ExampleTest").setMaster("local[2]");
        JavaSparkContext jsc = new JavaSparkContext(sc);

        HiveContext hc = new HiveContext(jsc);

        hc.sql("").show();

    }
}
