package com.liuyiling.coding.mysql.table;

/**
 * 所有与数据库交互的类都需要继承该类
 * Created by liuyl on 2016/12/29.
 */
public class BaseDaoImpl {

    protected TableContainer tableContainer;

    public TableContainer getTableContainer() {
        return tableContainer;
    }

    public void setTableContainer(TableContainer tableContainer) {
        this.tableContainer = tableContainer;
    }

}
