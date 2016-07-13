package com.liuyiling.spring.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyl on 15/12/8.
 * jdbcTemplate的使用方式,api方法
 */
public class UserDaoImpl implements UserDao {


    private JdbcTemplate jdbcTemplate;


    @Override
    //execute可以执行任何的sql语句
    public void testExecute() {
        jdbcTemplate.execute("create table user(id INTEGER , name VARCHAR (100))");
    }

    //update方法用于执行新增、修改、删除等语句
    @Override
    public void tesetUpdate() {
        String insertSql = "INSERT INTO USER VALUES(?,?)";
        jdbcTemplate.update(insertSql, 1, "one");
    }


    @Override
    public void insert(User user) {
        String sql = "insert into user(id,name) values(?,?)";
        Object obj[] = {user.getId(), user.getName()};
        int temp = jdbcTemplate.update(sql, obj);
        if (temp > 0) {
            System.out.println("插入成功！");
        } else {
            System.out.println("插入失败！");
        }
    }

    @Override
    public void delete(int id) {
        String sql = "delete from user where id=" + id;
        int temp = jdbcTemplate.update(sql);
        if (temp > 0) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败！");
        }
    }

    @Override
    public void update(User user) {
        String sql = "update user set id=?,name=? where id=?";
        Object obj[] = {user.getId(), user.getName(), user.getId()};
        int temp = jdbcTemplate.update(sql, obj);
        if (temp > 0) {
            System.out.println("更新成功！");
        } else {
            System.out.println("更新失败！");
        }
    }

    @Override
    public List<User> selectAll() {
        String sql = "select * from user";
        List list = jdbcTemplate.query(sql, new RowMapper() {

            //RowMapper接口提供mapRow方法，将结果集的每一行转化成一个Map
            @Override
            public Object mapRow(ResultSet rs, int row) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                return user;
            }

        });
        return list;
    }

    @Override
    public User selectById(int id) {
        String sql = "select id,name from user where id=" + id;
        User user = (User) jdbcTemplate.queryForObject(sql, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                return user;
            }

        });

        return user;
    }

    @Override
    public List<User> finaAllUser() {
        String sql = "select * from admin";
        List<User> listAllUser = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            //RowCallbackHandler接口也提供方法processRow(ResultSet rs)，能将结果集的行转换为需要的形式
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                User u = new User();
                u.setId(resultSet.getInt("ID"));
                u.setName(resultSet.getString("NAME"));
                u.setPassword(resultSet.getString("PASSWORD"));
                listAllUser.add(u);
            }
        });


        return listAllUser;
    }


    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
