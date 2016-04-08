package com.liuyiling.spring.jdbc;

import java.util.List;

/**
 * Created by liuyl on 15/12/8.
 */
public interface UserDao {

    public List<User> finaAllUser();

    public void testExecute();

    public void tesetUpdate();

    public void insert(User user);

    public void delete(int id);

    public void update(User user);

    public List<User> selectAll();

    public User selectById(int id);



}
