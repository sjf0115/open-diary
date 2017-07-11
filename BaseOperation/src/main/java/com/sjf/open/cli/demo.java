package com.sjf.open.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by xiaosi on 17-6-30.
 */
public class demo {

    public static Options booleanOption() {
        // Boolean Option
        Option help = new Option("help", "print this message");
        Option projectHelp = new Option("projecthelp", "print project help information");
        Option version = new Option("version", "print the version information and exit");
        Option quiet = new Option("quiet", "be extra quiet");
        Option verbose = new Option("verbose", "be extra verbose");
        Option debug = new Option("debug", "print debugging information");
        Option emacs = new Option("emacs", "produce logging information without adornments");

        Options options = new Options();

        options.addOption(help);
        options.addOption(projectHelp);
        options.addOption(version);
        options.addOption(quiet);
        options.addOption(verbose);
        options.addOption(debug);
        options.addOption(emacs);

        return options;

    }

    public static Options argumentOption() {
        // Argument Option
        Option logfile = Option.builder("logfile").argName("file").hasArg().desc("use given file for log").build();
        Option logger = Option.builder("logger").argName("classname").hasArg()
                .desc("the class which it to perform " + "logging").build();
        Option listener = Option.builder("listener").argName("classname").hasArg()
                .desc("add an instance of class as " + "a project listener").build();
        Option buildFile = Option.builder("buildfile").argName("file").hasArg().desc("use given buildfile").build();
        Option find = Option.builder("find").argName("file").hasArg()
                .desc("search for buildfile towards the " + "root of the filesystem and use it").build();

        Options options = new Options();

        options.addOption(logfile);
        options.addOption(logger);
        options.addOption(listener);
        options.addOption(buildFile);
        options.addOption(find);

        return options;

    }

    public static Options javaPropertyOption() {
        // Java Property Option
        Option property = Option.builder("D").argName("property=value").hasArgs().valueSeparator()
                .desc("use value for given property").build();

        Options options = new Options();

        options.addOption(property);

        return options;
    }

    /**
     * 解析
     * 
     * @param options
     * @param args
     */
    public static void parse(Options options, String[] args) {

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("logfile")) {
                String path = line.getOptionValue("logfile");
                System.out.println(path);
                return;
            }

            if(line.hasOption("help")){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "ant", options );
                return;
            }

        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }

    public static void main(String[] args) {

        String[] params = {"-help"};
        parse(booleanOption(), params);

        String[] params2 = {"-logfile file=/home/xiaosi/logs"};
        parse(argumentOption(), params2);
    }


}
