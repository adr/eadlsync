package com.eadlsync.data;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;

/**
 *
 */
public abstract class YStatementTestData {

    protected YStatementJustificationWrapper baseDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id").
                    context("context").
                    facing("facing").
                    chosen("test/folder/chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("achieving").
                    accepting("accepting").
                    build();

    protected YStatementJustificationWrapper differentBaseDecision =
            new YStatementJustificationWrapperBuilder("test/folder/different_id").
                    context("different context").
                    facing("different facing").
                    chosen("test/folder/different_chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two").
                    achieving("different achieving").
                    accepting("different accepting").
                    moreInformation("different more information").
                    build();

    protected YStatementJustificationWrapper someDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id").
                    context("some context").
                    facing("facing").
                    chosen("test/folder/some_chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("some achieving").
                    accepting("some accepting").
                    build();

    protected YStatementJustificationWrapper someNonConflictingDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id").
                    context("context").
                    facing("other facing").
                    chosen("test/folder/chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("achieving").
                    accepting("accepting").
                    build();

    protected YStatementJustificationWrapper someConflictingOtherDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id").
                    context("other context").
                    facing("other facing").
                    chosen("test/folder/other_chosen").
                    neglected("test/folder/neglected_other_one, test/folder/neglected_other_two, test/folder/neglected_other_three").
                    achieving("other achieving").
                    accepting("other accepting").
                    build();

    protected YStatementJustificationWrapper mergedBaseAndSomeDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id").
                    context("some context").
                    facing("facing").
                    chosen("test/folder/some_chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("some achieving").
                    accepting("some accepting").
                    build();

    protected YStatementJustificationWrapper mergedBaseAndSomeAndSomeNonConflictingDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id").
                    context("some context").
                    facing("other facing").
                    chosen("test/folder/some_chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("some achieving").
                    accepting("some accepting").
                    build();

    protected YStatementJustificationWrapper clone(YStatementJustificationWrapper wrapper) {
        return new YStatementJustificationWrapperBuilder(wrapper).build();
    }
}
