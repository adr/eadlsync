package com.eadlsync.model.decision;

/**
 * Created by Tobias on 03.04.2017.
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
        y.setId(this.id);
        y.setContext(this.context);
        y.setAccepting(this.accepting);
        y.setFacing(this.facing);
        y.setChosen(this.chosen);
        y.setNeglected(this.neglected);
        y.setAchieving(this.achieving);
        y.setMoreInformation(this.moreInformation);
        return y;
    }
}
