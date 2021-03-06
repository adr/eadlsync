package io.github.adr.eadlsync.cli;

import java.io.IOException;
import java.util.Calendar;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import io.github.adr.eadlsync.cli.command.CommitCommand;
import io.github.adr.eadlsync.cli.command.ConfigCommand;
import io.github.adr.eadlsync.cli.command.DeInitCommand;
import io.github.adr.eadlsync.cli.command.DiffCommand;
import io.github.adr.eadlsync.cli.command.InitCommand;
import io.github.adr.eadlsync.cli.command.MergeCommand;
import io.github.adr.eadlsync.cli.command.PullCommand;
import io.github.adr.eadlsync.cli.command.ResetCommand;
import io.github.adr.eadlsync.cli.command.StatisticCommand;
import io.github.adr.eadlsync.cli.command.StatusCommand;
import io.github.adr.eadlsync.cli.command.SyncCommand;
import io.github.adr.eadlsync.cli.option.MainOption;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.adr.eadlsync.cli.CLI.println;

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
    private static final StatisticCommand STATISTIC_COMMAND = new StatisticCommand();
    private static final DiffCommand DIFF_COMMAND = new DiffCommand();
    private static String[] args;

    public static void main(String[] args) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        EADLSyncMain.args = args;


        launch();
        long executionTime = Calendar.getInstance().getTimeInMillis() - startTime;
        LOG.info("Program finished in {} seconds", executionTime / 1000);
    }

    private static void printStacktraceInfo() {
        CLI.println("\t(use --stacktrace to get stacktrace information about an error)");
        CLI.println("\t(use --debug to get stacktrace information and additional debug output)");
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
                addCommand(StatisticCommand.NAME, STATISTIC_COMMAND).
                addCommand(DiffCommand.NAME, DIFF_COMMAND).
                build();

        try {
            commander.parse(args);

            if (option.isHelp()) commander.usage();

            option.evaluateOptions();

            try {
                String commandName = commander.getParsedCommand();
                if (commandName != null) {
                    switch (commandName) {
                        case InitCommand.NAME:
                            if (INIT_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                INIT_COMMAND.initialize();
                            }
                            break;
                        case ConfigCommand.NAME:
                            if (CONFIG_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                CONFIG_COMMAND.configure();
                            }
                            break;
                        case StatusCommand.NAME:
                            if (STATUS_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                STATUS_COMMAND.printStatus();
                            }
                            break;
                        case PullCommand.NAME:
                            if (PULL_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                PULL_COMMAND.pull();
                            }
                            break;
                        case CommitCommand.NAME:
                            if (COMMIT_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                COMMIT_COMMAND.commit();
                            }
                            break;
                        case MergeCommand.NAME:
                            if (MERGE_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                MERGE_COMMAND.merge();
                            }
                            break;
                        case ResetCommand.NAME:
                            if (RESET_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                RESET_COMMAND.resetLocalChanges();
                            }
                            break;
                        case SyncCommand.NAME:
                            if (SYNC_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                SYNC_COMMAND.sync();
                            }
                            break;
                        case DeInitCommand.NAME:
                            if (DE_INIT_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                DE_INIT_COMMAND.deInit();
                            }
                            break;
                        case StatisticCommand.NAME:
                            if (STATISTIC_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                STATISTIC_COMMAND.showStatistic();
                            }
                            break;
                        case DiffCommand.NAME:
                            if (DIFF_COMMAND.isHelp()) {
                                commander.usage(commandName);
                            } else {
                                DIFF_COMMAND.showDiff();
                            }
                            break;
                        default:
                            LOG.debug("Command not found {}", commander.getParsedCommand());
                    }
                }
            } catch (IOException ioException) {
                CLI.println("An error occurred when accessing some file on the local file system.");
                printStacktraceInfo();
            } catch (UnirestException uniRestException) {
                CLI.println("An error occurred when accessing the se-repo, please check your connection to the se-repo.");
                printStacktraceInfo();
            } catch (Exception e) {
                LOG.error("Error", e);
                CLI.println("An unexpected error occurred while the program was running.");
                printStacktraceInfo();
            }

        } catch (ParameterException e) {
            CLI.println(e.getMessage());
            String commandName = commander.getParsedCommand();
            if (commandName == null) {
                commander.usage();
            } else {
                commander.usage(commandName);
            }

        }

        Platform.exit();
    }
}
