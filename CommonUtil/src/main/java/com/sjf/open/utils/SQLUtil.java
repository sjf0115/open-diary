package com.sjf.open.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by xiaosi on 17-2-6.
 */
public class SQLUtil {

    public static final String url = "jdbc:mysql://localhost/test";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";

    public Connection conn = null;
    public PreparedStatement preparedStatement = null;

    public SQLUtil(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            preparedStatement = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
