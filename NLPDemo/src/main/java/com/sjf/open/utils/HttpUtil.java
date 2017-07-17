package com.sjf.open.utils;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaosi on 17-3-6.
 *
 * Get Post请求
 *
 */
public class HttpUtil {

    /**
     * GET请求
     *
     * @param path 发送请求的URL
     * @param param 请求参数格式： name1=value1&name2=value2
     * @return 响应结果
     */
    public static String sendGet(String path, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            URL url = new URL(path + "?" + param);
            URLConnection connection = url.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 关闭输入流
        finally {
            try {
                if (!Objects.equal(in, null)) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * POST请求
     *
     * @param path 发送请求的 URL
     * @param param 请求参数格式：name1=value1&name2=value2
     * @return 响应结果
     */
    public static String sendPost(String path, String param) {

        if(StringUtils.isBlank(path) || StringUtils.isBlank(param)){
            return "";
        }

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }//while

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 关闭输出流、输入流
        finally {
            try {
                if (!Objects.equal(out, null)) {
                    out.close();
                }
                if (!Objects.equal(in, null)) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
