package com.apachee.datastructure;

/*
单链表/链栈
LNode *A = (LNode*)malloc(sizeof(LNode));
 */
public class LNode<T> {
    T data;
    LNode<T> next;

    //链栈操作

    boolean isEmpty(LNode lst){
        if(lst.next == null)
            return true;
        else
            return false;
    }

    void push(LNode lst, T x){
        LNode p = new LNode();
        p.data=x;
        p.next = lst.next;
        lst.next = p;
    }

    T pop(LNode lst, T x){
        if(lst.next == null){
            try {
                throw new Exception("链栈空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LNode p = lst.next;
        lst.next = p.next;
        x = (T) p.data;
        return x;
    }
}

