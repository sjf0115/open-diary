package com.sjf.open.hive.udf;

import com.google.common.base.Objects;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

/**
 * Created by xiaosi on 16-11-20.
 */
public class NullDefaultUDF extends GenericUDF {

    private GenericUDFUtils.ReturnObjectInspectorResolver returnObjectInspectorResolver;
    private ObjectInspector valueObjectInspector;
    private ObjectInspector defaultObjectInspector;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        if (arguments.length < 2) {
            throw new UDFArgumentLengthException(
                    "nullDefault(value,default) requires two arguments, got " + arguments.length);
        }

        valueObjectInspector = arguments[0];
        defaultObjectInspector = arguments[1];

        if (Objects.equal(valueObjectInspector, null)) {
            throw new UDFArgumentTypeException(0,
                    "No \"value\" field in input structure " + arguments[0].getTypeName());
        }
        if (Objects.equal(defaultObjectInspector, null)) {
            throw new UDFArgumentTypeException(0,
                    "No \"default\" field in input structure " + arguments[1].getTypeName());
        }

        returnObjectInspectorResolver = new GenericUDFUtils.ReturnObjectInspectorResolver(true);

        if (!(returnObjectInspectorResolver.update(valueObjectInspector)
                && returnObjectInspectorResolver.update(defaultObjectInspector))) {
            throw new UDFArgumentTypeException(2, "The 1st and 2nd args of function null_default should have the same type," +
            "but they are different: \n \"" + valueObjectInspector.getTypeName() + "\" and \"" + defaultObjectInspector.getTypeName() + "\"");
        }

        return returnObjectInspectorResolver.get();
    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {

        Object object = returnObjectInspectorResolver.convertIfNecessary(arguments[0].get(), defaultObjectInspector);
        if(!Objects.equal(object, null)){
            return object;
        }
        object = returnObjectInspectorResolver.convertIfNecessary(arguments[1].get(), valueObjectInspector);
        return object;

    }

    @Override
    public String getDisplayString(String[] strings) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("if ");
        stringBuilder.append(strings[0]);
        stringBuilder.append(" is null ");
        stringBuilder.append("return ");
        stringBuilder.append(strings[1]);
        return stringBuilder.toString();

    }

    public static void main(String[] args) {

    }
}
