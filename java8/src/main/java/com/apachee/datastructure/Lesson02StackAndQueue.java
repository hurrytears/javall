package com.apachee.datastructure;

/*
顺序表操作
 */
public class Lesson02StackAndQueue {

    /**
     * C语言里算数表达式中的括号只有小括号。编写算法，判断一个表达式中的括号是否为正确配对，
     * 表达式已经存入字符数组exp[]中，表达式中的字符个数为n
     */
    int matches(char[] exp, int n){
        SqStack<String> sqStack = new SqStack<>();
        sqStack.initStack();
        for(int i=0; i<n; ++i){
            if('('==exp[i])
                sqStack.push("(");
            if(')'==exp[i]){
                if(sqStack.isEmpty())
                    return 0;
                else
                    --sqStack.top;
            }
        }
        if(sqStack.isEmpty())
            return 1;
        return 0;
    }

    /**
     * 求后缀表达式的数值
     */
    int op(int a, char op, int b){
        if(op=='+') return a+b;
        if(op=='-') return a-b;
        if(op=='*') return a*b;
        if(op=='/'){
            if(b==0) {
                try {
                    throw new Exception("devide by zero");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                return a / b;
            }
        }
        return 0;
    }
    int com(char exp[]){
        int i,a,b,c;
        SqStack<Integer> sqStack = new SqStack<>();
        char op;
        for(i=0; exp[i]!='\0'; ++i){
            if(exp[i]>='0' && exp[i]<='9') {
                sqStack.push(exp[i] - 0);
            }else {
                op = exp[i];
                b = sqStack.pop();
                a = sqStack.pop();
                c = op(a, op, b);
                sqStack.push(c);
            }
        }
        return sqStack.pop();
    }
}
