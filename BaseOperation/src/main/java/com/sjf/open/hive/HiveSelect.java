package com.sjf.open.hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by xiaosi on 16-7-24.
 */
public class HiveSelect {

    private static Logger logger = LoggerFactory.getLogger(HiveSelect.class);
    private static String sql;

    private static void getAllEmployees(){
        HiveClient.open();

        sql = "select * from employee";

        try {
            ResultSet resultSet = HiveClient.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + "\t" + resultSet.getFloat(2) + "\t" + resultSet.getString(3));
            }//while
        } catch (SQLException e) {
            logger.error("-------- 查询Hive表 失败 {}",e);
        }
    }

    public static void main(String[] args) {
        getAllEmployees();
    }
}
