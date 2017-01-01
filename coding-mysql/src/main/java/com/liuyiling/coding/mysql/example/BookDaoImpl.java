package com.liuyiling.coding.mysql.example;

import com.liuyiling.coding.mysql.table.BaseDaoImpl;
import com.liuyiling.coding.mysql.table.TableChannel;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在无按天分表 并且 没有多表连接查询的情况下
 * 一张表对应一个具体的Dao类
 * Created by liuyl on 2016/12/30.
 */
public class BookDaoImpl extends BaseDaoImpl {

    //spring配置中的itemName
    private static final String CHINESE_BOOK_TABLE_ITEM = "chinese_book_table_item";
    private static final String FOREIGN_BOOK_TABLE_ITEM = "foreign_book_table_item";

    //增加
    private final String INSERT_BOOK = "INSERT_BOOK";
    //删除
    private final String DEL_BOOK_BY_ID = "DEL_BOOK_BY_ID";
    //查询
    private final String GET_BOOK_BY_NAME = "GET_BOOK_BY_NAME";
    private final String GET_BOOKS_BY_TYPE = "GET_BOOKS_BY_TYPE";
    //修改
    private final String UPDATE_BOOK_TYPE_BY_ID = "UPDATE_BOOK_TYPE_BY_ID";

    public long insertBook(Book book) {

        TableChannel tableChannel = getTableChannel(INSERT_BOOK, BookLanguage.getByValue(book.getBookLanguage()));
        String sql = tableChannel.getSql();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        tableChannel.getJdbcTemplate().update(conn -> {

            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, book.getBookName());
            ps.setInt(2, book.getBookType());
            ps.setObject(3, book.getBookParams());
            ps.setObject(4, book.getBookTime());
            ps.setInt(5, book.getBookLanguage());

            return ps;
        }, keyHolder);

        long id = (Long) keyHolder.getKey();
        return id;
    }

    public boolean delChineseBook(long id) {
        TableChannel tableChannel = getTableChannel(DEL_BOOK_BY_ID, BookLanguage.CHINESE);
        String sql = tableChannel.getSql();

        int update = tableChannel.getJdbcTemplate().update(sql,
                new Object[]{id});

        return update == 1;
    }

    public boolean delForeignBook(long id) {
        TableChannel tableChannel = getTableChannel(DEL_BOOK_BY_ID, BookLanguage.FOREIGN);
        String sql = tableChannel.getSql();

        int update = tableChannel.getJdbcTemplate().update(sql,
                new Object[]{id});

        return update == 1;
    }

    public Book getChineseBookByName(String bookName) {
        TableChannel tableChannel = getTableChannel(GET_BOOK_BY_NAME, BookLanguage.CHINESE);

        String sql = tableChannel.getSql();
        List<Book> list = new ArrayList<>();
        tableChannel.getJdbcTemplate().query(sql, new Object[]{bookName}, (rs, id) -> {
            Book book = buildBook(rs);
            list.add(book);
            return null;
        });

        return list.isEmpty() ? null : list.get(0);
    }

    public Book getForeignBookByName(String bookName) {
        TableChannel tableChannel = getTableChannel(GET_BOOK_BY_NAME, BookLanguage.FOREIGN);

        String sql = tableChannel.getSql();
        List<Book> list = new ArrayList<>();
        tableChannel.getJdbcTemplate().query(sql, new Object[]{bookName}, (rs, id) -> {
            Book book = buildBook(rs);
            list.add(book);
            return null;
        });

        return list.isEmpty() ? null : list.get(0);
    }

    public List<Book> getChineseBooksByType(int booktype) {
        TableChannel tableChannel = getTableChannel(GET_BOOKS_BY_TYPE, BookLanguage.CHINESE);

        String sql = tableChannel.getSql();
        List<Book> list = new ArrayList<>();
        tableChannel.getJdbcTemplate().query(sql, new Object[]{booktype}, (rs, id) -> {
            Book book = BookDaoImpl.this.buildBook(rs);
            list.add(book);
            return null;
        });

        return list;
    }

    public List<Book> getForeignBooksByType(int booktype) {
        TableChannel tableChannel = getTableChannel(GET_BOOKS_BY_TYPE, BookLanguage.CHINESE);

        String sql = tableChannel.getSql();
        List<Book> list = new ArrayList<>();
        tableChannel.getJdbcTemplate().query(sql, new Object[]{booktype}, (rs, id) -> {
            Book book = BookDaoImpl.this.buildBook(rs);
            list.add(book);
            return null;
        });

        return list;
    }

    public boolean updateChineseBookTypeById(long bookId, int bookType) {
        TableChannel tableChannel = getTableChannel(UPDATE_BOOK_TYPE_BY_ID, BookLanguage.CHINESE);

        boolean result = tableChannel.getJdbcTemplate().update(tableChannel.getSql(), new Object[]{bookType, bookId}) == 1;

        return result;
    }

    public boolean updateForeignBookTypeById(long bookId, int bookType) {
        TableChannel tableChannel = getTableChannel(UPDATE_BOOK_TYPE_BY_ID, BookLanguage.FOREIGN);

        boolean result = tableChannel.getJdbcTemplate().update(tableChannel.getSql(), new Object[]{bookType, bookId}) == 1;

        return result;
    }

    private Book buildBook(ResultSet rs) throws SQLException {
        Book book = new Book();

        book.setId(rs.getLong("id"));
        book.setBookName(rs.getString("book_name"));
        book.setBookType(rs.getInt("book_type"));
        book.setBookParams(rs.getObject("book_params"));
        book.setBookTime(new Date(rs.getTimestamp("book_time").getTime()));
        book.setBookLanguage(rs.getInt("book_language"));

        return book;
    }

    private TableChannel getTableChannel(String sqlName, BookLanguage bookLanguage) {
        switch (bookLanguage) {
            case CHINESE:
                return tableContainer.getDefaultTableChannel(CHINESE_BOOK_TABLE_ITEM, sqlName);
            case FOREIGN:
                return tableContainer.getDefaultTableChannel(FOREIGN_BOOK_TABLE_ITEM, sqlName);
            default:
                return null;
        }
    }

}
