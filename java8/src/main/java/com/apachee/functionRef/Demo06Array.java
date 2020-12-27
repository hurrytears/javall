package com.apachee.functionRef;

@FunctionalInterface
interface ArrayBuilder{
    int[] buildArray(int len);
}

public class Demo06Array {
    static int[] create(int len, ArrayBuilder builder){
        return builder.buildArray(len);
    }

    public static void main(String[] args) {
        int[] arr1 = create(10, (len) -> {
            return new int[len];
        });
        System.out.println(arr1.length);

        int[] arr2 = create(11, int[]::new);
        System.out.println(arr2.length);
    }
}
