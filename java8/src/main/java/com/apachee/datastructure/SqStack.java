package com.apachee.datastructure;

/**
 * 顺序栈
 * top = -1 为栈空
 * top = maxSize-1 为栈满
 */
public class SqStack<T> {
    T[] data = (T[]) new Object[100];
    int top;

    void initStack(){
        top = -1;
    }

    boolean isEmpty(){
        return top == -1;
    }

    boolean push(T x){
        if(top == 99)
            return false;
        data[++top] = x;
        return true;
    }

    T pop(){
        if(top == -1){
            try {
                throw new Exception("栈空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data[--top];
    }
}
