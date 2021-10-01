package com.apachee.exception;

/*
异常的产生过程分析

 */
public class Demo02 {

    public static void main(String[] args) {
        //创建int类型的数组，并赋值
        int[] arr = {1, 2, 3};
        int e = getElement(arr, 3);
        System.out.println(e);
    }

    /*
    访问了第四个元素，元素不存在
    1.JVM会根据产生的原因创建一个异常对象，这个异常对象包含了异常产生的（内容，原因，位置）
        new IndexOutOfBoundsException("3");
    2.在getElement 方法中，没有异常的处理逻辑（try-catch),那么JVM 就会把异常对象抛出给方法的调用者main方法

    main方法也没有处理，JVM会做两件事
    1.把异常对象在控制台打印出来
    2.终止当前正在执行的程序

     */
    private static int getElement(int[] arr, int i) {
        int ele = arr[i];
        return  ele;
    }


}
