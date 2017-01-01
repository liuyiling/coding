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
 * Created by liuyl on 2016/12/30.
 */
public class OrderDaoImpl extends BaseDaoImpl {

    //spring配置中的itemName
    private static final String ORDER_TABLE_ITEM = "order_table_item";
    private static final String FINISHED_ORDER_TABLE_ITEM = "finished_order_table_item";

    //增加
    private final String INSERT_ORDER = "INSERT_ORDER";
    //查询
    private final String GET_ORDERS_BY_UID = "GET_ORDERS_BY_UID";
    private final String GET_ALL_ORDER = "GET_ALL_ORDER";

    public long insertOrder(Order order) {
        TableChannel tableChannel = tableContainer.getTableChannel(ORDER_TABLE_ITEM, INSERT_ORDER, order.getUid(), order.getUid());
        String sql = tableChannel.getSql();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        tableChannel.getJdbcTemplate().update(conn -> {

            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, order.getBookId());
            ps.setLong(2, order.getUid());

            return ps;
        }, keyHolder);

        long id = (Long) keyHolder.getKey();
        return id;
    }

    /**
     * 从所有的数据库中获取和数据表中获取对应的Order
     *
     * @return
     */
    public List<Order> getOrders() {
        List<TableChannel> allTableChannel = tableContainer.getAllTableChannel(ORDER_TABLE_ITEM, GET_ALL_ORDER);

        List<Order> list = new ArrayList<>();
        for (TableChannel tableChannel : allTableChannel) {
            String sql = tableChannel.getSql();

            tableChannel.getJdbcTemplate().query(sql, new Object[]{}, (rs, id) -> {
                Order order = buildOrder(rs);
                list.add(order);
                return null;
            });

        }
        return list;
    }

    public long insertFinishedOrder(Order order) {
        TableChannel tableChannel = tableContainer.getTableChannel(FINISHED_ORDER_TABLE_ITEM, INSERT_ORDER, order.getUid(), new Date());
        String sql = tableChannel.getSql();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        tableChannel.getJdbcTemplate().update(conn -> {

            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, order.getBookId());
            ps.setLong(2, order.getUid());

            return ps;
        }, keyHolder);

        long id = (Long) keyHolder.getKey();
        return id;
    }

    /**
     * 从所有的数据库中获取和数据表中获取对应的Order
     *
     * @return
     */
    public Order getFinishedOrderById(long uid) {
        TableChannel tableChannel = tableContainer.getTableChannel(FINISHED_ORDER_TABLE_ITEM, GET_ORDERS_BY_UID, uid, new Date());

        List<Order> list = new ArrayList<>();
        String sql = tableChannel.getSql();

        tableChannel.getJdbcTemplate().query(sql, new Object[]{uid}, (rs, id) -> {
            Order order = buildOrder(rs);
            list.add(order);
            return null;
        });

        return list.isEmpty() ? null : list.get(0);
    }

    private Order buildOrder(ResultSet rs) throws SQLException {
        Order order = new Order();

        order.setId(rs.getLong("id"));
        order.setBookId(rs.getLong("book_id"));
        order.setUid(rs.getLong("uid"));

        return order;
    }
}
