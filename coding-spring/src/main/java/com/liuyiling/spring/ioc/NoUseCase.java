package com.liuyiling.spring.ioc;

/**
 * Created by liuyl on 16/3/30.
 */
public class NoUseCase {

    private String name;
    private int id;

    public NoUseCase(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public int getId() {
        return id;
    }
}
