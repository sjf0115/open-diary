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
 * 必须  启动hive的远程服务接口命令行执行：hive --service hiveserver2 >/dev/null 2>/dev/null &
 *
 */
public class HiveClient {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = "jdbc:hive2://localhost:10000/test";
    private static String user = "hive";
    private static String password = "";
    private static ResultSet res;
    private static Logger logger = LoggerFactory.getLogger(HiveClient.class);

    private static  Connection conn = null;
    private static  Statement stmt = null;

    /**
     * 连接
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static Connection getConn() throws ClassNotFoundException,
            SQLException {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * 初始化
     */
    public static void open(){
        try {
            conn = getConn();
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            logger.error(driverName + " not found! {}", e);
        } catch (SQLException e) {
            logger.error("Connection error! {}", e);
        }
    }

    /**
     * 执行SQL --- execute
     * @param sql
     */
    public static boolean execute(String sql){
        try {
            boolean result = stmt.execute(sql);
            return result;
        } catch (SQLException e) {
            logger.error("-------- execute 失败 {}",e);
        }
        return false;
    }

    /**
     * 执行SQL --- executeUpdate
     * @param sql
     * @return
     */
    public static int executeUpdate(String sql){
        try {
            int result = stmt.executeUpdate(sql);
            return result;
        } catch (SQLException e) {
            logger.error("-------- executeUpdate 失败 {}",e);
        }
        return 0;
    }

    /**
     * 执行SQL --- executeQuery
     * @param sql
     * @return
     */
    public static ResultSet executeQuery(String sql){
        try {
            ResultSet result = stmt.executeQuery(sql);
            return result;
        } catch (SQLException e) {
            logger.error("-------- executeQuery 失败 {}",e);
        }
        return null;
    }

    /**
     * 关闭
     */
    public static void close(){
        try {
            if (conn != null && conn.isClosed()) {
                conn.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            logger.error("---------- close error {}",e);
        }
    }
}
