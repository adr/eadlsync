package com.eadlsync.cli;

import java.util.Arrays;

import com.eadlsync.EADLSyncMain;
import com.eadlsync.sync.IEADLSynchronizer;

/**
 * Created by Tobias on 23.04.2017.
 */
public class SyncMenu extends ACLIMenu {

    private static final SyncMenu instance = new SyncMenu();

    private SyncMenu() {
        super("Sync Menu");
        setMenuItems(Arrays.asList(new CLIMenuItem("1", "print additional eads"), new CLIMenuItem("2", "print " +
                "obsolete eads"), new CLIMenuItem("3", "update ead in code repo", Arrays.asList("id")), new
                CLIMenuItem("4", "update ead in se-repo", Arrays.asList("id")), new CLIMenuItem("0", "back"), new
                CLIMenuItem("00", "exit")));
        bindLoop(option.isNotEqualTo("0").or(option.isNotEqualTo("00")));
    }

    protected static SyncMenu getInstance() {
        return instance;
    }

    @Override
    protected void evaluate(String option) {
        IEADLSynchronizer synchronizer = EADLSyncMain.getSynchronizer();
        String id = "";
        switch (option) {
            case "0":
                MainMenu.getInstance().show();
                break;
            case "1":
                System.out.println(synchronizer.additionalYStatementsProperty());
                break;
            case "2":
                System.out.println(synchronizer.obsoleteYStatementsProperty());
                break;
            case "3":
                // TODO: use cli parser for argument
                // TODO: rework update of entry
                break;
            case "4":
                // TODO: use cli parser for argument
                // TODO: rework update of entry
                break;
            case "00":
                System.out.println("exiting...");
                System.exit(0);
            default:
                System.out.println("No valid option");
        }
    }

}
