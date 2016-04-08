package com.liuyiling.spring.dependson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liuyl on 15/12/8.
 * 在此配置中，resourceBean初始化在dependentBean之前被初始化，resourceBean销毁会在dependentBean销毁之后执行。
 */
public class ResourceBean {

    private FileOutputStream fos;
    private File file;

//    初始化方法
    public void init(){
        System.out.println("ResourceBean: init");
        System.out.println("ResourceBean: load resource");

        try {
            this.fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void destory(){
        System.out.println("ResourceBean: destory");
        System.out.println("ResourceBean: destory,realease resource");

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileOutputStream getFos() {
        return fos;
    }

    public void setFos(FileOutputStream fos) {
        this.fos = fos;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
