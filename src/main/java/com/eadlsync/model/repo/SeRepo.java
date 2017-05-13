package com.eadlsync.model.repo;

import javafx.beans.property.ListProperty;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.net.APIConnector;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by Tobias on 28.04.2017.
 */
public class SeRepo extends ARepo {

    private final String repositoryUrl;

    public SeRepo(String url) throws UnirestException {
        this.repositoryUrl = url;
        loadEadsFromSeRepo();
    }

    private void loadEadsFromSeRepo() throws UnirestException {
        yStatements.clear();
        yStatements.addAll(APIConnector.getYStatementJustifications(this.repositoryUrl));
    }

    /**
     * For a se-repo this will create a commit for the changed decisions and try to commit it to the
     * restful api of the se-repo. It should be manually called and not right after any field of an
     * embedded architectural decision is updated.
     *
     * @throws Exception
     */
    @Override
    public void commit() throws Exception {
//        SeItem seItem = new SeItem();
//        User user = new User("eADL-Sync", "eadl@sync.com");
//        seItem.setAuthor(user);
//
    }

    @Override
    public void reloadEADs() throws UnirestException {
        loadEadsFromSeRepo();
    }

    @Override
    public ListProperty<YStatementJustificationWrapper> yStatementJustificationsProperty() {
        return this.yStatements;
    }
}
