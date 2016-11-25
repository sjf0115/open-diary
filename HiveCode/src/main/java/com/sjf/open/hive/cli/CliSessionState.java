package com.sjf.open.hive.cli;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.session.SessionState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xiaosi on 16-10-26.
 */
public class CliSessionState extends SessionState {
    /**
     * -database option if any that the session has been invoked with.
     */
    public String database;

    /**
     * -e option if any that the session has been invoked with.
     */
    public String execString;

    /**
     * -f option if any that the session has been invoked with.
     */
    public String fileName;

    /**
     * properties set from -hiveconf via cmdline.
     */
    public Properties cmdProperties = new Properties();

    /**
     * -i option if any that the session has been invoked with.
     */
    public List<String> initFiles = new ArrayList<String>();

    public CliSessionState(HiveConf conf) {
        super(conf);
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
