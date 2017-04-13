package org.liuyiling.chapter2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.liuyiling.chapter2.helper.DatabaseHelper;
import org.liuyiling.chapter2.model.Customer;
import org.liuyiling.chapter2.service.CustomerService;

import java.util.List;


/**
 * Created by liuyl on 2016/11/5.
 */
public class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init(){
        //单元测试前的初始化工作
        DatabaseHelper.executeSqlFile("sql/customer_init.sql");
    }

    @Test
    public void getCustomerListTest() throws Exception {
        List<Customer> customerList = customerService.getCustomerList();
        Assert.assertTrue(2 == customerList.size());
    }

    @Test
    public void getCustomerTest() {
        long id = 1;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }


}
