package com.eadlsync.sync;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import com.eadlsync.eadl.annotations.YStatementJustificationComparator;
import com.eadlsync.eadl.annotations.YStatementJustificationWrapper;
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
    private final ListProperty<YStatementJustificationWrapper> differentYStatements;
    private EADLSyncReport report = new EADLSyncReport();
    private IRepo codeRepo;
    private IRepo seRepo;

    public EADLSynchronizer(String codeRepoPath, String seRepoUrl) {
        additionalYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        obsoleteYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        differentYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        try {
            codeRepo = new CodeRepo(codeRepoPath);
        } catch (IOException e) {
            LOG.error("Error while reading embedded architectural decisions from disk.", e);
        }
        try {
            seRepo = new SeRepo(seRepoUrl);
        } catch (UnirestException e) {
            LOG.error("Error while reading se-items from se-repo.", e);
        }
        updateAdditionalEads();
        updateObsoleteEads();
        updateDifferentEads();
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
        //        codeRepo.yStatementJustificationsProperty().addListener((ListChangeListener)(c -> {}));
        // TODO: set listener which only need to check the changed items
    }

    private void updateAdditionalEads() {
        additionalYStatements.clear();
        for (YStatementJustificationWrapper yStatementJustification : codeRepo
                .yStatementJustificationsProperty()) {
            boolean isNotAvailable = seRepo.yStatementJustificationsProperty().stream().filter(y -> y
                    .getId().equals(yStatementJustification.getId())).collect(Collectors.toList())
                    .isEmpty();
            if (isNotAvailable) {
                additionalYStatements.add(yStatementJustification);
            }
        }
    }

    private void updateObsoleteEads() {
        obsoleteYStatements.clear();
        for (YStatementJustificationWrapper yStatementJustification : seRepo
                .yStatementJustificationsProperty()) {
            boolean isNotAvailable = codeRepo.yStatementJustificationsProperty().stream().filter(y ->
                    y.getId().equals(yStatementJustification.getId())).collect(Collectors.toList())
                    .isEmpty();
            if (isNotAvailable) {
                obsoleteYStatements.add(yStatementJustification);
            }
        }
    }

    private void updateDifferentEads() {
        differentYStatements.clear();
        YStatementJustificationComparator comparator = new YStatementJustificationComparator();
        for (YStatementJustificationWrapper yStatement : codeRepo.yStatementJustificationsProperty()) {
            List<YStatementJustificationWrapper> differentYStatements = seRepo
                    .yStatementJustificationsProperty().stream().filter(y -> y.getId().equals
                            (yStatement.getId())).collect(Collectors.toList());
            if (!differentYStatements.isEmpty()) {
                YStatementJustificationWrapper differentYStatement = differentYStatements.get(0);
                if (comparator.equal(yStatement, differentYStatement)) {
                    this.differentYStatements.add(yStatement);
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
    public ListProperty<YStatementJustificationWrapper> differentYStatementsProperty() {
        return this.differentYStatements;
    }

    @Override
    public EADLSyncReport getEadlSyncReport() {
        return this.report;
    }

}
