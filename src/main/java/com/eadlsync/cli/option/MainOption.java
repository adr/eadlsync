package com.eadlsync.cli.option;

import java.io.IOException;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.beust.jcommander.Parameter;
import com.eadlsync.cli.CLI;
import com.eadlsync.util.common.BuildInfo;
import org.slf4j.LoggerFactory;

/**
 * This class contains cli options that are know to the program.
 * For the logging options setting --log all has highest priority.
 * On any other value the --debug option will be checked. If not
 * available the --stacktrace option will be check. If not available
 * --log value will will be used or Level.OFF if not specified.
 *
 * @option help
 * @option version
 * @option authors
 * @option copyright
 * @option debug
 * @option stacktrace
 * @option log
 */
public class MainOption {

    @Parameter(names = {"-h", "--help"}, description = "Show the usage of this program", help = true)
    private boolean help = false;

    @Parameter(names = {"-v", "--version"}, description = "Show version information of this program", help = true)
    private boolean version = false;

    @Parameter(names = {"-a", "--author", "--authors", "-dev", "--developers"}, description = "List the authors of this program", help = true)
    private boolean authors = false;

    @Parameter(names = {"-c", "--copyright", "--license"}, description = "Show copyright information of this program", help = true)
    private boolean copyright = false;

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
    public void evaluateOptions() throws IOException {
        if (version) {
            displayVersionInformation();
        }
        if (authors) {
            displayAuthors();
        }
        if (copyright) {
            displayCopyright();
        }
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

    private void displayCopyright() throws IOException {
        BuildInfo buildInfo = new BuildInfo();
        String year = buildInfo.getYear();
        String author = buildInfo.getAuthors();
        CLI.println(String.format("Copyright © 2016-%s %s", year, author));
        CLI.println("Icons made by Freepik from www.flaticon.com");
    }

    private void displayAuthors() {
        String authors = new BuildInfo().getAuthors();
        CLI.println("developed by " + authors);
    }

    private void displayVersionInformation() {
        String version = new BuildInfo().getVersion();
        version = ("${version}".equals(version)) ? "dev" : version;
        CLI.println("eadlsync " + version);
    }

    public boolean isHelp() {
        return help;
    }
}
