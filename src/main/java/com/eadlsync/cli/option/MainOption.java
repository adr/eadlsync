package com.eadlsync.cli.option;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.Parameter;
import org.slf4j.LoggerFactory;

/**
 * Cli options that are know to the program
 */

public class MainOption {

    @Parameter(names = "--help", help = true)
    private boolean help = false;

    @Parameter(names = "--debug", description = "Debug mode")
    private boolean debug = false;

    @Parameter(names = "--stacktrace", description = "print stacktrace")
    private boolean stacktrace = false;

    private static void setLoggingLevel(Level level) {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

    public void evaluateDebugMode() {
        setLoggingLevel(debug ? Level.DEBUG : Level.INFO);
    }

    public void evaluateStacktraceMode() {
        setLoggingLevel(stacktrace ? Level.ERROR : Level.INFO);
    }

}
