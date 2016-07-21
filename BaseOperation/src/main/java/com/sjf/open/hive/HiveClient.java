package com.sjf.open.hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by xiaosi on 16-7-14.
 *
 * 必须  启动hive的远程服务接口命令行执行：hive --service hiveserver >/dev/null 2>/dev/null &
 *
 */
public class HiveClient {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://localhost:10000/default";
    private static String user = "hive";
    private static String password = "";
    private static ResultSet res;
    private static Logger logger = LoggerFactory.getLogger(HiveClient.class);

    private static Connection getConn() throws ClassNotFoundException,
            SQLException {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    private static void select(Statement stmt , String tableName){
        String sql = "select * from " + tableName;
        System.out.println("Running:" + sql);
        try {
            res = stmt.executeQuery(sql);
            System.out.println("执行 select * query 运行结果:");
            while (res.next()) {
                System.out.println(res.getInt(1) + "\t" + res.getString(2));
            }//while
        } catch (SQLException e) {
            logger.error("-------- 查询Hive表 失败 {}",e);
        }
    }

    private static void run(){
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConn();
            stmt = conn.createStatement();

            select(stmt, "employee");

        } catch (ClassNotFoundException e) {
            logger.error(driverName + " not found!", e);
        } catch (SQLException e) {
            logger.error("Connection error!", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        run();
    }
}
