package com.sjf.open.test;

import com.sjf.open.utils.AvroUtil;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.util.Utf8;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;

/**
 * Created by xiaosi on 16-9-18.
 */
public class AvroInheritanceTest {
    private Schema schema;
    private Schema subSchema;

    @Before
    public void setUp() throws Exception {

        File facebookUserFile = new File(AvroUtil.class.getResource("/schema/facebookUser.avsc").getPath());
        File facebookSpecialUserFile = new File(AvroUtil.class.getResource("/schema/facebookSpecialUser.avsc").getPath());

        subSchema = AvroUtil.parseSchema(facebookUserFile);
        schema = AvroUtil.parseSchema(facebookSpecialUserFile);

    }

    @Test
    public void testSimpleInheritance() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        BinaryEncoder encoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);

        GenericDatumWriter writer = new GenericDatumWriter(schema);

        // subRecord1
        GenericRecord subRecord1 = new GenericData.Record(subSchema);
        subRecord1.put("name", new Utf8("Doctor Who"));
        subRecord1.put("num_likes", 1);
        subRecord1.put("num_photos", 0);
        subRecord1.put("num_groups", 423);

        // subRecord2
        GenericRecord subRecord2 = new GenericData.Record(subSchema);
        subRecord2.put("name", new org.apache.avro.util.Utf8("Doctor WhoWho"));
        subRecord2.put("num_likes", 2);
        subRecord2.put("num_photos", 0);
        subRecord2.put("num_groups", 424);

        // record1
        GenericRecord record1 = new GenericData.Record(schema);
        record1.put("user", subRecord1);
        record1.put("specialData", 1);
        writer.write(record1, encoder);

        // record2
        GenericRecord record2 = new GenericData.Record(schema);
        record2.put("user", subRecord2);
        record2.put("specialData", 2);
        writer.write(record2, encoder);

        encoder.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
        GenericDatumReader reader = new GenericDatumReader(schema);
        while (true) {
            try {
                GenericRecord result = (GenericRecord) reader.read(null, decoder);
                System.out.println(result);
            } catch (EOFException eof) {
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
