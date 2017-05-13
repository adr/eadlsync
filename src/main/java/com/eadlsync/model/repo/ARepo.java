package com.eadlsync.model.repo;

import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import com.eadlsync.model.decision.YStatementJustificationWrapper;

/**
 * Created by Tobias on 29.04.2017.
 */
public abstract class ARepo implements IRepo {

    protected ListProperty<YStatementJustificationWrapper> yStatements = new SimpleListProperty<>
            (FXCollections.observableArrayList());

    @Override
    public void updateContext(String context, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setContext(context));
    }

    @Override
    public void updateFacing(String facing, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setFacing(facing));
    }

    @Override
    public void updateChosen(String chosen, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setChosen(chosen));
    }

    @Override
    public void updateNeglected(String neglected, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setNeglected(neglected));
    }

    @Override
    public void updateAchieving(String achieving, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setAchieving(achieving));
    }

    @Override
    public void updateAccepting(String accepting, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setAccepting(accepting));
    }

    @Override
    public void updateMoreInformation(String moreInformation, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement.setMoreInformation(moreInformation));
    }

    @Override
    public void updateDecision(YStatementJustificationWrapper decision, String id) {
        yStatements.stream().filter(yStatement -> id.equals(yStatement.getId())).collect(Collectors
                .toList()).forEach(yStatement -> yStatement = decision);
    }

    @Override
    public ListProperty<YStatementJustificationWrapper> yStatementJustificationsProperty() {
        return this.yStatements;
    }
}
