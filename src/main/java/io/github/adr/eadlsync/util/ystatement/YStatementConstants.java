package io.github.adr.eadlsync.util.ystatement;

import java.util.regex.Pattern;

/**
 *
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
    public static final String SEREPO_SEITEM_METADATA_FROM_ID = "%s?metadata";
    public static final String SEREPO_SEITEM_RELATIONS_FROM_ID = "%s?relations";

    public static final Pattern COMMIT_ID_PATTERN = Pattern.compile("[0-9a-f]{40}");

}
