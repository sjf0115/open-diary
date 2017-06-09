package com.sjf.open.sql;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Created by xiaosi on 17-6-6.
 */
public class DataSourceDemo {

    private static String path = "SparkDemo/src/main/resources/people.json";
    private static String textPath = "SparkDemo/src/main/resources/people.txt";
    private static String employeePath = "SparkDemo/src/main/resources/employees.json";

    /**
     * 初始化
     * 
     * @return
     */
    public static SparkSession init() {
        SparkSession session = SparkSession.builder().appName("Java Spark SQL Data Source example")
                .config("spark.master", "local").getOrCreate();
        return session;
    }

    /**
     * 加载
     * 
     * @param session
     */
    public static void load(SparkSession session) {
        Dataset<Row> usersDF = session.read().load("SparkDemo/src/main/resources/users.parquet");
        usersDF.show();
    }

    /**
     * 保存
     * 
     * @param session
     */
    public static void save(SparkSession session) {
        Dataset<Row> usersDF = session.read().load("SparkDemo/src/main/resources/users.parquet");
        usersDF.select("name").write().save("users-name.parquet");
    }

    /**
     * 手动指定选项
     * 
     * @param session
     */
    public static void specifyOptions(SparkSession session) {
        Dataset<Row> peopleDF = session.read().format("json").load("SparkDemo/src/main/resources/people.json");
        peopleDF.select("name", "age").write().format("parquet").save("namesAndAges.parquet");
    }

    /**
     * 直接在文件上运行SQL
     * 
     * @param session
     */
    public static void sqlOnFile(SparkSession session) {
        Dataset<Row> sqlDF = session.sql("SELECT * FROM parquet.`SparkDemo/src/main/resources/users.parquet`");
        sqlDF.show();
    }

    /**
     * 加载Parquet文件数据
     * 
     * @param session
     */
    public static void loadByParquet(SparkSession session) {

        Dataset<Row> peopleDF = session.read().json("SparkDemo/src/main/resources/people.json");
        // DateFrame保存为parquet文件并保留schema信息
        peopleDF.write().parquet("people.parquet");

        // 读取上面创建的parquet文件
        Dataset<Row> parquetFileDF = session.read().parquet("people.parquet");
        parquetFileDF.show();

        // Parquet文件也可用于创建临时视图 然后在SQL语句中使用
        parquetFileDF.createOrReplaceTempView("parquetFile");
        Dataset<Row> namesDF = session.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19");
        Dataset<String> namesDS = namesDF.map(new MapFunction<Row, String>() {
            public String call(Row row) {
                return "Name: " + row.getString(0);
            }
        }, Encoders.STRING());
        namesDS.show();
    }

    /**
     *
     */
    public static void hiveTable() {
        // warehouseLocation指向数据库和表的默认位置
        String warehouseLocation = "/home/xiaosi/code/OpenDiary/SparkDemo/spark-warehouse";
        SparkSession session = SparkSession.builder()
                .appName("Java Spark Hive Example")
                .config("spark.sql.warehouse.dir", warehouseLocation)
                .config("spark.master", "local")
                .enableHiveSupport()
                .getOrCreate();
        session.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)");
        session.sql("LOAD DATA LOCAL INPATH 'SparkDemo/src/main/resources/kv1.txt' INTO TABLE src");

        session.sql("SELECT * FROM src").show();
    }

    public static void main(String[] args) throws Exception {
        //SparkSession session = init();
        hiveTable();
    }

}
