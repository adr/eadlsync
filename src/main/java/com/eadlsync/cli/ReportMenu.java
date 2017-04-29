package com.eadlsync.cli;

import java.util.Arrays;

import com.eadlsync.EADLSyncMain;
import com.eadlsync.sync.IEADLSynchronizer;

/**
 * Created by Tobias on 23.04.2017.
 */
public class ReportMenu extends ACLIMenu {

    private static final ReportMenu instance = new ReportMenu();

    private ReportMenu() {
        super("Report Menu");
        setMenuItems(Arrays.asList(new CLIMenuItem("1", "print report"), new CLIMenuItem("2", "export report to pdf")
                , new CLIMenuItem("0", "back"), new CLIMenuItem("00", "exit")));
        bindLoop(option.isNotEqualTo("0").or(option.isNotEqualTo("00")));
    }

    public static ReportMenu getInstance() {
        return instance;
    }

    @Override
    public void evaluate(String option) {
        IEADLSynchronizer synchronizer = EADLSyncMain.getSynchronizer();
        switch (option) {
            case "0":
                MainMenu.getInstance().show();
                break;
            case "1":
                System.out.println(synchronizer.getEadlSyncReport());
                break;
            case "2":
                // TODO: export as pdf
                break;
            case "00":
                System.out.println("exiting...");
                System.exit(0);
            default:
                System.out.println("No valid option");
        }
    }

}
