package com.eadlsync.cli;

import java.util.Arrays;

import com.eadlsync.EADLSyncMain;
import com.eadlsync.sync.EADLSyncInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tobias on 23.04.2017.
 */
public class MainMenu extends ACLIMenu {

    private static final Logger LOG = LoggerFactory.getLogger(MainMenu.class);
    private static final MainMenu instance = new MainMenu();

    private MainMenu() {
        super("Main Menu");
        setMenuItems(Arrays.asList(new CLIMenuItem("11", "Reinitialize Sync"), new CLIMenuItem("1", "report menu"), new CLIMenuItem("2", "sync menu"), new
                CLIMenuItem("3", "about"), new CLIMenuItem("00", "exit")));
        bindLoop(option.isNotEqualTo("3").or(option.isNotEqualTo("00").or(option.isNotEqualTo("1").or(option
                .isNotEqualTo("2")))));
    }

    public static MainMenu getInstance() {
        return instance;
    }

    @Override
    protected void evaluate(String option) {
        switch (option) {
            case "11":
                try {
                    EADLSyncMain.getSynchronizer().reinitialize();
                } catch (Exception e) {
                    LOG.error("Error while reinitializing the sync class" , e);
                }
                break;
            case "1":
                ReportMenu.getInstance().show();
                break;
            case "2":
                SyncMenu.getInstance().show();
                break;
            case "3":
                System.out.println(EADLSyncInfo.getInstance());
                break;
//            case "5":
//                // TODO: change base url for se-repo
//            case "6":
//                // TODO: change project name in se-repo
//            case "7":
//                // TODO: change commit id in se-repo
//            case "8":
//                // TODO: change project directory to work with
            case "00":
                System.out.println("exiting...");
                System.exit(0);
            default:
                System.out.println("No valid option");
        }
    }

}
