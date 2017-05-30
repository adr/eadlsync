package com.eadlsync.util;

import java.util.regex.Pattern;

/**
 * Created by Tobias on 16.05.2017.
 */
public class YStatementConstants {

    public static final String DELIMITER = ",";

    public static final String ID = "id";
    public static final String CONTEXT = "context";
    public static final String FACING = "facing";
    public static final String CHOSEN = "chosen";
    public static final String NEGLECTED = "neglected";
    public static final String ACHIEVING = "achieving";
    public static final String ACCEPTING = "accepting";
    public static final String MORE_INFORMATION = "moreInformation";

    public static final String SEITEM_CONTEXT = "in the context of";
    public static final String SEITEM_FACING = "facing";
    public static final String SEITEM_ACHIEVING = "to achieve";
    public static final String SEITEM_ACCEPTING = "accepting that";

    public static final String SEREPO_URL_COMMITS = "%s/repos/%s/commits";
    public static final String SEREPO_SEITEMS = "%s/repos/%s/commits/%s/seitems";
    public static final String SEREPO_SEITEM_METADATA = "%s/repos/%s/commits/%s/seitems/%s?metadata";
    public static final String SEREPO_SEITEM_RELATIONS = "%s/repos/%s/commits/%s/seitems/%s?relations";

    public static final Pattern COMMIT_ID_PATTERN = Pattern.compile("[0-9a-f]{40}");

}
