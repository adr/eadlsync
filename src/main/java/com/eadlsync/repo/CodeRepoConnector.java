package com.eadlsync.repo;

import java.net.MalformedURLException;

/**
 * Created by tobias on 07/03/2017.
 */
public class CodeRepoConnector {

    /**
     * Creates a new code repo implementing the methods of {@link ICodeRepo}. Depending
     * on the {@link CodeRepoType} you must either supply a valid url or file path to the repository.
     *
     * @param type as {@link CodeRepoType}
     * @param path as String
     * @return a code repo
     */
    public static ICodeRepo create(CodeRepoType type, String path) throws MalformedURLException {
        // TODO: verify path or url and throw specific exception
        switch (type) {
            case OFFLINE:
                return new OfflineCodeRepo(path);
            case ONLINE:
                return new OnlineCodeRepo(path);
        }
        return null;
    }

}
