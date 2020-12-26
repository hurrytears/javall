package com.apachee.functional;

//日志案例

/*
一下代码存在性能浪费
调用showLog方法，传递的第二个参数是一个拼接后的字符串
先把字符串拼接好，然后再调用showLog方法
那么就不是如此拼接后的字符串
所以感觉字符串是白拼接了，存在了浪费

使用Lambda优化，延迟加载
Lambda使用前提，必须存在函数式接口
 */
@FunctionalInterface
interface MessageBuileder{
    public abstract String builderMessage();
}
public class Demo02Lazy {

    public static void showLog(int level, String message){
        //对日志等级做判断，如果是1等级，那么输出日志信息
        if(level == 1){
            System.out.println(message);
        }
    }

    static void showLog2(int level, MessageBuileder mb){
        if(level == 1){
            System.out.println(mb.builderMessage());
        }
    }

    public static void main(String[] args) {
        String msg1 = "Hello";
        String msg2 = "World";
        String msg3 = "Java";

        //性能浪费
        showLog(1, msg1 + msg2 + msg3);

        //使用lambda表达式作为参数，只有满足条件，才会调用接口中的msg拼接方法
        showLog2(2, ()->{
            System.out.println("不满足条件不执行");
            //返回一个拼接好的字符串
            return msg1 + msg2 + msg3;
        });
    }
}
