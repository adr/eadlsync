package com.eadlsync.model.repo;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.net.APIConnector;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by Tobias on 28.04.2017.
 */
public class SeRepo extends ARepo {

    private final String repositoryBaseUrl;
    private final String repositoryProjectName;
    private final String repositoryCommitId;
    private final String repositoryUrl;
//    add ids of changed decisions to a list and only consider these y-statements when committing
    private final ListProperty<String> changedDecisions = new SimpleListProperty<>(FXCollections.observableArrayList());

    public SeRepo(String repositoryBaseUrl, String repositoryProjectName, String repositoryCommitId)
            throws UnirestException {
        this.repositoryBaseUrl = repositoryBaseUrl;
        this.repositoryProjectName = repositoryProjectName;
        this.repositoryCommitId = repositoryCommitId;
        this.repositoryUrl = String.format("%s/repos/%s/commits/%s/seitems", this.repositoryBaseUrl,
                this.repositoryProjectName, this.repositoryCommitId);
        loadEadsFromSeRepo();
    }

    private void loadEadsFromSeRepo() throws UnirestException {
        yStatements.clear();
        yStatements.addAll(APIConnector.getYStatementJustifications(this.repositoryUrl));
    }

    /**
     * For a se-repo this will create a commitToBaseRepo for the changed decisions and try to commitToBaseRepo it to the
     * restful api of the se-repo. It should be manually called and not right after any field of an
     * embedded architectural decision is updated.
     *
     * @param message for the commitToBaseRepo
     * @throws Exception
     */
    @Override
    public String commit(String message) throws Exception {
        // we need to commitToBaseRepo all se items to not only have our changes reflected in the se-repo
        // idea:
        //  1. request se-items again (or store them in the API-Connector?)
        //  1.1. optional: filter y-statements to only include the ones which actually have changed
        //  2. map y-statements to se-items and set the new fields
        //  3. commitToBaseRepo all se-items
        return APIConnector.commitYStatement(yStatements.get(), repositoryUrl, repositoryBaseUrl, repositoryProjectName, message);
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
