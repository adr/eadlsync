package com.eadlsync.model.decision;

public class YStatementJustificationWrapper {

    private String id = "";
    private String context = "";
    private String facing = "";
    private String chosen = "";
    private String neglected = "";
    private String achieving = "";
    private String accepting = "";
    private String moreInformation = "";
    private final String source;

    protected YStatementJustificationWrapper(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public String getNeglected() {
        return neglected;
    }

    public void setNeglected(String neglected) {
        this.neglected = neglected;
    }

    public String getAchieving() {
        return achieving;
    }

    public void setAchieving(String achieving) {
        this.achieving = achieving;
    }

    public String getAccepting() {
        return accepting;
    }

    public void setAccepting(String accepting) {
        this.accepting = accepting;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }

    public String getSource() {
        return this.source;
    }

    @Override
    public String toString() {
        return "\nYStatementJustificationWrapper{" + "\n\tid='" + id + '\'' + "\n\tcontext='" +
                context + '\'' + "\n\tfacing='" + facing + '\'' + "\n\tchosen='" + chosen + '\'' +
                "\n\tneglected='" + neglected + '\'' + "\n\tachieving='" + achieving + '\'' +
                "\n\taccepting='" + accepting + '\'' + "\n\tmoreInformation='" + moreInformation +
                '\'' + "\n}";
    }

}
