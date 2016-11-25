package com.sjf.open.hive.udf.udtf;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
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

import java.util.Iterator;
import java.util.List;

/**
 * Created by xiaosi on 16-11-24.
 */
public class NameParserUDTF extends GenericUDTF {


    private static final Logger logger = LoggerFactory.getLogger(NameParserUDTF.class);

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
            throw new UDFArgumentException("NameParserUDTF() takes exactly one argument, but got " + arguments.length);
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

        // output inspectors -- an object with two fields
        List<String> fieldNameList = Lists.newArrayList();
        List<ObjectInspector> fieldObjectInspectorList = Lists.newArrayList();

        fieldNameList.add("name");
        fieldNameList.add("surname");

        fieldObjectInspectorList.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldObjectInspectorList.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNameList, fieldObjectInspectorList);
    }

    /**
     * 行记录处理
     * 
     * @param name
     * @return
     */
    public List<Object[]> processInputRecord(String name) {

        List<Object[]> result = Lists.newArrayList();
        if (StringUtils.isBlank(name)) {
            logger.info("------------------ processInputRecord null");
            return result;
        }

        String[] tokens = name.split("\\s+");
        for(String token : tokens){
            logger.info("-------------------- " + token);
        }

        // John Smith
        if (tokens.length == 2) {
            result.add(new Object[] { tokens[0], tokens[1] });
        }
        // John and Ann White -> John White 和 Ann White
        else if (tokens.length == 4 && tokens[1].equals("and")) {
            result.add(new Object[] { tokens[0], tokens[3] });
            result.add(new Object[] { tokens[2], tokens[3] });
        }

        return result;

    }

    @Override
    public void process(Object[] record) throws HiveException {

        final String name = stringObjectInspector.getPrimitiveJavaObject(record[0]).toString();
        if(StringUtils.isBlank(name)){
            logger.info("------------------- process null");
        }
        // 处理行
        List<Object[]> results = processInputRecord(name);

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
