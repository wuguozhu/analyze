package com.anan.study.job;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
/**
 * <b><code>TestPath</code></b>
 * <p>descirption
 * </p>
 *
 * @author wuguozhu
 * @date 2019-05-19 20:00
 * @since analyze 0.1.0
 */

public class ExampleTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExampleTest.class);
    //获取数据路径
    private final static String data = TestPath.class.getClassLoader().getResource("data").getPath();
    //分隔符
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args){

        SparkConf sc = new SparkConf().setAppName("ExampleTest").setMaster("local[2]"); //获取spark配置
        JavaSparkContext jsc = new JavaSparkContext(sc); //spark 上下文
        JavaRDD<String> lines  = jsc.textFile(data); //读取数据

        //根据空格对单词分割
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterable<String> call(String s) throws Exception {
                return Arrays.asList(SPACE.split(s));
            }
        });

        //构建word，1
        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        //根据相同的key进行计算
        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

        //收集到一起，超大数据集合误用collect
        List<Tuple2<String, Integer>> output = counts.collect();

        //遍历输出
        for (Tuple2<?,?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }
        jsc.stop(); //关闭spark 上下文
    }
}
