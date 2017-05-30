package com.eadlsync.cli;

import java.util.List;
import java.util.Scanner;

import ch.hsr.isf.serepo.data.restinterface.commit.Commit;
import com.eadlsync.sync.EADLSynchronizer;
import com.eadlsync.sync.IEADLSynchronizer;
import com.eadlsync.util.net.APIConnector;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.util.net.APIConnector.getCommitIdFromCommit;

/**
 * Created by Tobias on 23.04.2017.
 */
public class EADLSyncMain {

    private static final Logger LOG = LoggerFactory.getLogger(EADLSyncMain.class);
    private static IEADLSynchronizer synchronizer;

    public static IEADLSynchronizer getSynchronizer() {
        return synchronizer;
    }

    public static void main(String[] args) {
        Options options = createCLIOptions();

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("eADLSync", options);

            System.exit(1);
            return;
        }

        String codeRepoPathOrUrl = cmd.getOptionValue("codePath");
        String seRepoBasePath = cmd.getOptionValue("serepoUrl");
        String seRepoProjectName = cmd.getOptionValue("projectName");
        String seRepoCommitId = cmd.getOptionValue("commitId");

        if (seRepoCommitId == null) {
            try {
                List<Commit> commits = APIConnector.getCommitsByUrl(seRepoBasePath + "/repos/" +
                        seRepoProjectName + "/commits");
                for (int i = commits.size() - 1; i >= 0; i--) {
                    Commit item = commits.get(i);
                    System.out.println(String.format("%d - %s\n\t%s", i, getCommitIdFromCommit(item),
                            item.getShortMessage()));
                }
                System.out.println("Please choose a commit by writing a number and pressing enter");
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();
                seRepoCommitId = String.valueOf(getCommitIdFromCommit(commits.get(choice)));
            } catch (UnirestException e) {
                LOG.debug("Failed to retrieve commits, exiting...", e);
                System.exit(0);
            }
        }

        // code repo path needs to specify the root of the sources folder of the java project
        // eg for this project it is -c "/Users/tobias/git/eadlsync/src/main/java"
        LOG.info("Code repo: {}", codeRepoPathOrUrl);
        LOG.info("Se-repo base url: {}", seRepoBasePath);
        LOG.info("Se-repo project name: {}", seRepoProjectName);
        LOG.info("Se-repo commit id: {}", seRepoCommitId);

        synchronizer = new EADLSynchronizer(codeRepoPathOrUrl, seRepoBasePath, seRepoProjectName,
                seRepoCommitId);
        MainMenu.getInstance().show();


    }

    private static Options createCLIOptions() {
        Options options = new Options();

        Option codeRepoPathOption = new Option("c", "codePath", true, "Path to the code repository");
        codeRepoPathOption.setRequired(true);
        options.addOption(codeRepoPathOption);

        Option seRepoPathOption = new Option("s", "serepoUrl", true, "Path to the se-repo");
        seRepoPathOption.setRequired(true);
        options.addOption(seRepoPathOption);

        Option seRepoNameOption = new Option("p", "projectName", true, "Project name in the se-repo");
        seRepoNameOption.setRequired(true);
        options.addOption(seRepoNameOption);

        Option seRepoCommitOption = new Option("i", "commitId", true, "Commit id");
        seRepoCommitOption.setRequired(false);
        options.addOption(seRepoCommitOption);

        return options;
    }

}
