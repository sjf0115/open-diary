package com.sjf.open.utils;

import com.google.common.collect.Maps;
import org.apache.avro.Schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by xiaosi on 16-9-18.
 */
public class AvroUtil {

    private static Map<String, Schema> schemas = Maps.newHashMap();

    private AvroUtil() {
    }

    public static void addSchema(String name, Schema schema) {

        schemas.put(name, schema);

    }

    public static Schema getSchema(String name) {

        return schemas.get(name);

    }

    /**
     * 组合模式
     * @param sc
     * @return
     */
    public static String resolveSchema(String sc) {

        String result = sc;
        for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
            result = replace(result, entry.getKey(), entry.getValue().toString());
        }
        return result;

    }

    /**
     * 对象替换
     * 例如：com.sjf.open.model.User 替换为 User定义模式
     * @param str
     * @param pattern
     * @param replace
     * @return
     */
    static String replace(String str, String pattern, String replace) {

        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();

    }

    /**
     * 从字符串中解析生成模式
     * @param schemaString
     * @return
     */
    public static Schema parseSchema(String schemaString) {

        String completeSchema = resolveSchema(schemaString);
        Schema schema = Schema.parse(completeSchema);
        String name = schema.getFullName();
        schemas.put(name, schema);
        return schema;

    }

    /**
     * 从流中解析生成模式
     * @param in
     * @return
     * @throws IOException
     */
    public static Schema parseSchema(InputStream in) throws IOException {

        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return parseSchema(out.toString());

    }

    /**
     * 从文件中解析生成模式
     * @param file
     * @return
     * @throws IOException
     */
    public static Schema parseSchema(File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        return parseSchema(fis);

    }
}