package com.eadlsync.model.decision;

import io.github.adr.embedded.YStatementJustification;

/**
 *
 */
public class YStatementJustificationWrapperBuilder {

    private String id = "";
    private String context = "";
    private String facing = "";
    private String chosen = "";
    private String neglected = "";
    private String achieving = "";
    private String accepting = "";
    private String moreInformation = "";

    public YStatementJustificationWrapperBuilder(String id) {
        this.id = id;
    }

    public YStatementJustificationWrapperBuilder (YStatementJustificationWrapper wrapper) {
        this.id = wrapper.getId();
        this.context = wrapper.getContext();
        this.facing = wrapper.getFacing();
        this.chosen = wrapper.getChosen();
        this.neglected = wrapper.getNeglected();
        this.achieving = wrapper.getAchieving();
        this.accepting = wrapper.getAccepting();
        this.moreInformation = wrapper.getMoreInformation();
    }

    public YStatementJustificationWrapperBuilder (YStatementJustification annotation, String source) {
        this.id = annotation.id();
        this.context = annotation.context();
        this.facing = annotation.facing();
        this.chosen = annotation.chosen();
        this.neglected = annotation.neglected();
        this.achieving = annotation.achieving();
        this.accepting = annotation.accepting();
        this.moreInformation = annotation.moreInformation();
    }

    public YStatementJustificationWrapperBuilder context(String context) {
        this.context = context;
        return this;
    }

    public YStatementJustificationWrapperBuilder facing(String facing) {
        this.facing = facing;
        return this;
    }

    public YStatementJustificationWrapperBuilder chosen(String chosen) {
        this.chosen = chosen;
        return this;
    }

    public YStatementJustificationWrapperBuilder neglected(String neglected) {
        this.neglected = neglected;
        return this;
    }

    public YStatementJustificationWrapperBuilder achieving(String achieving) {
        this.achieving = achieving;
        return this;
    }

    public YStatementJustificationWrapperBuilder accepting(String accepting) {
        this.accepting = accepting;
        return this;
    }

    public YStatementJustificationWrapperBuilder moreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
        return this;
    }

    public YStatementJustificationWrapper build() {
        YStatementJustificationWrapper y = new YStatementJustificationWrapper();
        y.setId(notNull(this.id));
        y.setContext(notNull(this.context));
        y.setAccepting(notNull(this.accepting));
        y.setFacing(notNull(this.facing));
        y.setChosen(notNull(this.chosen));
        y.setNeglected(notNull(this.neglected));
        y.setAchieving(notNull(this.achieving));
        y.setMoreInformation(notNull(this.moreInformation));
        return y;
    }

    private String notNull(String value) {
        return (value == null) ? "" : value;
    }
}
