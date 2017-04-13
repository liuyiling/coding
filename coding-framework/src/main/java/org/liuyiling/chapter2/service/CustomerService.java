package org.liuyiling.chapter2.service;

import org.liuyiling.chapter2.helper.DatabaseHelper;
import org.liuyiling.chapter2.model.Customer;
import org.liuyiling.chapter2.util.PropsUtil;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by liuyl on 2016/11/5.
 */
public class CustomerService {

    public List<Customer> getCustomerList() {

        //Connection conn = DatabaseHelper.getConnection();
        //List<Customer> customerList = new ArrayList<Customer>();
        //
        //try {
        //    String sql = "select * from customer";
        //    PreparedStatement stmp = conn.prepareStatement(sql);
        //    ResultSet rs = stmp.executeQuery();
        //
        //    while(rs.next()){
        //        Customer customer = new Customer();
        //        customer.setId(rs.getLong("id"));
        //        customer.setName(rs.getString("name"));
        //        customer.setContact(rs.getString("contact"));
        //        customer.setTelephone(rs.getString("contact"));
        //        customer.setEmail(rs.getString("email"));
        //        customer.setRemark(rs.getString("remark"));
        //        customerList.add(customer);
        //    }
        //
        //    return customerList;
        //} catch (Exception e) {
        //    LOGGER.error("execute sql failure", e);
        //} finally {
        //    DatabaseHelper.closeConnection(conn);
        //}
        //return customerList;

        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);

    }

    public Customer getCustomer(long id) {
        return null;
    }

    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }

}
