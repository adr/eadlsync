package com.eadlsync.model.report;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import com.eadlsync.eadl.annotations.YStatementJustificationWrapper;

/**
 * Created by tobias on 07/03/2017.
 */
public class EADLSyncReport {

    private final ListProperty<YStatementJustificationWrapper> codeRepoYStatements;
    private final ListProperty<YStatementJustificationWrapper> seRepoYStatements;
    private final ListProperty<YStatementJustificationWrapper> additionalYStatements;
    private final ListProperty<YStatementJustificationWrapper> obsoleteYStatements;
    private final ListProperty<YStatementJustificationWrapper> differentYStatements;

    public EADLSyncReport() {
        codeRepoYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        seRepoYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        additionalYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        obsoleteYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
        differentYStatements = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public void bindCodeRepoYStatements(ListProperty listProperty) {
        codeRepoYStatements.bind(listProperty);
    }

    public void bindSeRepoYStatements(ListProperty listProperty) {
        seRepoYStatements.bind(listProperty);
    }

    public void bindAdditionalYStatements(ListProperty listProperty) {
        additionalYStatements.bind(listProperty);
    }

    public void bindObsoleteYStatements(ListProperty listProperty) {
        obsoleteYStatements.bind(listProperty);
    }

    public void bindDifferentYStatements(ListProperty listProperty) {
        differentYStatements.bind(listProperty);
    }

    @Override
    public String toString() {
        String content = String.format("Code Repo Y Statements: %s\n" + "Se-Repo Y Statements: %s\n" +
                "Y Statements in Code Repo but not in Se-Repo: %s\n" + "Y Statements in Se-Repo but "
                + "not in Code Repo: %s\n" + "Y Statements that have different fields in the Se-Repo " +
                "than " + "in the Code Rpo: %s\n", codeRepoYStatements.get(), seRepoYStatements.get(),
                additionalYStatements.get(), obsoleteYStatements.get(), differentYStatements.get());
        return content;
    }

}
