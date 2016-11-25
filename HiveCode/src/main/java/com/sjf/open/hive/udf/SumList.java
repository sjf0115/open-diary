package com.sjf.open.hive.udf;

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StandardListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;


public class SumList extends GenericUDF {

    private static final JsonParser JSONPARSER = new JsonParser();
    private ListObjectInspector listLOI;
    private StringObjectInspector columnNameSOI;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        if (arguments.length < 2) {
            throw new UDFArgumentException("SumList(List<String>,String columnName) Wrong input argument number!");
        }

        ObjectInspector list = arguments[0];
        ObjectInspector columnName = arguments[1];

        if (!arguments[0].getCategory().equals(ObjectInspector.Category.LIST)) {
            throw new UDFArgumentException("SumList(List<String>,String columnName) Wrong input argument type!");
        }

        if (!arguments[1].getCategory().equals(ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException("SumList(List<String>,String columnName) Wrong input argument type!");
        }

        this.listLOI = (ListObjectInspector) list;
        this.columnNameSOI = (StringObjectInspector) columnName;

        // return
        // ObjectInspectorFactory.getStandardListObjectInspector(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        return PrimitiveObjectInspectorFactory.javaStringObjectInspector;

    }

    @Override
    public Double evaluate(DeferredObject[] args) throws HiveException {

        List<String> list = (List<String>) this.listLOI.getList(args[0].get());
        String columnName = this.columnNameSOI.getPrimitiveJavaObject(args[1].get());

        double result = 0;

        if (Objects.equal(columnName, null) || Objects.equal(list, null) || list.isEmpty()) {
            return result;
        }

        for (String str : list) {
            String priceStr = parser(str, columnName);
            if (StringUtils.isBlank(priceStr)) {
                continue;
            }
            result += Double.parseDouble(priceStr);
        }

        return result;
    }

    /**
     * 解析JSON
     * 
     * @param json
     * @return
     */
    public static String parser(String json, String columnName) {

        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            JsonObject jsonObject = JSONPARSER.parse(json).getAsJsonObject();
            JsonElement insureProdPriceElement = jsonObject.get(columnName);
            if (Objects.equal(insureProdPriceElement, null)) {
                return null;
            }
            return insureProdPriceElement.getAsString();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public String getDisplayString(String[] strings) {
        return "SumList(List<String>,String columnName)";
    }

    public static void main(String[] args) throws HiveException {

        List<String> jsonList = Lists.newArrayList();
        jsonList.add(
                "{\"birthday\":\"19850414\",\"certNo\":\"4405Ct8iM3CGaei822\",\"certType\":0,\"encryption\":false,\"gender\":2,\"insureCount\":0,\"insureProdPrice\":1.00,\"name\":\"陈桂芝\",\"phone\":\"\",\"ticketType\":1}");
        jsonList.add(
                "{\"birthday\":\"19550714\",\"certNo\":\"4405CMgIn7=MIGV35X\",\"certType\":0,\"encryption\":false,\"gender\":1,\"insureCount\":0,\"insureProdPrice\":2.00,\"name\":\"陈伟平\",\"phone\":\"\",\"ticketType\":1}");
        jsonList.add(
                "{\"birthday\":\"19550714\",\"certNo\":\"4405CMgIn7=MIGV35X\",\"certType\":0,\"encryption\":false,\"gender\":1,\"insureCount\":0,\"insureProdPrice\":3.00,\"name\":\"陈伟平\",\"phone\":\"\",\"ticketType\":1}");
        jsonList.add(
                "{\"birthday\":\"19550714\",\"certNo\":\"4405CMgIn7=MIGV35X\",\"certType\":0,\"encryption\":false,\"gender\":1,\"insureCount\":0,\"insureProdPrice\":4.00,\"name\":\"陈伟平\",\"phone\":\"\",\"ticketType\":1}");

        String columnName = "insureProdPrice";

        SumList example = new SumList();
        example.initialize(new ObjectInspector[] {
                ObjectInspectorFactory
                        .getStandardListObjectInspector(PrimitiveObjectInspectorFactory.javaStringObjectInspector),
                PrimitiveObjectInspectorFactory.javaStringObjectInspector });

        double result = example.evaluate(
                new DeferredObject[] { new DeferredJavaObject(jsonList), new DeferredJavaObject(columnName) });
        System.out.println(result);

    }
}
