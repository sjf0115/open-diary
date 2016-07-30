package com.sjf.open.hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by xiaosi on 16-7-24.
 */
public class HiveCreate {

    private static Logger logger = LoggerFactory.getLogger(HiveCreate.class);
    private static String sql;

    private static void createEmployee(){
        HiveClient.open();

        sql = "create table if not exists employee3(\n" +
                "   name string comment 'employee name',\n" +
                "   salary float comment 'employee salary',\n" +
                "   subordinates array<string> comment 'names of subordinates',\n" +
                "   deductions map<string,float> comment 'keys are deductions values are percentages',\n" +
                "   address struct<street:string, city:string, state:string, zip:int> comment 'home address'\n" +
                ")\n" +
                "PARTITIONED BY (country string, state string)\n" +
                "ROW FORMAT DELIMITED\n" +
                "FIELDS TERMINATED BY '\\001'\n" +
                "COLLECTION ITEMS TERMINATED BY '\\002'\n" +
                "MAP KEYS TERMINATED BY '\\003'\n" +
                "LINES TERMINATED BY '\\n'\n" +
                "STORED AS TEXTFILE;";

        boolean result = HiveClient.execute(sql);
        if(result){
            logger.info("-------- create employee3 success");
        }
        else{
            logger.info("--------- create employee3 failed");
        }
        HiveClient.close();
    }

    public static void main(String[] args) {
        createEmployee();
    }
}
