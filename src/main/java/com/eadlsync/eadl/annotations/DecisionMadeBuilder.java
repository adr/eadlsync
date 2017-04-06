package com.eadlsync.eadl.annotations;

import java.lang.annotation.Annotation;

/**
 * Created by Tobias on 04.04.2017.
 */
public class DecisionMadeBuilder {

    private String id = "";
    private String solvedProblem = "";
    private String chosenOption = "";
    private String rationale = "";
    private String[] relatedDecisions = new String[0];

    public DecisionMadeBuilder(String id) {
        this.id = id;
    }

    public DecisionMadeBuilder solvedProblem(String solvedProblem) {
        this.solvedProblem = solvedProblem;
        return this;
    }

    public DecisionMadeBuilder chosenOption(String chosenOption) {
        this.chosenOption = chosenOption;
        return this;
    }

    public DecisionMadeBuilder rationale(String rationale) {
        this.rationale = rationale;
        return this;
    }

    public DecisionMadeBuilder relatedDecisions(String[] relatedDecisions) {
        this.relatedDecisions = relatedDecisions;
        return this;
    }

    public DecisionMade build() {
        DecisionMade decisionMade = new DecisionMade() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String id() {
                return null;
            }

            @Override
            public String solvedProblem() {
                return null;
            }

            @Override
            public String chosenOption() {
                return null;
            }

            @Override
            public String rationale() {
                return null;
            }

            @Override
            public String[] relatedDecisions() {
                return new String[0];
            }
        };
        return decisionMade;
    }
}
