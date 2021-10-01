package com.apachee.utils;

import java.io.*;

public class FindString {
    static FileReader fr = null;
    static BufferedReader br;

    public static void main(String[] args) throws IOException {
        File dir = new File("d:/Code");
        find(dir);
    }

    static void find(File dir) throws IOException {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                find(file);
            }
        } else {
            fr = new FileReader(dir);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toUpperCase().contains("CFG_GRID_AOI_INFO")) {
                    System.out.println(dir.getAbsolutePath());
                    System.out.println(line);
                }
            }
        }
    }
}
