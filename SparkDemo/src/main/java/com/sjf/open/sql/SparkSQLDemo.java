package com.sjf.open.sql;

import com.sjf.open.model.Employee;
import com.sjf.open.model.MyAverage;
import com.sjf.open.model.MyAverage2;
import com.sjf.open.model.Person;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.TypedColumn;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.spark.sql.functions.col;

/**
 * Created by xiaosi on 17-6-6.
 */
public class SparkSQLDemo {

    private static String path = "SparkDemo/src/main/resources/people.json";
    private static String textPath = "SparkDemo/src/main/resources/people.txt";
    private static String employeePath = "SparkDemo/src/main/resources/employees.json";

    /**
     * 初始化
     * 
     * @return
     */
    public static SparkSession init() {
        SparkSession session = SparkSession.builder().appName("Java Spark SQL basic example")
                .config("spark.master", "local").getOrCreate();
        return session;
    }

    /**
     * 通过Json文件创建DateFrames
     * 
     * @param session
     */
    public static void createDataFrameByJson(SparkSession session) {
        Dataset<Row> df = session.read().json(path);
        df.show();
    }

    /**
     * DataFrame基本操作
     * 
     * @param session
     */
    public static void dataFrameOperation(SparkSession session) {
        Dataset<Row> dataFrame = session.read().json(path);

        // 以树格式输出范式
        dataFrame.printSchema();
        // 只选择name列数据
        dataFrame.select("name").show();
        // 选择name age列 age加一
        dataFrame.select(col("name"), col("age").plus(1)).show();
        // 过滤age大于21岁
        dataFrame.filter(col("age").gt(21)).show();
        // 按age分组求人数
        dataFrame.groupBy("age").count().show();
    }

    /**
     * SQL查询
     * 
     * @param session
     */
    public static void sparkSQL(SparkSession session) {
        // 创建DataFrame
        Dataset<Row> dataFrame = session.read().json(path);
        // 将DataFrame注册为SQL临时视图
        dataFrame.createOrReplaceTempView("people");
        // 使用SQL查询数据
        Dataset<Row> sqlDataFrame = session.sql("SELECT name, age FROM people where age < 30");
        // 输出结果
        sqlDataFrame.show();
    }

    /**
     * 全局临时视图
     * 
     * @param session
     */
    public static void createGlobalTmpView(SparkSession session) throws AnalysisException {
        // 创建DataFrame
        Dataset<Row> dataFrame = session.read().json(path);
        // 将DataFrame注册为全局临时视图
        dataFrame.createGlobalTempView("people");
        // 全局临时视图与系统保留的数据库global_temp有关
        Dataset<Row> sqlDataFrame = session.sql("SELECT name, age FROM global_temp.people where age < 30");
        // 输出结果
        sqlDataFrame.show();

        // 全局临时视图是跨会话的
        Dataset<Row> sqlDataFrame2 = session.newSession()
                .sql("SELECT name, age FROM global_temp.people where age < 30");
        sqlDataFrame2.show();
    }

