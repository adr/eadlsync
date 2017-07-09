package com.eadlsync.cli.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.cli.CLI;
import com.eadlsync.gui.DiffView;
import com.eadlsync.model.config.ConfigCore;
import com.eadlsync.model.decision.YStatementJustificationComparisionObject;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.io.JavaDecisionParser;
import com.eadlsync.util.net.SeRepoUrlObject;
import com.eadlsync.util.net.YStatementAPI;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.eadlsync.cli.command.DiffCommand.DESCRIPTION;

/**
 * Diff command used to visualize the differences of the local decisions compared to
 * the decisions of the se-repo of a specified commit.
 */
@Parameters(commandDescription = DESCRIPTION)
public class DiffCommand extends EADLSyncCommand {

    public static final String NAME = "diff";
    public static final String DESCRIPTION = "use 'eadlsync diff <commitId>' to view the diff of the specified decisions compared to the local decisions";

    @Parameter(required = true)
    private String commitId;
    @Parameter(names = {"-g", "--gui"})
    private boolean gui = false;

    private List<YStatementJustificationComparisionObject> createYStatementJustificationComparisionObjects() throws Exception {
        List<YStatementJustificationWrapper> localDecisions = JavaDecisionParser.readYStatementsFromDirectory(Paths.get(config.getConfigCore().getProjectRoot()));
        ConfigCore core = config.getConfigCore();
        SeRepoUrlObject seRepoUrlObject = new SeRepoUrlObject(core.getBaseUrl(), core.getProjectName(), commitId);
        List<YStatementJustificationWrapper> remoteDecisions = YStatementAPI.withSeRepoUrl(seRepoUrlObject).getYStatementJustifications();

        List<YStatementJustificationComparisionObject> comparisionObjects = new ArrayList<>();

        localDecisions.forEach(local -> {
            List<YStatementJustificationWrapper> same = remoteDecisions.stream().filter(remote -> YStatementJustificationComparator.isSame(local, remote)).collect(Collectors.toList());
            if (same.isEmpty()) {
                comparisionObjects.add(new YStatementJustificationComparisionObject(local, null));
            } else {
                comparisionObjects.add(new YStatementJustificationComparisionObject(local, same.get(0)));
            }
        });
        remoteDecisions.forEach(remote -> {
            List<YStatementJustificationWrapper> same = remoteDecisions.stream().filter(local -> YStatementJustificationComparator.isSame(remote, local)).collect(Collectors.toList());
            if (same.isEmpty()) {
                comparisionObjects.add(new YStatementJustificationComparisionObject(remote, null));
            }
        });


        return comparisionObjects;
    }

    public void showDiff() throws Exception {
        if (readConfig()) {

            List<YStatementJustificationComparisionObject> decisions = createYStatementJustificationComparisionObjects();

            if (gui) {

                new DiffView(decisions).showDialog();

            } else {
                CLI.println(String.format("Diff of local decisions and commit %s", commitId));
                CLI.println(String.format("\tproject '%s' at %s", config.getConfigCore().getProjectName(), config.getConfigCore().getBaseUrl()));
                decisions.forEach(diff -> {
                    CLI.newLine();
                    CLI.println(diff.toString());
                });

            }

        }
    }

}
