package com.liuyiling.coding.mysql.example;

import java.util.Date;

/**
 * 对应数据库中的Book实体类
 * Created by liuyl on 2016/12/30.
 */
public class Book {

    private long id;
    private String bookName;
    private int bookType;
    private Object bookParams;
    private Date bookTime;
    private int bookLanguage;

    public Book() {
    }

    public Book(String bookName, int bookType, Object bookParams, Date bookTime, int bookLanguage) {
        this.bookName = bookName;
        this.bookType = bookType;
        this.bookParams = bookParams;
        this.bookTime = bookTime;
        this.bookLanguage = bookLanguage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookType() {
        return bookType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public Object getBookParams() {
        return bookParams;
    }

    public void setBookParams(Object bookParams) {
        this.bookParams = bookParams;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public int getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(int bookLanguage) {
        this.bookLanguage = bookLanguage;
    }
}
