package com.eadlsync.eadl.annotations;

import java.lang.annotation.Annotation;

/**
 * Created by Tobias on 03.04.2017.
 */
public class YStatementJustificationBuilder {

    private String id = "";
    private String context = "";
    private String facing = "";
    private String chosen = "";
    private String neglected = "";
    private String achieving = "";
    private String accepting = "";
    private String moreInformation = "";

    public YStatementJustificationBuilder(String id) {
        this.id = id;
    }

    public YStatementJustificationBuilder context(String context) {
        this.context = context;
        return this;
    }

    public YStatementJustificationBuilder facing(String facing) {
        this.facing = facing;
        return this;
    }

    public YStatementJustificationBuilder chosen(String chosen) {
        this.chosen = chosen;
        return this;
    }

    public YStatementJustificationBuilder neglected(String neglected) {
        this.neglected = neglected;
        return this;
    }

    public YStatementJustificationBuilder achieving(String achieving) {
        this.achieving = achieving;
        return this;
    }

    public YStatementJustificationBuilder accepting(String accepting) {
        this.accepting = accepting;
        return this;
    }

    public YStatementJustificationBuilder moreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
        return this;
    }

    public YStatementJustification build() {
        YStatementJustification y = new YStatementJustification() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return YStatementJustification.class;
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public String context() {
                return context;
            }

            @Override
            public String facing() {
                return facing;
            }

            @Override
            public String chosen() {
                return chosen;
            }

            @Override
            public String neglected() {
                return neglected;
            }

            @Override
            public String achieving() {
                return achieving;
            }

            @Override
            public String accepting() {
                return accepting;
            }

            @Override
            public String moreInformation() {
                return moreInformation;
            }
        };
        return y;
    }
}
