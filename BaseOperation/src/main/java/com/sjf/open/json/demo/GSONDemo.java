package com.sjf.open.json.demo;

import avro.shaded.com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.sjf.open.json.adapter.DateTimeAdapter;
import com.sjf.open.json.model.Author;
import com.sjf.open.json.model.ClickDetail;
import com.sjf.open.json.model.Company;
import com.sjf.open.json.model.Course;
import com.sjf.open.json.model.Fruit;
import com.sjf.open.json.model.Person;
import com.sjf.open.json.model.Result;
import com.sjf.open.json.model.School;
import com.sjf.open.json.model.Student;
import com.sjf.open.json.strategy.UserExcludedStrategy;
import com.sjf.open.model.Book;
import com.sjf.open.model.Teacher;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaosi on 16-7-13.
 *
 * Gson 使用指南
 *
 */
public class GSONDemo {

    private static Logger logger = LoggerFactory.getLogger(GSONDemo.class);

    /**
     * 基本类型 序列化与反序列化
     */
    private static void PrimitivesExample() {

        // 序列化
        Gson gson = new Gson();
        String intJson = gson.toJson(1);
        String strJson = gson.toJson("abcd");
        String longJson = gson.toJson(new Long(10));
        String[] values = { "abcd" };
        String arrayJson = gson.toJson(values);

        System.out.println(intJson); // 1
        System.out.println(strJson); // "abcd"
        System.out.println(longJson); // 10
        System.out.println(arrayJson); // ["abcd"]

        // 反序列化
        int oneInt = gson.fromJson("1", int.class);
        Integer oneInteger = gson.fromJson("1", Integer.class);
        Long oneLong = gson.fromJson("1", Long.class);
        Boolean falseValue = gson.fromJson("false", Boolean.class);
        String str = gson.fromJson(strJson, String.class);
        String[] array = gson.fromJson(arrayJson, String[].class);

        System.out.println(oneInt); // 1
        System.out.println(oneInteger); // 1
        System.out.println(oneLong); // 1
        System.out.println(falseValue); // false
        System.out.println(str); // abcd
        for (String s : array) {
            System.out.println("array --- " + s); // abcd
        }
    }

    /**
     * 简单对象 序列化与反序列化
     */
    private static void SimpleObjectExample() {

        Author author = new Author();
        author.setAge(21);
        author.setName("yoona");
        author.setSex("男");
        author.setFavorite(null);

        Gson gson = new Gson();

        // 序列化
        String json = gson.toJson(author);
        System.out.println(json); // {"name":"yoona","age":21}

        // 反序列化
        Author a = gson.fromJson(json, Author.class);
        System.out.println(a); // Author{name='yoona', age=21, sex='null', favorite='null'}

    }

    /**
     * 复杂对象 序列化与反序列
     */
    private static void ComplexObjectExample() {

        Book book = new Book();
        book.setName("简洁之道");
        book.setPrice(45.32);

        Author author = new Author();
        author.setName("yoona");
        author.setAge(21);

        book.setAuthor(author);

        // 序列化
        Gson gson = new Gson();
        String json = gson.toJson(book);
        // {"name":"简洁之道","price":45.32,"author":{"name":"yoona","age":21}}
        System.out.println(json);

        // 反序列化
        Book b = gson.fromJson(json, Book.class);
        // Book{name='简洁之道', price=45.32, author=Author{name='yoona', age=21, sex='null', favorite='null'}}
        System.out.println(b);

    }

    /**
     * 嵌套内部类
     */
    private static void NestedClassExample() {

        Gson gson = new Gson();

        Person person = new Person();
        person.setName("yoona");
        person.setAge(25);
        Person.Address address = person.new Address();
        address.setProvince("山东");
        address.setCity("青岛");
        person.setAddress(address);

        // 序列化
        String json = gson.toJson(person);
        System.out.println(json);

        // 反序列化
        Person p = gson.fromJson(json, Person.class);
        System.out.println(p);

    }

