package com.sjf.open.sql;

import com.sjf.open.model.Record;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
     * 与Json交互
     * @param session
     */
    public static void sparkJson(SparkSession session){
        // JSON数据集由给定路径指定
        // 该路径可以是单个文本文件或存储文本文件的目录
        Dataset<Row> people = session.read().json("SparkDemo/src/main/resources/people.json");
        // 推断模式
        people.printSchema();

        // 使用DataFrame创建临时视图
        people.createOrReplaceTempView("people");

        // 可以使用由spark提供的sql方法来运行SQL语句
        Dataset<Row> namesDF = session.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");
        namesDF.show();

        // DataFrame可以由一个RDD[String](每个字符串一个JSON对象)表示的JSON数据集创建
        List<String> jsonData = Arrays.asList(
                "{\"name\":\"Yin\",\"address\":{\"city\":\"Columbus\",\"state\":\"Ohio\"}}");
        JavaRDD<String> anotherPeopleRDD =
                new JavaSparkContext(session.sparkContext()).parallelize(jsonData);
        Dataset anotherPeople = session.read().json(anotherPeopleRDD);
        anotherPeople.show();
    }

    /**
     * 与Hive交互
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
        //session.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)");
        //session.sql("LOAD DATA LOCAL INPATH 'SparkDemo/src/main/resources/kv1.txt' INTO TABLE src");

        // 使用HiveQL进行查询
        session.sql("SELECT * FROM src").show();

        // 支持聚合查询
        session.sql("SELECT COUNT(*) FROM src").show();

        // SQL查询的结果本身就是DataFrames 并支持所有基本的功能
        Dataset<Row> sqlDF = session.sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key");

        // DataFrame中的条目数据类型是Row 它允许你按顺序访问每一列
        Dataset<String> stringsDS = sqlDF.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Key: " + row.get(0) + ", Value: " + row.get(1);
            }
        }, Encoders.STRING());
        stringsDS.show();

        // 使用DataFrame在SparkSession中创建临时视图
        List<Record> records = new ArrayList<>();
        for (int key = 1; key < 100; key++) {
            Record record = new Record();
            record.setKey(key);
            record.setValue("val_" + key);
            records.add(record);
        }
        Dataset<Row> recordsDF = session.createDataFrame(records, Record.class);
        recordsDF.createOrReplaceTempView("records");
        // 可以使用Hive中存储的数据与DataFrames数据进行Join操作
        session.sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show();

    }

    /**
     * 与JDBC交互
     */
    public static void jdbc(SparkSession session){

        // JDBC加载和保存可以通过load / save或jdbc方法来实现

        // 使用load方法加载或保存JDBC
        String url = "jdbc:mysql://localhost:3306/test";
        String personsTable = "Persons";
        String personsCopyTable = "PersonsCopy";

        Dataset<Row> jdbcDF = session.read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", personsTable)
                .option("user", "root")
                .option("password", "root")
                .load();

        jdbcDF.write()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", personsCopyTable)
                .option("user", "root")
                .option("password", "root")
                .save();

        // 使用jdbc方法加载或保存JDBC
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "root");
        Dataset<Row> jdbcDF2 = session.read().jdbc(url, personsTable, connectionProperties);

        jdbcDF2.write().jdbc(url, personsCopyTable, connectionProperties);
    }

    public static void main(String[] args) throws Exception {
        SparkSession session = init();
        sparkJson(session);
    }

}
