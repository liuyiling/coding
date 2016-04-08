package com.liuyiling.spring.ioc;

/**
 * Created by liuyl on 16/3/30.
 */
public class NoUse {

    private String name;
    private int id;
    private NoUseCase noUseCase = new NoUseCase(name, id);

    public void hello(){
        System.out.println(noUseCase.getName());
        System.out.println(noUseCase.getId());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
