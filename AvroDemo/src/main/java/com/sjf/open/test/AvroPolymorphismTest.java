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
import org.apache.avro.io.JsonDecoder;
import org.apache.avro.io.JsonEncoder;
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
public class AvroPolymorphismTest {
    private Schema FBUser;
    private Schema FBUser2;
    private Schema base;
    private Schema ext1;
    private Schema ext2;
    private Schema ext3;

    @Before
    public void setUp() throws Exception {

        File facebookUserFile = new File(AvroUtil.class.getResource("/schema/facebookUser.avsc").getPath());
        File extension1File = new File(
                AvroUtil.class.getResource("/schema/facebookSpecialUserExtension1.avsc").getPath());
        File extension2File = new File(
                AvroUtil.class.getResource("/schema/facebookSpecialUserExtension2.avsc").getPath());
        File extension3File = new File(
                AvroUtil.class.getResource("/schema/facebookSpecialUserExtension3.avsc").getPath());
        File facebookUserInheritanceFile = new File(
                AvroUtil.class.getResource("/schema/facebookUserInheritance.avsc").getPath());
        File facebookUserInheritance2File = new File(
                AvroUtil.class.getResource("/schema/facebookUserInheritance2.avsc").getPath());


        base = AvroUtil.parseSchema(facebookUserFile);
        ext1 = AvroUtil.parseSchema(extension1File);
        ext2 = AvroUtil.parseSchema(extension2File);
        ext3 = AvroUtil.parseSchema(extension3File);
        FBUser = AvroUtil.parseSchema(facebookUserInheritanceFile);
        FBUser2 = AvroUtil.parseSchema(facebookUserInheritance2File);
    }

    @Test
    public void testInheritanceBinary() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GenericDatumWriter writer = new GenericDatumWriter(FBUser2);
        //BinaryEncoder encoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        JsonEncoder encoder = EncoderFactory.get().jsonEncoder(FBUser2,outputStream);

        GenericRecord baseRecord = new GenericData.Record(base);
        baseRecord.put("name", new Utf8("Doctor Who"));
        baseRecord.put("num_likes", 1);
        baseRecord.put("num_photos", 0);
        baseRecord.put("num_groups", 423);

        GenericRecord FBrecord = new GenericData.Record(FBUser2);
        FBrecord.put("type", "base");
        FBrecord.put("user", baseRecord);

        writer.write(FBrecord, encoder);

        baseRecord = new GenericData.Record(base);
        baseRecord.put("name", new Utf8("Doctor WhoWho"));
        baseRecord.put("num_likes", 1);
        baseRecord.put("num_photos", 0);
        baseRecord.put("num_groups", 423);
        GenericRecord extRecord = new GenericData.Record(ext1);
        extRecord.put("specialData1", 1);
        FBrecord = new GenericData.Record(FBUser2);
        FBrecord.put("type", "extension1");
        FBrecord.put("user", baseRecord);
        FBrecord.put("extension", extRecord);

        writer.write(FBrecord, encoder);

        baseRecord = new GenericData.Record(base);
        baseRecord.put("name", new Utf8("Doctor WhoWhoWho"));
        baseRecord.put("num_likes", 2);
        baseRecord.put("num_photos", 0);
        baseRecord.put("num_groups", 424);
        extRecord = new GenericData.Record(ext2);
        extRecord.put("specialData2", 2);
        FBrecord = new GenericData.Record(FBUser2);
        FBrecord.put("type", "extension2");
        FBrecord.put("user", baseRecord);
        FBrecord.put("extension", extRecord);

        writer.write(FBrecord, encoder);

        baseRecord = new GenericData.Record(base);
        baseRecord.put("name", new Utf8("Doctor WhoWhoWhoWho"));
        baseRecord.put("num_likes", 3);
        baseRecord.put("num_photos", 0);
        baseRecord.put("num_groups", 424);
        extRecord = new GenericData.Record(ext3);
        extRecord.put("specialData3", 3);
        FBrecord = new GenericData.Record(FBUser2);
        FBrecord.put("type", "extension3");
        FBrecord.put("user", baseRecord);
        FBrecord.put("extension", extRecord);

        writer.write(FBrecord, encoder);

        encoder.flush();

        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        //BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
        JsonDecoder decoder = DecoderFactory.get().jsonDecoder(FBUser, inputStream);
        GenericDatumReader reader = new GenericDatumReader(FBUser);
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
