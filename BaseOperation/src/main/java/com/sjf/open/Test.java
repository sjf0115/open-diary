package com.sjf.open;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by xiaosi on 16-8-16.
 */
public class Test {
    public static void main(String[] args) throws ParseException {

        String[] params = {"--block-size=10"};
        //定义
        Options options = new Options();
        options.addOption("a", "almost-all", false, "do not list implied . and ..");
        options.addOption("b", "block-size", true, "use SIZE-byte blocks");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,params);

        //查询交互
        if (cmd.hasOption("h")){
            String formatStr = "CLI  cli  test";
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatStr, "", options, "");
            return;
        }

        if (cmd.hasOption("t")){
            System.out.printf("system time has setted  %s \n",cmd.getOptionValue("t"));
            return;
        }

        System.out.println("error");
    }
}
