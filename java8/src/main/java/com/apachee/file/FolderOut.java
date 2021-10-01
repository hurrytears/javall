package com.apachee.file;


import java.io.*;

public class FolderOut {

    public static void main(String[] args) throws IOException {

        File dir = new File("F:\\baidunetdiskdownload\\中华十三");
        getPpt(dir);
    }

    private static void getPpt(File dir) throws IOException {
        if(dir.isFile()){
            String name = dir.getName();
            if (name.endsWith(".ppt") || name.endsWith(".pptx")){
                System.out.println(dir.getPath());
                copyToPath(dir);
            }
        }else{
            for (File f: dir.listFiles()){
                if(f.getName().equals("ppt"))
                    continue;
                getPpt(f);
            }
        }
    }

    private static void copyToPath(File file) throws IOException {
        InputStream fi = new FileInputStream(file);
        System.out.println(file.getPath());
        String path = file.getPath().split("\\\\")[4];
        if(path.startsWith("第")){
            path = path.substring(1).replaceFirst("讲-", "_");
        }
        OutputStream fo = new FileOutputStream(new File("F:\\baidunetdiskdownload\\中华十三\\ppt\\"+ path + ".ppt"));
        byte[] buffer = new byte[1024];
        int bufferInt;
        while ((bufferInt = fi.read(buffer)) != -1){
            fo.write(buffer, 0, bufferInt);
        }
        fi.close();
        fo.close();
    }


    public void clearTargetDir(){
        File dir = new File("F:\\baidunetdiskdownload\\中华十三\\ppt");
        for(File f : dir.listFiles()){
            f.delete();
        }
    }


    public void test(){
        String str = "第59讲-";
        System.out.println(str.substring(1).replaceFirst("讲-", "_"));
    }
}
