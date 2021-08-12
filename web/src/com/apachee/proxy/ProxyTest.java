package com.apachee.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {
        Lenovo lenovo = new Lenovo();
        /**
         * @parameter1 类加载器
         * @parameter2 接口数组
         * @parameter3 处理器
         */
        SaleComputor proxy_lenovo = (SaleComputor) Proxy.newProxyInstance(lenovo.getClass().getClassLoader(), lenovo.getClass().getInterfaces(), new InvocationHandler() {

            /**
             * 代理逻辑编写
             * @param proxy 代理对象
             * @param method 封装的方法
             * @param args 传递的实际参数
             * @return
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                System.out.println("该方法执行了");
//                System.out.println(method.getName());
//                System.out.println(args[0]);

                if(method.getName().equals("sale")){
                    // 1.增强参数
                    double money = (double) args[0];
                    money = money * 0.85;
                    System.out.println("专车接送");
                    String obj = (String) method.invoke(lenovo, money);
                    System.out.println("免费送货");
                    // 2.增强返回值
                    return obj + "_鼠标垫";
                }else {
                    Object obj = method.invoke(lenovo, args);
                    return obj;
                }
            }
        });
        String computor = proxy_lenovo.sale(8000);
        System.out.println(computor);
    }
}
