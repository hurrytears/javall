package com.apachee.datastructure;

/*
顺序表操作
 */
public class Demo01 {

    /*
     * 已知一个顺序表L，其中的元素递增有序排列，设计一个算法，插入一个元素x(x 为int 型）
     * 插入后保持该顺序表荏苒递增有序（假设插入操作总能成功）
     * 1.找到元素的位置
     * 2.插入元素
     */
    int findE(SqList L, int x){
        int i = 0;
        for ( ; i < L.length; i++) {
            if(x < L.data[i]) return i;
        }
        return i;
    }
    void insertElem(SqList L, int x){
        int p, i;
        p = findE(L, x);
        for(i = L.length -1; i>=p; --i)
            L.data[i+1] = L.data[i];
        L.data[p] = x;
        L.length ++;
    }
}