    /**
     * 创建DataSet
     */
    public static void createDataSet(SparkSession session) {
        // 创建Person对象
        Person person = new Person();
        person.setName("Andy");
        person.setAge(32);

        // 创建Encoders
        Encoder<Person> personEncoder = Encoders.bean(Person.class);
        Dataset<Person> dataSet = session.createDataset(Collections.singletonList(person), personEncoder);
        dataSet.show();

        // 大多数常见类型的编码器都在Encoders中提供
        Encoder<Integer> integerEncoder = Encoders.INT();
        Dataset<Integer> primitiveDS = session.createDataset(Arrays.asList(1, 2, 3), integerEncoder);
        Dataset<Integer> transformedDS = primitiveDS.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer call(Integer value) throws Exception {
                return value + 1;
            }
        }, integerEncoder);
        transformedDS.collect(); // Returns [2, 3, 4]

        // DataFrames可以通过提供的类来转换为DataSet 基于名称映射
        // 创建DataFrame
        Dataset<Row> dataFrame = session.read().json(path);
        // DataFrame转换为DataSet
        Dataset<Person> peopleDS = dataFrame.as(personEncoder);
        peopleDS.show();

    }

    /**
     * 使用反射方式与RDD进行互操作
     * 
     * @param session
     */
    public static void interOperateRDDByReflection(SparkSession session) {
        // 从文本文件创建一个Person对象的RDD
        JavaRDD<Person> peopleRDD = session.read().textFile(textPath).javaRDD().map(new Function<String, Person>() {
            @Override
            public Person call(String line) throws Exception {
                String[] parts = line.split(",");
                Person person = new Person();
                person.setName(parts[0]);
                if (parts.length > 1) {
                    person.setAge(Integer.parseInt(parts[1].trim()));
                }
                return person;
            }
        });

        // 将范式应用到JavaBeans的RDD上获取DataFrame
        Dataset<Row> peopleDataFrame = session.createDataFrame(peopleRDD, Person.class);
        // 将DataFrame注册为临时视图
        peopleDataFrame.createOrReplaceTempView("people");
        // 使用由spark提供的sql方法来运行SQL语句
        Dataset<Row> teenagersDataFrame = session.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19");

        // 可以通过字段索引访问结果行的列
        Encoder<String> stringEncoder = Encoders.STRING();
        Dataset<String> teenagerNamesByIndexDF = teenagersDataFrame.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Name: " + row.getString(0);
            }
        }, stringEncoder);
        teenagerNamesByIndexDF.show();

        // 可以通过字段名称访问结果行的列
        Dataset<String> teenagerNamesByFieldDF = teenagersDataFrame.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Name: " + row.<String> getAs("name");
            }
        }, stringEncoder);
        teenagerNamesByFieldDF.show();

    }

    /**
     * 使用接口方式与RDD进行互操作
     *
     * @param session
     */
    public static void interOperateRDDByInterface(SparkSession session) {
        // 创建RDD
        JavaRDD<String> peopleRDD = session.sparkContext().textFile(textPath, 1).toJavaRDD();
        // 在字符串中编码范式
        String schemaString = "name age";

        // 根据字符串定义的范式生成范式
        List<StructField> fields = new ArrayList<>();
        for (String fieldName : schemaString.split(" ")) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }
        StructType schema = DataTypes.createStructType(fields);

        // 将RDD(people)的记录转换为Rows
        JavaRDD<Row> rowRDD = peopleRDD.map(new Function<String, Row>() {
            @Override
            public Row call(String record) throws Exception {
                String[] attributes = record.split(",");
                return RowFactory.create(attributes[0], attributes[1].trim());
            }
        });

        // 将范式应用到RDD
        Dataset<Row> peopleDataFrame = session.createDataFrame(rowRDD, schema);
        // 将DataFrame注册为SQL临时视图
        peopleDataFrame.createOrReplaceTempView("people");

        // SQL可以在使用DataFrames创建的临时视图中运行
        Dataset<Row> results = session.sql("SELECT name FROM people");

        // SQL查询的结果是DataFrames，并支持所有正常的RDD操作
        // 结果中的行的列可以通过字段索引或字段名称访问
        Dataset<String> namesDS = results.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Name: " + row.getString(0);
            }
        }, Encoders.STRING());
        namesDS.show();
    }

    /**
     * Untyped User-Defined Aggregate Functions
     * @param session
     */
    public static void untypedAggregate(SparkSession session){

        // 注册自定义聚合函数
        session.udf().register("myAverage", new MyAverage());
        // 创建DataFrame
        Dataset<Row> df = session.read().json(employeePath);
        // 将DataFrame注册为SQL临时视图
        df.createOrReplaceTempView("employees");
        df.show();

        Dataset<Row> result = session.sql("SELECT myAverage(salary) as average_salary FROM employees");
        result.show();
    }

    /**
     * Type-Safe User-Defined Aggregate Functions
     * @param session
     */
    public static void typeSafeAggregate(SparkSession session){
        Encoder<Employee> employeeEncoder = Encoders.bean(Employee.class);
        Dataset<Employee> ds = session.read().json(employeePath).as(employeeEncoder);
        ds.show();

        MyAverage2 myAverage2 = new MyAverage2();
        // 将函数转换为TypedColumn 并赋予一个名称
        TypedColumn<Employee, Double> averageSalary = myAverage2.toColumn().name("average_salary");
        Dataset<Double> result = ds.select(averageSalary);
        result.show();
    }

    public static void main(String[] args) throws Exception {
        SparkSession session = init();
        typeSafeAggregate(session);
    }

}
