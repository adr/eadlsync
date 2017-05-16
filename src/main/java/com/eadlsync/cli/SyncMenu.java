package com.eadlsync.cli;

import java.util.Arrays;

import com.eadlsync.EADLSyncMain;
import com.eadlsync.sync.IEADLSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tobias on 23.04.2017.
 */
public class SyncMenu extends ACLIMenu {

    private static final Logger LOG = LoggerFactory.getLogger(SyncMenu.class);
    private static final SyncMenu instance = new SyncMenu();

    private SyncMenu() {
        super("Sync Menu");
        setMenuItems(Arrays.asList(new CLIMenuItem("11", "Reinitialize Sync"), new CLIMenuItem("1",
                "print different eads"), new CLIMenuItem("2", "update ead in code repo", Arrays.asList
                ("id")), new CLIMenuItem("3", "update ead in " + "se-repo", Arrays.asList("id")), new
                CLIMenuItem("4", "Commit changes to code and se-repo"), new CLIMenuItem("0", "back"),
                new CLIMenuItem("00", "exit")));
        bindLoop(option.isNotEqualTo("0").or(option.isNotEqualTo("00")));
    }

    protected static SyncMenu getInstance() {
        return instance;
    }

    @Override
    protected void evaluate(String option) {
        IEADLSynchronizer synchronizer = EADLSyncMain.getSynchronizer();
        switch (option) {
            case "0":
                MainMenu.getInstance().show();
                break;
            case "11":
                try {
                    synchronizer.reinitialize();
                } catch (Exception e) {
                    LOG.error("Error while reinitializing the sync class", e);
                }
                break;
            case "1":
                System.out.println(synchronizer.differentYStatementsProperty());
                break;
            case "2":
                synchronizer.updateYStatementInCodeRepo(synchronizer.getEadlSyncReport()
                        .codeRepoYStatementsProperty().get(0).getId());
                System.out.println("updated code repo decision");
                // TODO: use cli parser for argument
                // TODO: rework update of entry
                break;
            case "3":
                synchronizer.updateYStatementInSeRepo(synchronizer.getEadlSyncReport()
                        .seRepoYStatementsProperty().get(0).getId());
                System.out.println("updated se-repo decision");
                // TODO: use cli parser for argument
                // TODO: rework update of entry
                break;
            case "4":
                try {
                    // TODO: ask user for commit message
                    synchronizer.commit("committed by eadlsync");
                } catch (Exception e) {
                    LOG.error("Error while committing changes", e);
                }
                break;
            case "00":
                System.out.println("exiting...");
                System.exit(0);
            default:
                System.out.println("No valid option");
        }
    }

}
