package com.eadlsync;

import com.eadlsync.cli.MainMenu;
import com.eadlsync.sync.EADLSynchronizer;
import com.eadlsync.sync.IEADLSynchronizer;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String seRepoPath = cmd.getOptionValue("serepoUrl");

        // code repo path needs to specify the root of the sources folder of the java project
        // eg for this project it is -c "/Users/tobias/git/eadlsync/src/main/java"
        LOG.info("Code Repo: {}", codeRepoPathOrUrl);
        LOG.info("Se-Repo: {}", seRepoPath);

        synchronizer = new EADLSynchronizer(codeRepoPathOrUrl, seRepoPath);
        MainMenu.getInstance().show();


    }

    private static Options createCLIOptions() {
        Options options = new Options();

        Option codeRepoPathOption = new Option("c", "codePath", true, "Path to the code repository");
        codeRepoPathOption.setRequired(true);
        options.addOption(codeRepoPathOption);

        Option seRepoPathOption = new Option("s", "serepoUrl", true, "Path to the se-repo");
        codeRepoPathOption.setRequired(true);
        options.addOption(seRepoPathOption);

        return options;
    }

}
