package com.eadlsync.cli;

import com.eadlsync.cli.command.CommitCommand;
import com.eadlsync.cli.command.ConfigCommand;


/**
 * Helper class display information on the console.
 */
public class CLI {

    public static void println(String message) {
        System.out.println(message);
    }

    public static void println(Class command) {
        String description = "";
        if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        } else if (command.equals(ConfigCommand.class)) {
            description = ConfigCommand.DESCRIPTION;
        } else if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        } else if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        } else if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        } else if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        } else if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        } else if (command.equals(CommitCommand.class)) {
            description = CommitCommand.DESCRIPTION;
        }
        println(String.format("\t(%s)", description));
    }

    public static void newLine() {
        println("");
    }
}
