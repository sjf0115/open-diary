package mvel;

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
        System.out.println(object);
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

    public static void main(String[] args) {
        foreachTest();
    }

}