    /**
     * 数组
     */
    private static void ArrayExample() {

        Gson gson = new Gson();
        int[] ints = { 1, 2, 3, 4, 5 };
        String[] strings = { "abc", "def", "ghi" };

        // 序列化
        String intJson = gson.toJson(ints);
        String strJson = gson.toJson(strings);

        System.out.println(intJson); // [1,2,3,4,5]
        System.out.println(strJson); // ["abc","def","ghi"]

        // 反序列化
        int[] ints2 = gson.fromJson(intJson, int[].class);
        for (int num : ints2) {
            System.out.print(num + " "); // 1 2 3 4 5
        }
        System.out.println();

        String[] strings2 = gson.fromJson(strJson, String[].class);
        for (String str : strings2) {
            System.out.print(str + " "); // abc def ghi
        }
        System.out.println();

    }

    /**
     * 泛型
     */
    private static void GenericTypeExample() {

        Gson gson = new Gson();

        Result<ClickDetail> result = new Result<ClickDetail>();
        ClickDetail clickDetail = new ClickDetail();
        clickDetail.setBusiness("hotel");
        clickDetail.setPrice(234121.5);
        result.setValue(clickDetail);

        // 序列化
        String json = gson.toJson(result);
        System.out.println(json); // {"value":{"business":"hotel","price":234121.5}

        // 反序列化
        Result<ClickDetail> r = gson.fromJson(json, Result.class);
        // java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to
        // com.sjf.open.json.model.ClickDetail
        // System.out.println(r.getValue().getBusiness());
        System.out.println(r); // Result{value={business=hotel, price=234121.5}}

        // 序列化
        Type resultType = new TypeToken<Result<ClickDetail>>() {
        }.getType();
        json = gson.toJson(result, resultType);
        System.out.println(json); // {"value":{"business":"hotel","price":234121.5}}

        // 反序列化
        Result<ClickDetail> re = gson.fromJson(json, resultType);
        System.out.println(re); // Result{value=ClickDetail{business='hotel', price=234121.5}}
        System.out.println(re.getValue().getBusiness()); // hotel

    }

    /**
     * 集合
     */
    private static void CollectionsExample() {
        List<String> list = Lists.newArrayList("apple", "banana", "orange");
        Map<String, Integer> map = Maps.newHashMap();
        map.put("banana", 1);
        map.put("apple", 2);

        Gson gson = new Gson();

        // 序列化
        String listJson = gson.toJson(list);
        String mapJson = gson.toJson(map);

        System.out.println(listJson); // ["apple","banana","orange"]
        System.out.println(mapJson); // {"banana":1,"apple":2}

        // 反序列化
        map = gson.fromJson(mapJson, Map.class);
        System.out.println(map);

        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list2 = gson.fromJson(listJson, listType);
        System.out.println(list2); // [apple, banana, orange]

        Type mapType = new TypeToken<Map<String, Integer>>() {
        }.getType();
        Map<String, Integer> map2 = gson.fromJson(mapJson, mapType);
        System.out.println(map2); // {banana=1, apple=2}

    }

    /**
     * 任意类型集合
     */
    private static void CollectionArbitraryTypesExample() {

        List<Object> objectList = Lists.newArrayList();
        objectList.add(2);
        objectList.add("banana");

        Gson gson = new Gson();

        // 序列化
        String objectJson = gson.toJson(objectList);
        System.out.println(objectJson); //[2,"banana"]

        // 反序列化

        // 失败
        Type objectListType = new TypeToken<List<Object>>() {
        }.getType();
        List<Object> objectList2 = gson.fromJson(objectJson, objectListType);
        System.out.println(objectList2);

        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(objectJson).getAsJsonArray();
        int number = gson.fromJson(array.get(0), int.class);
        String message = gson.fromJson(array.get(1), String.class);
        System.out.println(number + "----" + message);

    }

    /**
     * 自定义序列化器和反序列化器
     */
    private static void CustomExample() {
        Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdapter()).create();
        DateTime dateTime = new DateTime();
        String json = gson.toJson(dateTime);
        System.out.println(json);

