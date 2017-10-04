package io.github.adr.eadlsync.cli;

import io.github.adr.eadlsync.cli.command.CommitCommand;
import io.github.adr.eadlsync.cli.command.ConfigCommand;
import io.github.adr.eadlsync.cli.command.DeInitCommand;
import io.github.adr.eadlsync.cli.command.InitCommand;
import io.github.adr.eadlsync.cli.command.PullCommand;
import io.github.adr.eadlsync.cli.command.ResetCommand;
import io.github.adr.eadlsync.cli.command.StatusCommand;
import io.github.adr.eadlsync.cli.command.SyncCommand;


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
        } else if (command.equals(DeInitCommand.class)) {
            description = DeInitCommand.DESCRIPTION;
        } else if (command.equals(InitCommand.class)) {
            description = InitCommand.DESCRIPTION;
        } else if (command.equals(PullCommand.class)) {
            description = PullCommand.DESCRIPTION;
        } else if (command.equals(ResetCommand.class)) {
            description = ResetCommand.DESCRIPTION;
        } else if (command.equals(StatusCommand.class)) {
            description = StatusCommand.DESCRIPTION;
        } else if (command.equals(SyncCommand.class)) {
            description = SyncCommand.DESCRIPTION;
        }
        println(String.format("\t(%s)", description));
    }

    public static void newLine() {
        println("");
    }
}
