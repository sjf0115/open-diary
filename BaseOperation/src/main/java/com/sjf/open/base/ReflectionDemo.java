package com.sjf.open.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import com.google.common.base.Objects;
import org.apache.commons.math3.analysis.function.Add;
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

    @Test
    public void test7(){
        Class addressClass = Address.class;

        try {
            Field streetField = addressClass.getField("street");
            Address address = new Address();
            streetField.set(address, "新中关大街");
            System.out.println(streetField.get(address)); // 新中关大街

            System.out.println(streetField.getType()); // class java.lang.String
            System.out.println(streetField.getName());

            Field[] fieldArray = addressClass.getFields();
            for(Field field : fieldArray){
                System.out.println(field.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test8(){
        Class addressClass = Address.class;

        try {
            Address address = new Address();
            Method setProvinceMethod = addressClass.getMethod("setProvince", new Class[] {String.class});
            setProvinceMethod.invoke(address, "山东");
            Method getProvinceMethod = addressClass.getMethod("getProvince", null);
            Object value = getProvinceMethod.invoke(address, null);
            System.out.println(value); // 山东

            Class returnType = setProvinceMethod.getReturnType();
            Class[] parameterTypes = setProvinceMethod.getParameterTypes();
            System.out.println(setProvinceMethod.getName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Method[] methodArray = addressClass.getMethods();
        for(Method method : methodArray){
            System.out.println(method.getName());
        }
    }

    @Test
    public void test9(){
        Class addressClass = Address.class;
        Method[] methods = addressClass.getMethods();

        for(Method method : methods){
            System.out.println("method name : " + method.getName());
            if(isGetter(method)) {
                System.out.println("getter: " + method);
            }
            if(isSetter(method)) {
                System.out.println("setter: " + method);
            }
        }
    }

    public static boolean isGetter(Method method){

        if(!method.getName().startsWith("get")) {
            return false;
        }

        if(method.getParameterTypes().length != 0) {
            return false;
        }

        if(Objects.equal("void", method.getReturnType().getName())) {
            return false;
        }

        return true;

    }

    public static boolean isSetter(Method method){

        if(!method.getName().startsWith("set")) {
            return false;
        }

        if(method.getParameterTypes().length != 1) {
            return false;
        }

        if(!Objects.equal("void", method.getReturnType().getName())) {
            return false;
        }
        return true;

    }

    @Test
    public void test10() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Address address = new Address("山东", "青岛");
        Class addressClass = Address.class;
        Method getProvinceMethod = addressClass.getDeclaredMethod("getProvince", null);
        getProvinceMethod.setAccessible(true);
        String province = (String) getProvinceMethod.invoke(address, null);
        System.out.println(province); // 山东

        Field cityField = addressClass.getDeclaredField("city");
        cityField.setAccessible(true);
        String city = (String) cityField.get(address);
        System.out.println(city); // 青岛

    }
}
