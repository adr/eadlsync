package com.eadlsync.cli;

import java.util.Arrays;

import com.eadlsync.model.report.EADLSyncReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tobias on 23.04.2017.
 */
public class ReportMenu extends ACLIMenu {

    private static final Logger LOG = LoggerFactory.getLogger(ReportMenu.class);
    private static final ReportMenu instance = new ReportMenu();

    private ReportMenu() {
        super("Report Menu");
        setMenuItems(Arrays.asList(new CLIMenuItem("11", "Reinitialize Sync"), new CLIMenuItem("1",
                "print y-statements of code repository"), new CLIMenuItem("2", "print y-statements of " +
                "" + "se-repo"), new CLIMenuItem("3", "print " + "additional y-statements of the " +
                "se-repo"), new CLIMenuItem("4", "print " + "obsolete " + "y-statements of the code " +
                "repository"), new CLIMenuItem("5", "print different" + "y-statements"), new
                CLIMenuItem("6", "print " + "complete report"), new CLIMenuItem("0", "back"), new
                CLIMenuItem("00", "exit")));
        bindLoop(option.isNotEqualTo("0").or(option.isNotEqualTo("00")));
    }

    public static ReportMenu getInstance() {
        return instance;
    }

    @Override
    public void evaluate(String option) {
        EADLSyncReport report = EADLSyncMain.getSynchronizer().getEadlSyncReport();
        switch (option) {
            case "0":
                MainMenu.getInstance().show();
                break;
            case "11":
                try {
                    EADLSyncMain.getSynchronizer().reinitializeCodeRepo();
                    EADLSyncMain.getSynchronizer().reinitializeSeRepo();
                } catch (Exception e) {
                    LOG.error("Error while reinitializing the sync class", e);
                }
                break;
            case "1":
                System.out.println(report.codeRepoYStatementsProperty().get());
                break;
            case "2":
                System.out.println(report.seRepoYStatementsProperty().get());
                break;
            case "3":
                System.out.println(report.additionalYStatementsProperty().get());
                break;
            case "4":
                System.out.println(report.obsoleteRepoYStatementsProperty().get());
                break;
            case "5":
                System.out.println(report.differentYStatementsProperty().get());
                break;
            case "6":
                System.out.println(report);
                break;
            case "00":
                System.out.println("exiting...");
                System.exit(0);
            default:
                System.out.println("No valid option");
        }
    }

}
