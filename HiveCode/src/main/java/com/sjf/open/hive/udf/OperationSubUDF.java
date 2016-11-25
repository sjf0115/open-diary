package com.sjf.open.hive.udf;

import com.google.common.base.Objects;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.DoubleObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.List;

/**
 * Created by xiaosi on 16-11-20.
 */
public class OperationSubUDF extends GenericUDF {

    private DoubleObjectInspector numOneObjectInspector;
    private DoubleObjectInspector numTwoObjectInspector;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        // 参数个数检查
        if (arguments.length < 2) {
            throw new UDFArgumentLengthException("sub(numOne, numTwo) requires two arguments, got " + arguments.length);
        }

        numOneObjectInspector = (DoubleObjectInspector) arguments[0];
        numTwoObjectInspector = (DoubleObjectInspector) arguments[1];

        // 类型判断 Java原生类型
        if (!numOneObjectInspector.getCategory().equals(ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException("sub(numOne, numTwo) wrong input argument type");
        }
        if (!numTwoObjectInspector.getCategory().equals(ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException("sub(numOne, numTwo) wrong input argument type");
        }

        return PrimitiveObjectInspectorFactory.javaDoubleObjectInspector;
    }

    @Override
    public Double evaluate(DeferredObject[] arguments) throws HiveException {

        if(arguments.length < 2){
            return null;
        }

        if(Objects.equal(arguments[0], null) || Objects.equal(arguments[1], null)){
            return null;
        }

        Double numOne = (Double) this.numOneObjectInspector.getPrimitiveJavaObject(arguments[0].get());
        Double numTwo = (Double) this.numTwoObjectInspector.getPrimitiveJavaObject(arguments[1].get());

        double result = 0;

        if (Objects.equal(numOne, null) || Objects.equal(numTwo, null)) {
            return result;
        }
        result = numOne - numTwo;
        return result;
    }

    @Override
    public String getDisplayString(String[] strings) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("if ");
        stringBuilder.append(strings[0]);
        stringBuilder.append(" and ");
        stringBuilder.append(strings[1]);
        stringBuilder.append(" are not null");
        stringBuilder.append(" return ");
        stringBuilder.append(" \"");
        stringBuilder.append(strings[0]);
        stringBuilder.append(" \" + \"");
        stringBuilder.append(strings[1]);
        stringBuilder.append("\"");
        return stringBuilder.toString();

    }
}
