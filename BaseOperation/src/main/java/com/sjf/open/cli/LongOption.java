package com.sjf.open.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Created by xiaosi on 17-6-29.
 */
public class LongOption {

    public static void run1(String[] args) throws ParseException {

        Options options = new Options();
        options.addOption("v", "var", true, "Here you can set parameter .");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,args);

        if (cmd.hasOption("v")){
            System.out.printf("Here you can set parameter  %s \n", cmd.getOptionValue("v"));
            return;
        }

    }

    public static void run2(String[] args) throws ParseException {

        Options options = new Options();
        options.addOption("h", "help", false, "show help .");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,args);

        if (cmd.hasOption("h")){
            System.out.printf("show help\n", cmd.getOptionValue("h"));
            return;
        }

    }

    public static void run3(String[] args) throws ParseException {

        Options options = new Options();
        Option.Builder builder = Option.builder("b").optionalArg(true).longOpt("block-size");
        options.addOption(builder.build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,args);

        if (cmd.hasOption("b")){
            System.out.printf("block size\n", cmd.getOptionValue("b"));
            return;
        }

    }

    public static void main(String[] args) throws ParseException {

        String[] params = {"-bport"};
        run3(params);
    }

}
