package io.github.adr.eadlsync.cli.command;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import io.github.adr.eadlsync.cli.CLI;
import io.github.adr.eadlsync.cli.CommitIdValidator;
import io.github.adr.eadlsync.gui.DiffView;
import io.github.adr.eadlsync.model.config.ConfigCore;
import io.github.adr.eadlsync.model.decision.YStatementJustificationComparisionObject;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import io.github.adr.eadlsync.util.io.JavaDecisionParser;
import io.github.adr.eadlsync.util.net.YStatementSeItemHelper;
import io.github.adr.eadlsync.util.ystatement.YStatementJustificationComparator;

import static io.github.adr.eadlsync.cli.command.DiffCommand.DESCRIPTION;

/**
 * Diff command used to visualize the differences of the local decisions compared to
 * the decisions of the se-repo of a specified commit.
 */
@Parameters(commandDescription = DESCRIPTION)
public class DiffCommand extends EADLSyncCommand {

    public static final String NAME = "diff";
    public static final String DESCRIPTION = "use 'eadlsync diff <commit-id>' to view the diff of the specified decisions compared to the local decisions";

    @Parameter(required = true, description = "commit-id", validateWith = CommitIdValidator.class)
    private String commitId;

    @Parameter(names = {"-g", "--gui"}, description = "display the diff in a JavaFX window")
    private boolean gui = false;

    private List<YStatementJustificationComparisionObject> createYStatementJustificationComparisionObjects() throws Exception {
        List<YStatementJustificationWrapper> localDecisions = JavaDecisionParser.readYStatementsFromDirectory(Paths.get(config.getConfigCore().getProjectRoot()));
        ConfigCore core = config.getConfigCore();
        List<YStatementJustificationWrapper> remoteDecisions = YStatementSeItemHelper.getYStatementJustifications(core.getBaseUrl(), core.getProjectName(), commitId);

        List<YStatementJustificationComparisionObject> comparisionObjects = new ArrayList<>();

        localDecisions.forEach(local -> {
            List<YStatementJustificationWrapper> same = remoteDecisions.stream().filter(remote ->
                    YStatementJustificationComparator.isSame(local, remote)).collect(Collectors.toList());
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
