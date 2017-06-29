package com.sjf.open.parser;

import com.sjf.open.bean.CommandLine;
import com.sjf.open.bean.Options;
import com.sjf.open.exception.ParseException;

/**
 * Created by xiaosi on 17-6-29.
 */
public interface CommandLineParser {

    CommandLine parse(Options options, String[] arguments) throws ParseException;
    CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException;

}
