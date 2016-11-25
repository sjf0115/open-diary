package com.sjf.open.hive.udf;

import com.google.common.base.Objects;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.BooleanObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.io.BooleanWritable;

/**
 * Created by xiaosi on 16-11-21.
 *
 * 实现 逻辑与 and
 *
 */
@Description(name = "and", value = "a _FUNC_ b - Logical and")
public class GenericUDFOPAnd extends GenericUDF {

    private final BooleanWritable result = new BooleanWritable();
    private BooleanObjectInspector boi0, boi1;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        // 参数个数校验
        if (arguments.length != 2) {
            throw new UDFArgumentLengthException(
                    "The operator 'AND' only accepts 2 argument , but got " + arguments.length);
        }

        boi0 = (BooleanObjectInspector) arguments[0];
        boi1 = (BooleanObjectInspector) arguments[1];

        // 返回值类型
        return PrimitiveObjectInspectorFactory.writableBooleanObjectInspector;

    }

    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        boolean booleanOne = false;
        boolean booleanTwo = false;

        // 第一个参数为false 直接返回false
        Object argumentOne = arguments[0].get();
        if (!Objects.equal(argumentOne, null)) {
            booleanOne = boi0.get(argumentOne);
            if (booleanOne == false) {
                result.set(false);
                return result;
            }
        }

        // 第二个参数为false 直接返回false
        Object argumentTwo = arguments[1].get();
        if (!Objects.equal(argumentTwo, null)) {
            booleanTwo = boi1.get(argumentTwo);
            if (booleanTwo == false) {
                result.set(false);
                return result;
            }
        }

        // 均为true 返回true
        if ((!Objects.equal(argumentOne, null) && Objects.equal(booleanOne, true))
                && (!Objects.equal(argumentTwo, null) && Objects.equal(booleanTwo, true))) {
            result.set(true);
            return result;
        }

        return null;
    }

    @Override
    public String getDisplayString(String[] children) {
        return "(" + children[0] + " and " + children[1] + ")";
    }

}
