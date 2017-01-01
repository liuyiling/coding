package com.liuyiling.coding.mysql.example;

/**
 * Created by liuyl on 2016/12/30.
 */
public enum BookLanguage {

    CHINESE(1),     //中文图书
    FOREIGN(2);    //外文图书

    private int value;

    BookLanguage(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static BookLanguage getByValue(int value) {
        BookLanguage[] values = BookLanguage.values();

        for (BookLanguage bookLanguage : values) {
            if (bookLanguage.value == value) {
                return bookLanguage;
            }
        }

        throw new IllegalArgumentException("BookLanguage getByValue exception value:" + value);
    }

}
