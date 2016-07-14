package com.sjf.open.json;

import com.google.common.collect.Lists;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sjf.open.model.Author;
import com.sjf.open.model.Book;
import com.sjf.open.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaosi on 16-7-13.
 */
public class GSONDemo {

    private static Logger logger = LoggerFactory.getLogger(GSONDemo.class);

    private static void beantoJson() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                .setPrettyPrinting() //对json结果格式化.
                .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
                .create();

        Teacher teacher = new Teacher();
        teacher.setAge(34);
        teacher.setName("yoona");
        teacher.setSex("girl");
        teacher.setBirthday(new Date());

        String json = gson.toJson(teacher);
        logger.info("-------------------------json from bean {}",json);
        //---------------------------------------------------------------------
        Teacher teacher1 = gson.fromJson(json, Teacher.class);
        logger.info("--------------------- bean from json {}",teacher1.toString());
    }


    private static void test2(){
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
        logger.info("--------------------------- teacher to json {}",json);

        Type type = new TypeToken<List<Teacher>>(){}.getType();

        List<Teacher> teacherList1 = gson.fromJson(json, type);
        for(Teacher teacher : teacherList1){
            logger.info("---------------------- teacher from json {}",teacher.toString());
        }
    }


    private static void test3(){
        Book book = new Book();
        book.setName("简洁之道");
        book.setPrice(45.32);
        //book.setAuthor(Lists.newArrayList("lily","jim"));

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(book);
        logger.info("--------- json from bean {}",json);

        Book book1 = gson.fromJson(json, Book.class);
        logger.info("-------- bean from json {}",book1.toString());
    }

    private static void test4(){
        Book book = new Book();
        book.setName("简洁之道");
        book.setPrice(45.32);

        Author author = new Author();
        author.setName("yoona");
        author.setAge(21);

        book.setAuthor(author);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(book);
        logger.info("--------- json from bean {}",json);

        Book book1 = gson.fromJson(json, Book.class);
        logger.info("-------- bean from json {}",book1.toString());
    }

    public static void main(String[] args) {
        test4();
    }
}
