package com.hanl.spark;

import avro.shaded.com.google.common.collect.Maps;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import scala.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/5/5
 * @desc:
 */
public class SparkEtlApp {

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().setAppName("wordCount");
        sparkConf.setMaster("local");
       // sparkConf.set("spark.defalut.parallelism","2");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        LongAccumulator total = new LongAccumulator();
        total.register(jsc.sc(), Option.apply("total"), false);
        JavaRDD<String> textFile = jsc.textFile("D:/word.txt",2);
        JavaRDD<Map<String, Object>> mapJavaRDD = textFile.mapPartitions(consumerRecordIterator -> {
            List<Map<String, Object>> res = new ArrayList<>();
            int count = 0;
            while (consumerRecordIterator.hasNext()) {
                String content = consumerRecordIterator.next();
                String key = "第" + count + "行";
                Map map = Maps.newHashMap();
                map.put(key, content);
                res.add(map);
                count++;
                ((LongAccumulator) total).add(1);
            }
            System.out.println("mapPartitions="+total);
            return res.iterator();
        });
        mapJavaRDD.saveAsTextFile("D:/wordCount-1");
        System.out.println(total.count());
        jsc.close();
    }
}
