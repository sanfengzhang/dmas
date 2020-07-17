package com.hanl.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * @author: Hanl
 * @date :2020/5/5
 * @desc:
 */
public class WordCount {

    public static void main(String[] args) {
        SparkConf sparkConf=new SparkConf().setAppName("wordCount");
        sparkConf.setMaster("local");
        JavaSparkContext jsc=new JavaSparkContext(sparkConf);
        JavaRDD<String> textFile=jsc.textFile("D:/word.txt");
        JavaPairRDD<String, Integer> counts = textFile.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1)) .reduceByKey((a, b) -> a + b);
        counts.saveAsTextFile("D:/wordCount.result.txt");
        jsc.close();

    }
}
