package com.eadlsync.cli.command;

import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.util.common.Statistic;
import com.eadlsync.util.ystatement.YStatementField;

import java.util.Arrays;

import static com.eadlsync.cli.command.StatisticCommand.DESCRIPTION;
import static com.eadlsync.util.ystatement.YStatementField.*;

/**
 * Statistic command used to get statistics about the eadls.
 */
@Parameters(commandDescription = DESCRIPTION)
public class StatisticCommand extends EADLSyncCommand {

    public static final String NAME = "statistic";
    public static final String DESCRIPTION = "use 'eadlsync statistic' to display statistics about the decisions in this code repository";

    public void showStatistic() throws Exception {
        if (readConfig()) {
            CLI.println("Statistic of the decisions in this code");
            CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
            Statistic statistic = new Statistic(config.getConfigCore().getProjectRoot());

            CLI.println("Collecting decision statistic");
            statistic.collectStats();
            CLI.println("Processing decision statistic");
            statistic.processStats();
            CLI.println(String.format("\tsource folder is %s", config.getConfigCore().getProjectRoot()));
            CLI.newLine();

            formatAndPrintStatistic(statistic);
        }
    }

    private void formatAndPrintStatistic(Statistic statistic) {
        CLI.println("decisions by packages that have at least one class");
        statistic.getClassesInPackage().forEach((folder, classes) -> {
            if (classes != 0) {
                int decisions = statistic.getDecisionsInPackage().get(folder);
                float coverage = (float) decisions / classes * 100f;
                CLI.println(String.format("\t%d decisions for %d classes (%.1f%%) in package %s", decisions, classes, coverage, folder));
            }
        });
        CLI.println("\t--------------------");
        float coverage = (float) statistic.getDecisions() / ((statistic.getClasses() == 0) ? 1 : statistic.getClasses()) * 100f;
        CLI.println(String.format("\t%d total decisions for %d classes in %d packages resulting in a decision coverage of %.1f%%", statistic.getDecisions(), statistic.getClasses(), statistic.getPackages(), coverage));
        CLI.newLine();

        for (YStatementField field : YStatementField.values()) {
            if (Arrays.asList(CONTEXT, FACING, ACHIEVING, ACCEPTING).contains(field)) {
                int decisionsWithoutField = statistic.getDecisionsWithoutField().get(field);
                float percent = (float) decisionsWithoutField / ((statistic.getDecisions() == 0) ? 1f : statistic.getDecisions()) * 100f;
                CLI.println(String.format("decision field %s", field));
                CLI.println(String.format("\t%d decisions (%.1f%%) have the field %s not specified", decisionsWithoutField, percent, field));
                CLI.println(String.format("\tminimum content length is %d", statistic.getMinFieldLengthOrOptions().get(field)));
                CLI.println(String.format("\tmaximum content length is %d", statistic.getMaxFieldLengthOrOptions().get(field)));
                CLI.println(String.format("\taverage content length is %.1f", statistic.getAvgFieldLengthOrOptions().get(field)));
                CLI.println(String.format("\tcontent length median is %.1f", statistic.getMedFieldLengthOrOptions().get(field)));
                CLI.newLine();
            } else if (field == NEGLECTED) {
                CLI.println(String.format("decision field %s", field));
                CLI.println(String.format("\tminimum content length is %d", statistic.getMinFieldLengthOrOptions().get(field)));
                CLI.println(String.format("\tmaximum content length is %d", statistic.getMaxFieldLengthOrOptions().get(field)));
                CLI.println(String.format("\taverage neglected options %.1f", statistic.getAvgFieldLengthOrOptions().get(field)));
                CLI.println(String.format("\tcontent length median is %.1f", statistic.getMedFieldLengthOrOptions().get(field)));
                CLI.newLine();
            }
        }
    }

}
