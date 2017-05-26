package com.eadlsync.sync;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import com.eadlsync.model.decision.YStatementJustificationComparisionObject;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.repo.CodeRepo;
import com.eadlsync.model.repo.IRepo;
import com.eadlsync.model.repo.SeRepo;
import com.eadlsync.model.report.EADLSyncReport;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tobias on 28.04.2017.
 */
public class EADLSynchronizer implements IEADLSynchronizer {

    private final Logger LOG = LoggerFactory.getLogger(EADLSynchronizer.class);
    private final ListProperty<YStatementJustificationWrapper> additionalYStatements;
    private final ListProperty<YStatementJustificationWrapper> obsoleteYStatements;
    private final ListProperty<YStatementJustificationComparisionObject> differentYStatements;
    private EADLSyncReport report = new EADLSyncReport();
    private IRepo codeRepo;
    private IRepo seRepo;

    public EADLSynchronizer(String codeRepoPath, String repositoryBaseUrl, String
            repositoryProjectName, String repositoryCommitId) {
        additionalYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        obsoleteYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        differentYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        try {
            codeRepo = new CodeRepo(codeRepoPath);
        } catch (IOException e) {
            LOG.error("Error while reading embedded architectural decisions from disk.", e);
        }
        try {
            seRepo = new SeRepo(repositoryBaseUrl, repositoryProjectName, repositoryCommitId);
        } catch (UnirestException e) {
            LOG.error("Error while reading se-items from se-repo.", e);
        }
        initAdditionalEads();
        initObsoleteEads();
        initDifferentEads();
        setListeners();
        bindReportToYStatements();
    }

    private void bindReportToYStatements() {
        report.bindCodeRepoYStatements(codeRepo.yStatementJustificationsProperty());
        report.bindSeRepoYStatements(seRepo.yStatementJustificationsProperty());
        report.bindAdditionalYStatements(additionalYStatementsProperty());
        report.bindObsoleteYStatements(obsoleteYStatementsProperty());
        report.bindDifferentYStatements(differentYStatementsProperty());
    }

    private void setListeners() {
        // TODO: listener which only need to check the changed items
        codeRepo.yStatementJustificationsProperty().addListener(
                (ListChangeListener<YStatementJustificationWrapper>) c -> {
            initAdditionalEads();
            initObsoleteEads();
            initDifferentEads();
        });
        seRepo.yStatementJustificationsProperty().addListener(
                (ListChangeListener<YStatementJustificationWrapper>) c -> {
            initAdditionalEads();
            initObsoleteEads();
            initDifferentEads();
        });
    }

    private void initAdditionalEads() {
        this.additionalYStatements.clear();
        updateAdditionalEads(codeRepo.yStatementJustificationsProperty());
    }

    private void updateAdditionalEads(List<YStatementJustificationWrapper> baseDecisions) {
        for (YStatementJustificationWrapper yStatementJustification : baseDecisions) {
            boolean isNotAvailable = seRepo.yStatementJustificationsProperty().stream().filter(y -> y
                    .getId().equals(yStatementJustification.getId())).collect(Collectors.toList())
                    .isEmpty();
            if (isNotAvailable) {
                this.additionalYStatements.add(yStatementJustification);
            }
        }
    }

    private void initObsoleteEads() {
        this.obsoleteYStatements.clear();
        updateObsoleteEads(seRepo.yStatementJustificationsProperty());
    }

    private void updateObsoleteEads(List<YStatementJustificationWrapper> baseDecisions) {
        for (YStatementJustificationWrapper yStatementJustification : baseDecisions) {
            boolean isNotAvailable = codeRepo.yStatementJustificationsProperty().stream().filter(y ->
                    y.getId().equals(yStatementJustification.getId())).collect(Collectors.toList())
                    .isEmpty();
            if (isNotAvailable) {
                this.obsoleteYStatements.add(yStatementJustification);
            }
        }
    }

    private void initDifferentEads() {
        this.differentYStatements.clear();
        updateDifferentEads(codeRepo.yStatementJustificationsProperty());
    }

    public void updateDifferentEads(List<YStatementJustificationWrapper> baseDecisions) {
        for (YStatementJustificationWrapper yStatement : baseDecisions) {
            List<YStatementJustificationWrapper> seSameYStatements = seRepo
                    .yStatementJustificationsProperty().stream().filter(y -> y.getId().equals
                            (yStatement.getId())).collect(Collectors.toList());
            if (!seSameYStatements.isEmpty()) {
                YStatementJustificationComparisionObject decisionCompareObject = new
                        YStatementJustificationComparisionObject(yStatement, seSameYStatements.get(0));
                if (decisionCompareObject.hasSameObjectWithDifferentFields()) {
                    this.differentYStatements.add(decisionCompareObject);
                }
            }
        }
    }

    @Override
    public ListProperty<YStatementJustificationWrapper> additionalYStatementsProperty() {
        return this.additionalYStatements;
    }

    @Override
    public ListProperty<YStatementJustificationWrapper> obsoleteYStatementsProperty() {
        return this.obsoleteYStatements;
    }

    @Override
    public ListProperty<YStatementJustificationComparisionObject> differentYStatementsProperty() {
        return this.differentYStatements;
    }

    @Override
    public void updateYStatementInCodeRepo(String id) {
        List<YStatementJustificationWrapper> wrappers = seRepo.yStatementJustificationsProperty()
                .stream().filter(y -> id.equals(y.getId())).collect(Collectors.toList());
        if (!wrappers.isEmpty()) {
            codeRepo.updateDecision(wrappers.get(0));
        }
    }

    @Override
    public void updateYStatementInSeRepo(String id) {
        List<YStatementJustificationWrapper> wrappers = codeRepo.yStatementJustificationsProperty()
                .stream().filter(y -> id.equals(y.getId())).collect(Collectors.toList());
        if (!wrappers.isEmpty()) {
            seRepo.updateDecision(wrappers.get(0));
        }
    }

    @Override
    public String commitToBaseRepo(String message) throws Exception {
        return codeRepo.commit(message);
    }

    @Override
    public String commitToRemoteRepo(String message) throws Exception {
        return seRepo.commit(message);
    }

    @Override
    public EADLSyncReport getEadlSyncReport() {
        return this.report;
    }

    @Override
    public void reinitialize() throws Exception {
        this.codeRepo.reloadEADs();
        this.seRepo.reloadEADs();
    }

}
