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
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setContext(context);
        updateDecision(wrapper);
    }

    @Override
    public void updateFacing(String facing, String id) {
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setFacing(facing);
        updateDecision(wrapper);
    }

    @Override
    public void updateChosen(String chosen, String id) {
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setChosen(chosen);
        updateDecision(wrapper);
    }

    @Override
    public void updateNeglected(String neglected, String id) {
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setNeglected(neglected);
        updateDecision(wrapper);
    }

    @Override
    public void updateAchieving(String achieving, String id) {
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setAchieving(achieving);
        updateDecision(wrapper);
    }

    @Override
    public void updateAccepting(String accepting, String id) {
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setAccepting(accepting);
        updateDecision(wrapper);
    }

    @Override
    public void updateMoreInformation(String moreInformation, String id) {
        YStatementJustificationWrapper wrapper = getYStatementJustificationWrapperForID(id);
        wrapper.setMoreInformation(moreInformation);
        updateDecision(wrapper);
    }

    private YStatementJustificationWrapper getYStatementJustificationWrapperForID(String id) {
        return yStatements.stream().filter(y -> id.equals(y.getId())
        ).collect(Collectors.toList()).get(0);
    }

    @Override
    public void updateDecision(YStatementJustificationWrapper decision) {
        yStatements.removeIf(y -> decision.getId().equals(y.getId()));
        yStatements.add(decision);
    }

    @Override
    public ListProperty<YStatementJustificationWrapper> yStatementJustificationsProperty() {
        return this.yStatements;
    }
}
