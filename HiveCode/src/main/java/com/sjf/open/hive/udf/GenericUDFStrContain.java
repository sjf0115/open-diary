package com.sjf.open.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.lazy.LazyString;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.BooleanObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.BooleanWritable;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xiaosi on 16-11-21.
 *
 */
@Description(name = "contain", value = "_FUNC_(List<T>, T) ")
public class GenericUDFStrContain extends GenericUDF {

    private static final Logger logger = LoggerFactory.getLogger(GenericUDFStrContain.class);

    private ListObjectInspector listObjectInspector;
    private StringObjectInspector stringObjectInspector;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        logger.info("--------- GenericUDFStrContain --- initialize");

        // 参数个数校验
        if (arguments.length != 2) {
            throw new UDFArgumentLengthException(
                    "The function 'Contain' only accepts 2 argument : List<T> and T , but got " + arguments.length);
        }

        ObjectInspector argumentOne = arguments[0];
        ObjectInspector argumentTwo = arguments[1];

        // 参数类型校验
        if (!(argumentOne instanceof ListObjectInspector)) {
            throw new UDFArgumentException("The first argument of function must be a list / array");
        }
        if (!(argumentTwo instanceof StringObjectInspector)) {
            throw new UDFArgumentException("The second argument of function must be a string");
        }

        this.listObjectInspector = (ListObjectInspector) argumentOne;
        this.stringObjectInspector = (StringObjectInspector) argumentTwo;

        // 链表元素类型检查
        if (!(listObjectInspector.getListElementObjectInspector() instanceof StringObjectInspector)) {
            throw new UDFArgumentException("The first argument must be a list of strings");
        }

        // 返回值类型
        return PrimitiveObjectInspectorFactory.javaBooleanObjectInspector;

    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        logger.info("--------- GenericUDFStrContain --- evaluate");

        // 利用ObjectInspector从DeferredObject[]中获取元素值
        List<LazyString> list = (List<LazyString>) this.listObjectInspector.getList(arguments[0].get());
        String str = this.stringObjectInspector.getPrimitiveJavaObject(arguments[1].get());

        if (Objects.equal(list, null) || Objects.equal(str, null)) {
            return null;
        }

        // 判断是否包含查询元素
        for (LazyString lazyString : list) {
            String s = lazyString.toString();
            if (Objects.equal(str, s)) {
                return new Boolean(true);
            }
        }
        return new Boolean(false);

    }

    @Override
    public String getDisplayString(String[] children) {
        return "arrayContainsExample() strContain(List<T>, T)";
    }

}
