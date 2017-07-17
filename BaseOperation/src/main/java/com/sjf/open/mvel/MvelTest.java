package com.sjf.open.mvel;

import com.google.common.collect.Maps;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by xiaosi on 17-7-11.
 */
public class MvelTest {

    /**
     * 解析模式
     * @param expression
     * @param paramMap
     * @return
     */
    public static Object eval(String expression, Map<String, Object> paramMap) {
        Object object = MVEL.eval(expression, paramMap);
        return object;
    }

    /**
     * 编译模式
     * @param expression
     * @param paramMap
     * @return
     */
    public static Object compile(String expression, Map<String, Object> paramMap) {
        Serializable compiled = MVEL.compileExpression(expression);
        Object result = MVEL.executeExpression(compiled, paramMap);
        return result;
    }

    /**
     * 三元表达式
     */
    public static void ternaryTest(){
        String expression = "num > 0  ? \"Yes\" : \"No\";";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("num", new Integer(1));
        Object object = eval(expression, paramMap);
        System.out.println(object); // Yes
    }

    /**
     * if-then-else
     */
    public static void ifTest(){
        String expression = "if (num > 0) {\n" +
                "   return \"Greater than zero!\";\n" +
                "}\n" +
                "else if (num == -1) { \n" +
                "   return \"Minus one!\";\n" +
                "}\n" +
                "else { \n" +
                "   return \"Something else!\";\n" +
                "}";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("num", new Integer(1));
        Object object = eval(expression, paramMap);
        System.out.println(object);
    }

    /**
     * foreach
     */
    public static void foreachTest(){
        String expression = "foreach (el : str) {\n" +
                "   System.out.print(\"[\"+ el + \"]\"); \n" +
                "}";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("str", "abcd");
        Object object = eval(expression, paramMap);
        System.out.println(object);
    }

    /**
     * Empty
     */
    public static void emptyTest(){
        String expression = "a == empty && b == empty";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("a", "");
        paramMap.put("b", null);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // true
    }

    /**
     * Null
     */
    public static void nullTest(){
        String expression = "a == null && b == nil";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("a", null);
        paramMap.put("b", null);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // true
    }

    /**
     * 强制类型转换
     */
    public static void coercionTest(){
        String expression = "a == b";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("a", "123");
        paramMap.put("b", 123);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // true
    }

    /**
     * bean 属性
     */
    public static void beanTest(){

        Fruit fruit = new Fruit();
        fruit.setName("苹果");

        //String expression = "fruit.getName()";
        String expression = "fruit.name";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("fruit", fruit);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // 苹果
    }

    /**
     * Null-Safe Bean
     */
    public static void nullSafeBeanTest(){

        Fruit fruit = new Fruit();

        String expression = "fruit.getName()";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("fruit", fruit);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // 苹果
    }

    /**
     * If-Then-Else
     */
    public static void ifElseTest(){
        String expression = "if (param > 0) {return \"Greater than zero!\"; } else if (param == -1) { return \"Minus one!\"; } else { return \"Something else!\"; }";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("param", 2);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // Greater than zero!
    }

    /**
     * 简单函数
     */
    public static void simpleFunctionTest(){
        String expression = "def hello() { return \"Hello!\"; } hello();";
        Map<String, Object> paramMap = Maps.newHashMap();
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // Hello!
    }

    /**
     * 接受参数
     */
    public static void paramFunctionTest(){
        String expression = "def addTwo(num1, num2) { num1 + num2; } val = addTwo(a, b);";
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("a", 2);
        paramMap.put("b", 4);
        Object object = MVEL.eval(expression, paramMap);
        System.out.println(object); // 6
    }

    public static void main(String[] args) {
        paramFunctionTest();
    }

}