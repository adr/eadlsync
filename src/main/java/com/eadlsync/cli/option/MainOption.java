package com.eadlsync.cli.option;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.Parameter;
import org.slf4j.LoggerFactory;

/**
 * This class contains cli options that are know to the program.
 * For the logging options setting --log all has highest priority.
 * On any other value the --debug option will be checked. If not
 * available the --stacktrace option will be check. If not available
 * --log value will will be used or Level.OFF if not specified.
 *
 * @option help
 * @option debug
 * @option stacktrace
 * @option log
 */
public class MainOption {

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this program", help = true)
    private boolean help = false;

    @Parameter(names = {"-d", "--debug"}, description = "Debug mode")
    private boolean debug = false;

    @Parameter(names = {"-s", "--stacktrace"}, description = "Print stacktrace")
    private boolean stacktrace = false;

    @Parameter(names = {"-l", "--log"}, description = "Set the log level", converter = LogLevelConverter.class)
    private Level level = Level.OFF;

    private static void setLoggingLevel(Level level) {
        Logger root = (Logger) LoggerFactory.getLogger("com.eadlsync");
        root.setLevel(level);
    }
    public void evaluateOptions() {
        if (!level.equals(Level.ALL)) {
            if (debug) {
                setLoggingLevel(Level.DEBUG);
            } else if (stacktrace) {
                setLoggingLevel(Level.ERROR);
            } else {
                setLoggingLevel(level);
            }
        }
    }

    public boolean isHelp() {
        return help;
    }
}
