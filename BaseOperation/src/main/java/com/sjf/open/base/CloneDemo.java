package com.sjf.open.base;


/**
 * Created by xiaosi on 16-12-7.
 */
public class CloneDemo {

    private static void cloneTest(){

        Student student = new Student();
        student.setName("yoona");
        student.setAge(25);

        Student s = student;
        s.setAge(26);
        s.setName("lucy");

        System.out.println("student ->" + student);
        System.out.println("s ->" + s);

    }

    /**
     * 浅拷贝
     * @throws CloneNotSupportedException
     */
    private static void shallowCloneTest() throws CloneNotSupportedException {

        Teacher teacher = new Teacher();
        teacher.setName("yoona");
        teacher.setAge(29);
        Address address = new Address();
        address.setProvince("山东");
        address.setCity("青岛");
        teacher.setAddress(address);

        Teacher t = teacher.clone();
        t.setAge(30);
        t.setName("lucy");
        address.setCity("济南");

        System.out.println("teacher ->" + teacher);
        System.out.println("t ->" + t);

    }

    public static void main(String[] args) throws CloneNotSupportedException {
        shallowCloneTest();
    }

}
