package com.liuyiling.coding.mysql.example;

/**
 * Created by liuyl on 2016/12/30.
 */
public class Order {

    private long id;
    private long bookId;
    private long uid;

    public Order() {
    }

    public Order(long id, long bookId, long uid) {
        this.id = id;
        this.bookId = bookId;
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
