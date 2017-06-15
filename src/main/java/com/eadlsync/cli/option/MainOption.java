package com.eadlsync.cli.option;

import com.beust.jcommander.Parameter;

/**
 * Created by tobias on 02/06/2017.
 */

public class MainOption {

    @Parameter(names = "--help", description = "Shows this help")
    private boolean help = false;

    @Parameter(names = "--debug", description = "Debug mode")
    private boolean debug = false;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
