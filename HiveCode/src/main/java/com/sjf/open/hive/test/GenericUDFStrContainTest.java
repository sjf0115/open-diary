package com.sjf.open.hive.test;


import com.sjf.open.hive.udf.GenericUDFStrContain;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.BooleanObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaosi on 16-11-22.
 */
public class GenericUDFStrContainTest {

    public static void test() throws HiveException {

        GenericUDFStrContain genericUDFStrContain = new GenericUDFStrContain();
        ObjectInspector stringOI = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
        ObjectInspector listOI = ObjectInspectorFactory.getStandardListObjectInspector(stringOI);
        BooleanObjectInspector resultInspector = (BooleanObjectInspector) genericUDFStrContain.initialize(new ObjectInspector[]{listOI, stringOI});

        // create the actual UDF arguments
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");

        // test our results

        // the value exists
        Object result = genericUDFStrContain.evaluate(new GenericUDF.DeferredObject[]{new GenericUDF.DeferredJavaObject(list), new GenericUDF.DeferredJavaObject("a")});
        System.out.println("-----------" + result);

        // the value doesn't exist
        Object result2 = genericUDFStrContain.evaluate(new GenericUDF.DeferredObject[]{new GenericUDF.DeferredJavaObject(list), new GenericUDF.DeferredJavaObject("d")});
        System.out.println("-----------" + result2);

        // arguments are null
        Object result3 = genericUDFStrContain.evaluate(new GenericUDF.DeferredObject[]{new GenericUDF.DeferredJavaObject(null), new GenericUDF.DeferredJavaObject(null)});
        System.out.println("-----------" + result3);
    }

    public static void main(String[] args) throws HiveException {
        test();
    }
}