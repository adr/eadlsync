package com.eadlsync.cli.option;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.Parameter;
import org.slf4j.LoggerFactory;

/**
 * Created by tobias on 02/06/2017.
 */

public class MainOption {

    @Parameter(names = "--help", description = "Shows this help")
    private boolean help = false;

    @Parameter(names = "--debug", description = "Debug mode")
    private boolean debug = false;


    public void printHelp() {

    }

    public void enableDebugMode() {
        setLoggingLevel(Level.DEBUG);
    }

    public static void setLoggingLevel(Level level) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

}
