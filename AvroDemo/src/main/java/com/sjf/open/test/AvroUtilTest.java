package com.sjf.open.test;

import com.sjf.open.utils.AvroUtil;
import org.apache.avro.Schema;
import org.junit.Test;

import java.io.File;

/**
 * Created by xiaosi on 16-9-18.
 */
public class AvroUtilTest {

    private static final String userSchema = "{\n" +
            "  \"namespace\":\"com.sjf.open.model\",\n" +
            "  \"type\":\"record\",\n" +
            "  \"name\":\"User\",\n" +
            "  \"fields\":[\n" +
            "    {\"name\":\"name\",\"type\":\"string\"},\n" +
            "    {\"name\":\"age\",\"type\":\"int\"},\n" +
            "    {\"name\":\"favorite\",\"type\":[\"string\",\"null\"]}\n" +
            "  ]\n" +
            "}";

    private static final String bookSchema = "{\n" +
            "  \"namespace\":\"com.sjf.open.model\",\n" +
            "  \"type\":\"record\",\n" +
            "  \"name\":\"Book\",\n" +
            "  \"fields\":[\n" +
            "    {\"name\":\"name\",\"type\":\"string\"},\n" +
            "    {\"name\":\"price\",\"type\":[\"double\",\"null\"]},\n" +
            "    {\"name\":\"author\",\"type\":com.sjf.open.model.User}\n" +
            "  ]\n" +
            "}";

    @Test
    public void testParseSchemaFromStr() throws Exception{

        AvroUtil.parseSchema(userSchema);
        Schema extended = AvroUtil.parseSchema(bookSchema);
        System.out.println(extended.toString(true));
    }

    @Test
    public void testParseSchemaFromFile() throws Exception{
        File userFile = new File(AvroUtil.class.getResource("/schema/user.avsc").getPath());
        File bookFile = new File(AvroUtil.class.getResource("/schema/book.avsc").getPath());

        AvroUtil.parseSchema(userFile);
        Schema extended = AvroUtil.parseSchema(bookFile);
        System.out.println(extended.toString(true));
    }
}
