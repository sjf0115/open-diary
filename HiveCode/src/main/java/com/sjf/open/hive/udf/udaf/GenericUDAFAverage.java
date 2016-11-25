package com.sjf.open.hive.udf.udaf;

import com.sjf.open.hive.udf.GenericUDAFMin;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.DoubleObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.LongObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.io.LongWritable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by xiaosi on 16-11-22.
 *
 * UDF Average
 *
 */
public class GenericUDAFAverage extends AbstractGenericUDAFResolver {

    static final Log LOG = LogFactory.getLog(GenericUDAFMin.class.getName());

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {
        if (parameters.length != 1) {
            throw new UDFArgumentTypeException(parameters.length - 1, "Exactly one argument is expected.");
        }

        if (parameters[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentTypeException(0,
                    "Only primitive type arguments are accepted but " + parameters[0].getTypeName() + " is passed.");
        }
        switch (((PrimitiveTypeInfo) parameters[0]).getPrimitiveCategory()) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case STRING:
        case TIMESTAMP:
            return new GenericUDAFAverageEvaluator();
        case BOOLEAN:
        default:
            throw new UDFArgumentTypeException(0, "Only numeric or string type arguments are accepted but "
                    + parameters[0].getTypeName() + " is passed.");
        }
    }

    /**
     * GenericUDAFAverageEvaluator.
     *
     */
    public static class GenericUDAFAverageEvaluator extends GenericUDAFEvaluator {

        // For PARTIAL1 and COMPLETE
        PrimitiveObjectInspector inputOI;

        // For PARTIAL2 and FINAL
        StructObjectInspector soi;
        StructField countField;
        StructField sumField;
        LongObjectInspector countFieldOI;
        DoubleObjectInspector sumFieldOI;

        // For PARTIAL1 and PARTIAL2
        Object[] partialResult;

        // For FINAL and COMPLETE
        DoubleWritable result;

        boolean warned = false;

        @Override
        public ObjectInspector init(Mode mode, ObjectInspector[] parameters) throws HiveException {
            assert (parameters.length == 1);
            super.init(mode, parameters);

            // init input
            if (mode == Mode.PARTIAL1 || mode == Mode.COMPLETE) {
                inputOI = (PrimitiveObjectInspector) parameters[0];
            }
            else {
                soi = (StructObjectInspector) parameters[0];
                countField = soi.getStructFieldRef("count");
                sumField = soi.getStructFieldRef("sum");
                countFieldOI = (LongObjectInspector) countField.getFieldObjectInspector();
                sumFieldOI = (DoubleObjectInspector) sumField.getFieldObjectInspector();
            }

            // init output
            if (mode == Mode.PARTIAL1 || mode == Mode.PARTIAL2) {
                // The output of a partial aggregation is a struct containing
                // a "long" count and a "double" sum.

                ArrayList<ObjectInspector> foi = new ArrayList<ObjectInspector>();
                foi.add(PrimitiveObjectInspectorFactory.writableLongObjectInspector);
                foi.add(PrimitiveObjectInspectorFactory.writableDoubleObjectInspector);
                ArrayList<String> fname = new ArrayList<String>();
                fname.add("count");
                fname.add("sum");
                partialResult = new Object[2];
                partialResult[0] = new LongWritable(0);
                partialResult[1] = new DoubleWritable(0);
                return ObjectInspectorFactory.getStandardStructObjectInspector(fname, foi);

            } else {
                result = new DoubleWritable(0);
                return PrimitiveObjectInspectorFactory.writableDoubleObjectInspector;
            }
        }

        /**
         * 存储中间计算结果 总和 个数
         */
        static class AverageAgg implements AggregationBuffer {
            long count;
            double sum;
        }

        /**
         * 获取一个新的AggregationBuffer 存储中间计算结果
         * @return
         * @throws HiveException
         */
        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            AverageAgg result = new AverageAgg();
            reset(result);
            return result;
        }

        /**
         * 重置中间计算结果
         * @param agg
         * @throws HiveException
         */
        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            AverageAgg averageAgg = (AverageAgg) agg;
            averageAgg.count = 0;
            averageAgg.sum = 0;
        }

        /**
         * 将一行新的数据载入到聚合buffer中
         * @param agg
         * @param parameters
         * @throws HiveException
         */
        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {
            assert (parameters.length == 1);
            Object p = parameters[0];
            if (p != null) {
                AverageAgg averageAgg = (AverageAgg) agg;
                try {
                    double v = PrimitiveObjectInspectorUtils.getDouble(p, inputOI);
                    averageAgg.count++;
                    averageAgg.sum += v;
                } catch (NumberFormatException e) {
                    if (!warned) {
                        warned = true;
                         LOG.warn(getClass().getSimpleName() + " " + StringUtils.stringifyException(e));
                         LOG.warn(getClass().getSimpleName() + " ignoring similar exceptions.");
                    }
                }
            }
        }

        /**
         *
         * @param agg
         * @return
         * @throws HiveException
         */
        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            AverageAgg averageAgg = (AverageAgg) agg;
            ((LongWritable) partialResult[0]).set(averageAgg.count);
            ((DoubleWritable) partialResult[1]).set(averageAgg.sum);
            return partialResult;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {
            if (partial != null) {
                AverageAgg averageAgg = (AverageAgg) agg;
                Object partialCount = soi.getStructFieldData(partial, countField);
                Object partialSum = soi.getStructFieldData(partial, sumField);
                averageAgg.count += countFieldOI.get(partialCount);
                averageAgg.sum += sumFieldOI.get(partialSum);
            }
        }

        /**
         * 返回最终聚合结果给Hive
         * @param agg
         * @return
         * @throws HiveException
         */
        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {

            AverageAgg averageAgg = (AverageAgg) agg;
            if (averageAgg.count == 0) {
                return null;
            }
            else {
                result.set(averageAgg.sum / averageAgg.count);
                return result;
            }

        }
    }
}
