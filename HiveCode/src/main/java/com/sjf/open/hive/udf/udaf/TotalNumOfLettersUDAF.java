package com.sjf.open.hive.udf.udaf;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

/**
 * Created by xiaosi on 16-11-24.
 */
public class TotalNumOfLettersUDAF extends AbstractGenericUDAFResolver {

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] info) throws SemanticException {
        return super.getEvaluator(info);
    }

    public static class TotalNumOfLettersUDAFEvaluator extends GenericUDAFEvaluator {
        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            if(parameters.length != 1){
            }
            return super.init(m, parameters);
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return null;
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {

        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {

        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return null;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {

        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            return null;
        }
    }
}
