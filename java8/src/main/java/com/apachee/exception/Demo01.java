package com.apachee.exception;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
Throwable 类是java语言中所有错误和异常的超类
    Exception: 编译器异常，进行编译（写代码）java程序出现的问题
        RuntimeException:运行期异常，java程序运行过程中的问题
    Error: 错误
        必须修复后才能执行

 */
public class Demo01 {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;

        {
            try {
                date = sdf.parse("1999-8999");
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //RuntimeException
        int[] arr = {1, 2, 3};
        System.out.println(arr[3]);

        /*
        错误
        必须修改代码，让内存占用小一点
         */
        int[] arr2 = new int[1024*1024*1024];
    }
}
