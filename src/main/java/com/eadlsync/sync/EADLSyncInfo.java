package com.eadlsync.sync;

/**
 * Info class which holds information about version, license, usage and more
 * <p>
 * Created by Tobias on 28.04.2017.
 */
public class EADLSyncInfo {

    public static final String VERSION = "1.0-SNAPSHOT";
    public static final String LICENSE = "MIT";
    public static final String AUTHOR = "Tobias Boceck";
    public static final String AUTHOR_EMAIL = "boceckts@gmail.com";
    public static final String DESCRIPTION = "Program to sync embedded architectural decisions with "
            + "decisions in a se-repo";
    public static final String USAGE = "TBD";

    public static final String PROGRAM_NAME = "eADL-Synchronizer";
    public static final String PROGRAM_EMAIL = "eadl@sync.com";

    public static String getCompleteInfoString() {
        return String.format("Version: %s\nLicense: %s\nAuthor: %s<%s>\nDescription: %s\nUsage: %s",
                VERSION, LICENSE, AUTHOR, AUTHOR_EMAIL, DESCRIPTION, USAGE);
    }

}
