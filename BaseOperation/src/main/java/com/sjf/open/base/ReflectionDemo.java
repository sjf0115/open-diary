package com.sjf.open.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

/**
 * Created by xiaosi on 16-12-16.
 */

public class ReflectionDemo {


    @Test
    public void test1(){
        Method[] methodArray = Student.class.getMethods();
        for(Method method : methodArray){
            System.out.println("method name ->" + method.getName());
        }
    }

    @Test
    public void test2(){
        String className = "com.sjf.open.base.Address2";
        try {
            Class classObject = Class.forName(className);
            String name = classObject.getName();
            System.out.println("class name ->" + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        Class studentClass = Student.class;
        System.out.println(studentClass.getSimpleName()); // Student
        System.out.println(studentClass.getName()); // com.sjf.open.base.Student
    }

    @Test
    public void test4(){

        int publicNum = Modifier.PUBLIC; // 1
        int privateNum = Modifier.PRIVATE; // 2
        int protectedNum = Modifier.PROTECTED; // 4
        int staticNum = Modifier.STATIC; // 8
        int finalNum = Modifier.FINAL; // 16
        int synchronizedNum = Modifier.SYNCHRONIZED; // 32
        int volatileNum = Modifier.VOLATILE; // 64
        int transientNum = Modifier.TRANSIENT; // 128
        int nativeNum = Modifier.NATIVE; // 256
        int interfaceNum = Modifier.INTERFACE; // 512
        int abstractNum = Modifier.ABSTRACT; // 1024
        int strictNum = Modifier.STRICT; // 2048

    }

    @Test
    public void test5(){
        Class studentClass = Student.class;
        Annotation[] annotationArray = studentClass.getAnnotations();
        Field[] fieldArray = studentClass.getFields();
        Method[] methodArray = studentClass.getMethods();
        Constructor[] constructorArray = studentClass.getConstructors();
        Class[] interfaceArray = studentClass.getInterfaces();
        Class superStudent = studentClass.getSuperclass();
        System.out.println(studentClass.getPackage()); // package com.sjf.open.base
    }

    @Test
    public void test6(){
        Class addressClass = Address.class;
        try {
            Constructor constructor = addressClass.getConstructor(new Class[]{String.class, String.class});
            Address address = (Address) constructor.newInstance("山东","青岛");
            System.out.println(address);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Constructor constructor = addressClass.getConstructor(new Class[]{String.class});
            Class[] parameterTypes = constructor.getParameterTypes();
            Parameter[] parameterArray = constructor.getParameters();
            for(Parameter parameter : parameterArray){
                System.out.println( "name -> " + parameter.getName() + " | type -> " + parameter.getType() + " | modifiers -> " + parameter.getModifiers());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Constructor[] constructorArray = addressClass.getConstructors();
    }

}
