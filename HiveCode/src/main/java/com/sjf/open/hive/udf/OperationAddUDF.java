package com.sjf.open.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.serde2.ByteStream;
import org.apache.hadoop.hive.serde2.io.DoubleWritable;
import org.apache.hadoop.hive.serde2.lazy.LazyInteger;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/**
 * Created by xiaosi on 16-11-19.
 */
@Description(name = "add",
        value = "_FUNC_(num1,num2) - return the sum of num1 and num2",
        extended = "Example:\n" +
                "SELECT add(1,2) FROM src;\n" +
                "SELECT add(1.4,2.3) FROM src;\n" +
                "SELECT add(\"2\",\"4\") FROM src;")
public class OperationAddUDF extends UDF {

    private final ByteStream.Output out = new ByteStream.Output();
    /**
     * IntWritable
     * @param num1
     * @param num2
     * @return
     */
    public IntWritable evaluate(IntWritable num1, IntWritable num2){
        if(num1 == null || num2 == null){
            return null;
        }
        return new IntWritable(num1.get() + num2.get());
    }

    /**
     * DoubleWritable
     * @param num1
     * @param num2
     * @return
     */
    public DoubleWritable evaluate(DoubleWritable num1, DoubleWritable num2){
        if(num1 == null || num2 == null){
            return null;
        }
        return new DoubleWritable(num1.get() + num2.get());
    }

    /**
     * FloatWritable
     * @param num1
     * @param num2
     * @return
     */
    public FloatWritable evaluate(FloatWritable num1, FloatWritable num2){
        if(num1 == null || num2 == null){
            return null;
        }
        return new FloatWritable(num1.get() + num2.get());
    }

    /**
     * Text
     * @param num1
     * @param num2
     * @return
     */
    public Text evaluate(Text num1, Text num2){
        if(num1 == null || num2 == null){
            return null;
        }
        try{
            Integer n1 = Integer.valueOf(num1.toString());
            Integer n2 = Integer.valueOf(num2.toString());
            Integer result = n1 + n2;
            out.reset();
            LazyInteger.writeUTF8NoException(out, result);
            Text text = new Text();
            text.set(out.getData(), 0, out.getLength());
            return text;
        }
        catch (Exception e){
            return null;
        }

    }
}
