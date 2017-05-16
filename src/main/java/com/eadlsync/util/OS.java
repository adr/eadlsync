package com.eadlsync.util;

/**
 * Created by Tobias on 16.05.2017.
 */
public class OS {

    public static final String WINDOWS = "win";
    public static final String LINUX = "mac";
    public static final String MACOS = "lin";
    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().startsWith(WINDOWS);
    public static final boolean IS_LINUX = System.getProperty("os.name").toLowerCase().startsWith(LINUX);
    public static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().startsWith(MACOS);
    public static final String FS = System.getProperty("file.separator");
    public static final String LS = System.getProperty("line.separator");

}
