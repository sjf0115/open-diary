package com.sjf.open.hive.cli;

import com.google.common.collect.Maps;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.hive.common.cli.CommonCliOptions;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * Created by xiaosi on 16-10-26.
 */
public class OptionsProcessor {

    protected static final Logger logger = LoggerFactory.getLogger(OptionsProcessor.class.getName());
    private final Options options = new Options();
    private CommandLine commandLine;
    Map<String, String> hiveVariables = Maps.newHashMap();

    public OptionsProcessor() {

        // -database database
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("databasename")
                .withLongOpt("database")
                .withDescription("Specify the database to use")
                .create());

        // -e 'quoted-query-string'
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("quoted-query-string")
                .withDescription("SQL from command line")
                .create('e'));

        // -f <query-file>
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("filename")
                .withDescription("SQL from files")
                .create('f'));

        // -i <init-query-file>
        options.addOption(OptionBuilder
                .hasArg()
                .withArgName("filename")
                .withDescription("Initialization SQL file")
                .create('i'));

        // -hiveconf x=y
        options.addOption(OptionBuilder
                .withValueSeparator()
                .hasArgs(2)
                .withArgName("property=value")
                .withLongOpt("hiveconf")
                .withDescription("Use value for given property")
                .create());

        // Substitution option -d, --define
        options.addOption(OptionBuilder
                .withValueSeparator()
                .hasArgs(2)
                .withArgName("key=value")
                .withLongOpt("define")
                .withDescription("Variable subsitution to apply to hive commands. e.g. -d A=B or --define A=B")
                .create('d'));

        // Substitution option --hivevar
        options.addOption(OptionBuilder
                .withValueSeparator()
                .hasArgs(2)
                .withArgName("key=value")
                .withLongOpt("hivevar")
                .withDescription("Variable subsitution to apply to hive commands. e.g. --hivevar A=B")
                .create());

        // [-S|--silent]
        options.addOption(new Option("S", "silent", false, "Silent mode in interactive shell"));

        // [-v|--verbose]
        options.addOption(new Option("v", "verbose", false, "Verbose mode (echo executed SQL to the console)"));

        // [-H|--help]
        options.addOption(new Option("H", "help", false, "Print help information"));
    }

    public boolean process_stage1(String[] argv) {
        try {
            commandLine = new GnuParser().parse(options, argv);
            Properties confProps = commandLine.getOptionProperties("hiveconf");
            for (String propKey : confProps.stringPropertyNames()) {
                // with HIVE-11304, hive.root.logger cannot have both logger name and log level.
                // if we still see it, split logger and level separately for hive.root.logger
                // and hive.log.level respectively
                if (propKey.equalsIgnoreCase("hive.root.logger")) {
                    CommonCliOptions.splitAndSetLogger(propKey, confProps);
                } else {
                    System.setProperty(propKey, confProps.getProperty(propKey));
                }
            }

            Properties hiveVars = commandLine.getOptionProperties("define");
            for (String propKey : hiveVars.stringPropertyNames()) {
                hiveVariables.put(propKey, hiveVars.getProperty(propKey));
            }

            Properties hiveVars2 = commandLine.getOptionProperties("hivevar");
            for (String propKey : hiveVars2.stringPropertyNames()) {
                hiveVariables.put(propKey, hiveVars2.getProperty(propKey));
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printUsage();
            return false;
        }
        return true;
    }

    private void printUsage() {
        new HelpFormatter().printHelp("hive", options);
    }

    public Map<String, String> getHiveVariables() {
        return hiveVariables;
    }
}
