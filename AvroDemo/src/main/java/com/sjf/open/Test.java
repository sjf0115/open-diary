package com.sjf.open;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
/**
 * Created by xiaosi on 16-9-14.
 */
public class Test {
    /**
     * 序列化
     */
    public static void serialize(){
        // 方式一
        User user1 = new User();
        user1.setName("yoona");
        user1.setAge(25);
        user1.setFavorite("football");
        // 方式二
        User user2 = new User("sjf", 24, "basketball");
        // 方式三
        User user3 = User.newBuilder().setName("Tom").setAge(26).setFavorite("football").build();
        // 序列化
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
        try {
            dataFileWriter.create(user1.getSchema(), new File("user.avro"));
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
            dataFileWriter.append(user3);
            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 反序列化
     */
    public static void deserialize(){
        DatumReader<User> reader = new SpecificDatumReader<User>(User.class);
        DataFileReader<User> fileReader = null;
        try {
            fileReader = new DataFileReader<User>(new File(
                    "user.avro"), reader);
            User user = null;
            while (fileReader.hasNext()) {
                // 复用user对象，避免重复分配内存和GC
                user = fileReader.next(user);
                System.out.println(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        serialize();
        deserialize();
    }
}