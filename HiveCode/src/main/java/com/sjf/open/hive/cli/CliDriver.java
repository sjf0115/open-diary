package com.sjf.open.hive.cli;

import org.apache.hadoop.hive.common.LogUtils;
import org.apache.hadoop.hive.common.io.CachingPrintStream;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.conf.VariableSubstitution;
import org.apache.hadoop.hive.ql.session.SessionState;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by xiaosi on 16-10-26.
 */
public class CliDriver {

    /**
     *  run
     * @param args
     * @return
     * @throws Exception
     */
    public  int run(String[] args) throws Exception{

        OptionsProcessor oproc = new OptionsProcessor();
        if (!oproc.process_stage1(args)) {
            return 1;
        }

        // NOTE: It is critical to do this here so that log4j is reinitialized
        // before any of the other core hive classes are loaded
        boolean logInitFailed = false;
        String logInitDetailMessage;
        try {
            logInitDetailMessage = LogUtils.initHiveLog4j();
        } catch (LogUtils.LogInitializationException e) {
            logInitFailed = true;
            logInitDetailMessage = e.getMessage();
        }

        CliSessionState ss = new CliSessionState(new HiveConf(SessionState.class));
        ss.in = System.in;
        try {
            ss.out = new PrintStream(System.out, true, "UTF-8");
            ss.info = new PrintStream(System.err, true, "UTF-8");
            ss.err = new CachingPrintStream(System.err, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return 3;
        }

        /*if (!oproc.process_stage2(ss)) {
            return 2;
        }*/

        if (!ss.getIsSilent()) {
            if (logInitFailed) {
                System.err.println(logInitDetailMessage);
            } else {
                SessionState.getConsole().printInfo(logInitDetailMessage);
            }
        }

        // set all properties specified via command line
        HiveConf conf = ss.getConf();
        for (Map.Entry<Object, Object> item : ss.cmdProperties.entrySet()) {
            conf.set((String) item.getKey(), (String) item.getValue());
            ss.getOverriddenConfigurations().put((String) item.getKey(), (String) item.getValue());
        }

        /*// read prompt configuration and substitute variables.
        prompt = conf.getVar(HiveConf.ConfVars.CLIPROMPT);
        prompt = new VariableSubstitution(new HiveVariableSource() {
            @Override
            public Map<String, String> getHiveVariable() {
                return SessionState.get().getHiveVariables();
            }
        }).substitute(conf, prompt);
        prompt2 = spacesForString(prompt);

        if (HiveConf.getBoolVar(conf, HiveConf.ConfVars.HIVE_CLI_TEZ_SESSION_ASYNC)) {
            // Start the session in a fire-and-forget manner. When the asynchronously initialized parts of
            // the session are needed, the corresponding getters and other methods will wait as needed.
            SessionState.beginStart(ss, console);
        } else {
            SessionState.start(ss);
        }

        ss.updateThreadName();

        // execute cli driver work
        try {
            return executeDriver(ss, conf, oproc);
        } finally {
            ss.resetThreadName();
            ss.close();
        }*/
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int ret = new CliDriver().run(args);
        System.exit(ret);
    }
}