        DateTime d = gson.fromJson(json, DateTime.class);
        System.out.println(d.toString("yyyy-MM-dd"));
    }

    /**
     * NULL处理
     */
    private static void SerializeNullsExample() {

        Author author = new Author(null, 21);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(author, Author.class);
        System.out.println(json);

        json = gson.toJson(null);
        System.out.println(json);

    }

    /**
     * 版本控制
     */
    private static void VersioningExample() {

        Gson gson = new GsonBuilder().setVersion(1.0).create();

        Student student = new Student();
        student.setName("刘二");
        student.setNickName("yoona");
        student.setAge(25);

        String json = gson.toJson(student);
        System.out.println(json);

        Gson gson1 = new Gson();
        json = gson1.toJson(student);
        System.out.println(json);

    }

    /**
     * 保留特定字段
     */
    private static void ExcludingFieldsExample() {

        Gson gson = new Gson();

        Fruit fruit = new Fruit();
        fruit.setName("apple");
        fruit.setPrice(4.12);
        fruit.setLocation("山东");

        String json = gson.toJson(fruit, Fruit.class);
        System.out.println(json);

        Gson gson1 = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();
        json = gson1.toJson(fruit, Fruit.class);
        System.out.println(json);
    }

    /**
     * 排除字段
     */
    private static void ExposeExample() {
        Company company = new Company();
        company.setName("去呼呼");
        company.setLocation("北京");
        company.setCount(2000);

        // 序列化
        // 排除未使用@Expose注解的字段
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(company);
        System.out.println(json); // {"name":"去呼呼","count":2000}

        // 反序列化
        Company c = gson.fromJson(json, Company.class);
        System.out.println(c); // Company{name='去呼呼', location='null', count=2000}
    }

    /**
     * 自定义排除策略
     */
    private static void ExclusionStrategiesExample() {

        School school = new School();
        school.setName("加利福尼亚");
        school.setCount(10000);
        school.setLocation("美国");

        // 排除属性
        Gson gson = new GsonBuilder().setExclusionStrategies(new UserExcludedStrategy()).serializeNulls().create();

        // 序列化
        String json = gson.toJson(school);
        System.out.println(json);

        // 反序列化
        School s = gson.fromJson(json, School.class);
        System.out.println(s);

        // 排除String类型
        gson = new GsonBuilder().setExclusionStrategies(new UserExcludedStrategy(String.class)).serializeNulls()
                .create();

        // 序列化
        json = gson.toJson(school);
        System.out.println(json);

        // 反序列化
        s = gson.fromJson(json, School.class);
        System.out.println(s);
    }

    /**
     * 属性重命名
     */
    private static void FieldNamingPolicyExample() {

        Course course = new Course();
        course.setCount(35);
        course.setName("Java 选修");
        course.setTeacher("James Gosling");

        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

        // 序列化
        String json = gson.toJson(course);
        System.out.println(json); // {"course_name":"Java 选修","courser_teacher_name":"James Gosling","Count":35}

        // 反序列化
        Course c = gson.fromJson(json, Course.class);
        System.out.println(c); // Course{name='Java 选修', teacher='James Gosling', count=35}

    }

    private static void test11() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation() // 不导出实体中没有用@Expose注解的属性
                .enableComplexMapKeySerialization() // 支持Map的key为复杂对象的形式
                .serializeNulls() // NULL处理
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")// 时间转化为特定格式
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)// 会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                .setPrettyPrinting() // 对json结果格式化.
                .setVersion(1.0) // 有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                .create();

        Teacher teacher = new Teacher();
        teacher.setAge(34);
        teacher.setName("yoona");
        teacher.setSex("girl");
        teacher.setBirthday(new Date());

        String json = gson.toJson(teacher);
        logger.info("-------------------------json from bean {}", json);
        // ---------------------------------------------------------------------
        Teacher teacher1 = gson.fromJson(json, Teacher.class);
        logger.info("--------------------- bean from json {}", teacher1.toString());
    }

    private static void test2() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        List<Teacher> teacherList = Lists.newArrayList();
        Teacher teacher1 = new Teacher();
        teacher1.setSex("boy");
        teacher1.setAge(23);
        teacher1.setName("guava");
        teacher1.setBirthday(new Date());
        teacherList.add(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setSex("girl");
        teacher2.setAge(25);
        teacher2.setName("java");
        teacher2.setBirthday(new Date());
        teacherList.add(teacher2);

        String json = gson.toJson(teacherList);
        logger.info("--------------------------- teacher to json {}", json);

        Type type = new TypeToken<List<Teacher>>() {
        }.getType();

        List<Teacher> teacherList1 = gson.fromJson(json, type);
        for (Teacher teacher : teacherList1) {
            logger.info("---------------------- teacher from json {}", teacher.toString());
        }
    }

    public static void main(String[] args) {
        CollectionsExample();
    }
}
