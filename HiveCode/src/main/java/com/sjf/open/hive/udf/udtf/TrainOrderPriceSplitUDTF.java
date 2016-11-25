package com.sjf.open.hive.udf.udtf;

import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Created by xiaosi on 16-11-24.
 */
public class TrainOrderPriceSplitUDTF extends GenericUDTF {


    private static final Logger logger = LoggerFactory.getLogger(TrainOrderPriceSplitUDTF.class);

    private StringObjectInspector stringObjectInspector;

    /**
     * The output struct represents a row of the table where the fields of the struct are the columns. The field names
     * are unimportant as they will be overridden by user supplied column aliases
     * 
     * @param arguments
     * @return
     * @throws UDFArgumentException
     */
    @Override
    public StructObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {

        // 参数个数检查
        if (arguments.length != 1) {
            throw new UDFArgumentException("train_order_price_split takes exactly one argument, but got " + arguments.length);
        }

        ObjectInspector argumentOne = arguments[0];

        // 参数类型检查
        if (!Objects.equal(argumentOne.getCategory(), ObjectInspector.Category.PRIMITIVE)) {
            throw new UDFArgumentException("The argument of function must be a string");
        }
        if (!Objects.equal(((StringObjectInspector) argumentOne).getPrimitiveCategory(),
                PrimitiveObjectInspector.PrimitiveCategory.STRING)) {
            throw new UDFArgumentException("The argument of function must be a string");
        }

        stringObjectInspector = (StringObjectInspector) argumentOne;

        // 列名 可以提供多列
        List<String> fieldNameList = Lists.newArrayList();
        fieldNameList.add("price");

        // 列类型
        List<ObjectInspector> fieldObjectInspectorList = Lists.newArrayList();
        fieldObjectInspectorList.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNameList, fieldObjectInspectorList);
    }

    /**
     * 数据处理
     * 
     * @param field
     * @return
     */
    public List<Object[]> processInputRecord(String field) {

        List<Object[]> result = Lists.newArrayList();
        if (StringUtils.isBlank(field)) {
            logger.info("------------------ processInputRecord null");
            return result;
        }

        logger.info("-------------------- json : {}", field);

        try{
            Gson gson = new GsonBuilder().create();
            TrainOrderPrice trainOrderPrice = gson.fromJson(field, TrainOrderPrice.class);

            logger.info("-------------------- ticket price {} , service_price {}, insurance_price {} ", trainOrderPrice.getTicketPrice(), trainOrderPrice.getServicePrice(), trainOrderPrice.getInsurancePrice());

            result.add(new Object[]{trainOrderPrice.getTicketPrice()});
            result.add(new Object[]{trainOrderPrice.getServicePrice()});
            result.add(new Object[]{trainOrderPrice.getInsurancePrice()});

            return result;
        }
        catch (Exception e){
            result.add(new Object[]{"NULL"});
            return result;
        }

    }

    @Override
    public void process(Object[] record) throws HiveException {

        String field = stringObjectInspector.getPrimitiveJavaObject(record[0]).toString();
        if(StringUtils.isBlank(field)){
            logger.info("------------------- process null");
        }

        // 处理行
        List<Object[]> results = processInputRecord(field);

        Iterator<Object[]> iterator = results.iterator();
        while (iterator.hasNext()) {
            Object[] r = iterator.next();
            forward(r);
        }

    }

    @Override
    public void close() throws HiveException {

    }
}
