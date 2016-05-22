package com.liuyiling.spring.jdbc;

import java.util.List;

/**
 * Created by liuyl on 15/12/8.
 */
public interface UserDao {

    List<User> finaAllUser();

    void testExecute();

    void tesetUpdate();

    void insert(User user);

    void delete(int id);

    void update(User user);

    List<User> selectAll();

    User selectById(int id);



}
