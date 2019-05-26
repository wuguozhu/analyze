package com.anan.study.job;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.sources.In;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <b><code>KafkaStreamingWordCount</code></b>
 * <p>
 * 仿照官方样例
 * https://github.com/apache/spark/blob/master/examples/src/main/java/org/apache/spark/examples/streaming/JavaDirectKafkaWordCount.java
 * </p>
 *
 * @author wuguozhu
 * @date 2019-05-23 22:10
 * @since analyze 0.1.0
 */


public class KafkaStreamingWordCount {
    public static void main(String[] args) {


        //模式匹配
        final Pattern SPACE = Pattern.compile(",");

        //zookeeper 地址
        String zkQuorum = "192.168.6.174:2181";

        //消费组
        String group = "kafka-test";

        //主题
        String topic = "test";

        //每个话题的分片数
        int numThreads = 2;


        //拿配置
        SparkConf sparkConf = new SparkConf().setAppName("JavaKafkaWordCount").setMaster("local[*]");

        //上下文
        JavaStreamingContext jsc = new JavaStreamingContext(sparkConf, new Duration(10000));

        //设置检查点
        //jsc.checkpoint("");

        //将多topic 按逗号分隔，存入hashmap中
        Map<String, Integer> topicMap = new HashMap<>();

        String[] topicArr = topic.split(",");

        for (int i = 0; i < topicArr.length; i++) {
            topicMap.put(topicArr[i], numThreads);
        }

        //从Kafka中获取数据转换成RDD
        JavaPairReceiverInputDStream<String, String> message = KafkaUtils.createStream(jsc, zkQuorum, group, topicMap);

        Map<String,Object> kafkaParams = new HashMap<>();
        kafkaParams.put("auto.offset.reset","earliest");





        //打散单词
        JavaDStream<String> words = message.flatMap(new FlatMapFunction<Tuple2<String, String>, String>() {
            @Override
            public Iterable<String> call(Tuple2<String, String> message) throws Exception {
                return Arrays.asList(SPACE.split(message._2));
            }
        });

        JavaPairDStream<String, Integer> wordPair = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });


        JavaPairDStream<String, Integer> wordCount = wordPair.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) throws Exception {
                return i1 + i2;
            }
        });

        wordCount.print();

        //启动程序并计算
        jsc.start();
        jsc.awaitTermination();


    }

}
