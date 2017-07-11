package com.eadlsync.util.net;

import com.eadlsync.util.ystatement.YStatementConstants;

/**
 *
 */
public class SeRepoLinkFactory {

    public static String commitsUrl(String url, String project) {
        return String.format(YStatementConstants.SEREPO_URL_COMMITS, url, project);
    }

    public static String seItemsUrl(String url, String project, String commit) {
        return String.format(YStatementConstants.SEREPO_SEITEMS, url, project, commit);
    }

    public static String metadataUrl(String id) {
        return String.format(YStatementConstants.SEREPO_SEITEM_METADATA_FROM_ID, id);
    }

    public static String relationsUrl(String id) {
        return String.format(YStatementConstants.SEREPO_SEITEM_RELATIONS_FROM_ID, id);
    }


}
