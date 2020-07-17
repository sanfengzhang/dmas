package com.hanl.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Hanl
 * @date :2020/5/12
 * @desc:
 */
public class ParallizeCollection {


    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local").setAppName(ParallizeCollection.class.getName());
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<Integer> javaRDD = javaSparkContext.parallelize(numbers, 2);

        int max = javaRDD.max(Comparator.naturalOrder());
        System.out.println(max);

        int count = javaRDD.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        System.out.println(count);

        javaSparkContext.close();
    }
}
