package com.eadlsync.cli;

import com.beust.jcommander.JCommander;
import com.eadlsync.cli.command.*;
import com.eadlsync.cli.option.MainOption;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.eadlsync.cli.CLI.println;

/**
 * Starting point of the program
 */
public class EADLSyncMain extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(EADLSyncMain.class);
    private static final InitCommand INIT_COMMAND = new InitCommand();
    private static final DeInitCommand DE_INIT_COMMAND = new DeInitCommand();
    private static final ConfigCommand CONFIG_COMMAND = new ConfigCommand();
    private static final StatusCommand STATUS_COMMAND = new StatusCommand();
    private static final CommitCommand COMMIT_COMMAND = new CommitCommand();
    private static final PullCommand PULL_COMMAND = new PullCommand();
    private static final MergeCommand MERGE_COMMAND = new MergeCommand();
    private static final ResetCommand RESET_COMMAND = new ResetCommand();
    private static final SyncCommand SYNC_COMMAND = new SyncCommand();
    private static String[] args;

    public static void main(String[] args) {

        EADLSyncMain.args = args;

        launch();

    }

    private static void printStacktraceInfo() {
        println("\t(use --stacktrace to get more information)");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainOption option = new MainOption();
        JCommander commander = JCommander.newBuilder().addObject(option).
                addCommand(InitCommand.NAME, INIT_COMMAND).
                addCommand(DeInitCommand.NAME, DE_INIT_COMMAND).
                addCommand(ConfigCommand.NAME, CONFIG_COMMAND).
                addCommand(StatusCommand.NAME, STATUS_COMMAND).
                addCommand(CommitCommand.NAME, COMMIT_COMMAND).
                addCommand(PullCommand.NAME, PULL_COMMAND).
                addCommand(MergeCommand.NAME, MERGE_COMMAND).
                addCommand(ResetCommand.NAME, RESET_COMMAND).
                addCommand(SyncCommand.NAME, SYNC_COMMAND).
                build();

        commander.parse(args);

        option.evaluateDebugMode();
        option.evaluateStacktraceMode();

        try {
            switch (commander.getParsedCommand()) {
                case InitCommand.NAME:
                    INIT_COMMAND.initialize();
                    break;
                case StatusCommand.NAME:
                    STATUS_COMMAND.printStatus();
                    break;
                case PullCommand.NAME:
                    PULL_COMMAND.pull();
                    break;
                case CommitCommand.NAME:
                    COMMIT_COMMAND.commit();
                    break;
                case MergeCommand.NAME:
                    MERGE_COMMAND.merge();
                    break;
                case ResetCommand.NAME:
                    RESET_COMMAND.resetLocalChanges();
                    break;
                case SyncCommand.NAME:
                    SYNC_COMMAND.sync();
                    break;
                case DeInitCommand.NAME:
                    DE_INIT_COMMAND.deInit();
                    break;
                default:
                    LOG.debug("Command not found {}", commander.getParsedCommand());
            }
        } catch (IOException ioException) {
            println("An error occurred when accessing some file on the local file system.");
            printStacktraceInfo();
        } catch (UnirestException uniRestException) {
            println("An error occurred when accessing the se-repo, please check your connection to the se-repo.");
            printStacktraceInfo();
        } catch (Exception e) {
            LOG.error("Error", e);
            println("An unexpected error occurred while the program was running.");
            printStacktraceInfo();
        }
    }
}
