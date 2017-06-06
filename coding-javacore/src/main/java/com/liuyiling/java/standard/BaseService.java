package com.liuyiling.java.standard;

import com.liuyiling.java.basic.Animal;

import java.util.List;

/**
 * Created by liuyiling on 17/5/24.
 */
public interface BaseService {

    String animalDO = "animal_table";
    String animalDTO = "animal";

    void function();

    String company = "alibaba";

    Animal getAnimal();

    List<Animal> listAnimal();

    int countAnimal();

    void insertAnimal();

    void deleteAnimal();

    void updateAnimal();



}
