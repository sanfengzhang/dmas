package com.hanl.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;
import scala.Tuple3;

import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapReduceJoin {
    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local").setAppName(ParallizeCollection.class.getName());
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        groupByKey(javaSparkContext);
        javaSparkContext.close();
    }

    private static void groupByKey(JavaSparkContext javaSparkContext) {
        List<Tuple2<String, Integer>> tuple2List = Arrays.asList(new Tuple2<String, Integer>("class1", 20), new Tuple2<String, Integer>("class2", 10),
                new Tuple2<String, Integer>("class1", 40), new Tuple2<String, Integer>("class2", 50));

        //这样就会有两个task执行
        JavaPairRDD<String, Integer> javaPairRDD = javaSparkContext.parallelizePairs(tuple2List,2);

        JavaPairRDD<String, Iterable<Integer>> groupbyRdd = javaPairRDD.groupByKey();

        groupbyRdd.foreach(new VoidFunction<Tuple2<String, Iterable<Integer>>>() {
            @Override
            public void call(Tuple2<String, Iterable<Integer>> stringIterableTuple2) throws Exception {

                System.out.println("thread=" + Thread.currentThread().getId() + ",class=" + stringIterableTuple2._1 + ",scores=" + stringIterableTuple2._2);
            }
        });

    }


}
