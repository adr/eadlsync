package com.eadlsync;

import com.eadlsync.cli.MainMenu;

/**
 * Created by Tobias on 23.04.2017.
 */
public class EADLSyncMain {

    public static void main(String[] args) {
        // TODO: process arguments to get path/url for repo and url for se-repo
        String codeRepoPathOrUrl = null;
        String seRepoPath = null;
        initialize(codeRepoPathOrUrl, seRepoPath);

        MainMenu.getInstance().show();
    }

    public static void initialize(String codeRepoPathOrUrl, String seRepoPath) {
        // TODO: read eads from code repository and se-repo from start arguments

    }

}
