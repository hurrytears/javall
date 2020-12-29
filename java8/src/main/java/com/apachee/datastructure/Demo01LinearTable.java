package com.apachee.datastructure;

/*
顺序表操作
 */
public class Demo01LinearTable {

    /**
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

    /**
    * 删除顺序表L中下标为p(0<=p<=length-1)的元素，
    * 成功返回1，否则返回0
    * 并将删除元素的值赋给e
     */
    int deleteE(SqList L, int p){
        if(p<0 || p> L.length-1){
            return 0;
        }
        for(int i=p; i<L.length-1; i++){
            L.data[i] = L.data[i + 1];
        }
        L.length -= 1;
        return 0;
    }

    /**
     * A和B是两个单链表（带头节点），其中元素递增有序。
     * 设计一个算法，将A和B并成一个递增有序的链表C
     * C由A和B中的节点组成
     */
    void conpact(LNode A, LNode B){
        LNode p = A.next;
        LNode q = B.next;
        LNode C = new LNode();
        LNode r = C;
        while(p!=null && q!=null){
            if(p.data < q.data){
                r.next = p; r = r.next; p = p.next;
            }else {
                r.next = q; r = r.next; q = q.next;
            }
        }
        if(p == null) r.next = q;
        else r.next = p;
    }

    /**
     * 头插法建立单链表
     * @param arr 传递链表中要存储的值
     * @param n 数组的长度
     * @return 单链表
     */
    LNode tailInsert(int[] arr, int n){
        LNode A = new LNode();
        if(n>0) {
            for (int i = 0; i < n; ++i) {
                LNode r = new LNode();
                r.data = arr[i];
                r.next = A.next;
                A.next = r;
            }
        }
        return A;
    }

    /**
     * 尾插法建立单链表
     */
    LNode headInsert(int[] arr, int n){
        LNode A = new LNode();
        if(n>0) {
            LNode r = A;
            for (int i = 0; i < n; ++i) {
                LNode c = new LNode();
                c.data = arr[i];
                r.next = c;
                r = r.next;
            }
        }
        return A;
    }

    /**
     * 查找链表C中是否存在一个值为x的节点
     * 若存在，删除并返回1
     * 若不存在，返回0
     */
    int deleteX(LNode C, int x){
        LNode r = C;
        while (r.next != null && r.next.data != x){
            r = r.next;
        }
        if(r.next == null) return 0;
        LNode p = r.next;
        r.next = p.next;
        //释放 p
        return  1;
    }

    /**
     * 尾插法建立双链表
     */
    DLNode headInsertD(int[] arr, int n){
        DLNode A = new DLNode();
        if(n > 0) {
            DLNode r = A;
            for (int i = 0; i < n; ++i) {
                DLNode c = new DLNode();
                r.next = c;
                c.data = arr[i];
                c.prior = r;
                r = c;
            }
        }
        return A;
    }
}
