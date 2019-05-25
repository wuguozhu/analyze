package com.anan.study.job;

/**
 * <b><code>TestPath</code></b>
 * <p>descirption
 * </p>
 *
 * @author wuguozhu
 * @date 2019-05-19 20:00
 * @since analyze 0.1.0
 *//**
 * <b><code>TestPath</code></b>
 * <p>descirption
 * </p>
 *
 * @author wuguozhu
 * @date 2019-05-19 20:00
 * @since analyze 0.1.0
 */

//MyClass.class.getResource(File.separator + "FILE_NAME").getPath(); // 这种方法相当于使用绝对运行时路径
public class TestPath {

    public static void main(String[] args){
        String ss = TestPath.class.getClassLoader().getResource("data").getPath();
        System.out.println(ss);
    }
}
