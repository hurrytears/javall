package com.apachee.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * -X号: X信息输出时左对齐；
 * %p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL,
 * %d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921
 * %r: 输出自应用启动到输出该log信息耗费的毫秒数
 * %c: 输出日志信息所属的类目，通常就是所在类的全名
 * %t: 输出产生该日志事件的线程名
 * %l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main (TestLog4.java:10)
 * %x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
 * %%: 输出一个”%”字符
 * %F: 输出日志消息产生时所在的文件名称
 * %L: 输出代码中的行号
 * %m: 输出代码中指定的消息,产生的日志具体信息
 * %n: 输出一个回车换行符，Windows平台为”\r\n”，Unix平台为”\n”输出日志信息换行
 */
public class Demo {
    static Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        logger.info("Hello World");
        logger.warn("Hello World");
        logger.trace("Hello World");
        logger.debug("Hello World");
        sayHello();
    }

    static void sayHello(){
        logger.error("Hello World");
    }
}
