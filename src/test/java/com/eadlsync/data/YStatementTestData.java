package com.eadlsync.data;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;

/**
 *
 */
public interface YStatementTestData {

    YStatementJustificationWrapper baseDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id", "remote_source").
                    context("context").
                    facing("facing").
                    chosen("test/folder/chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("achieving").
                    accepting("accepting").
                    build();

    YStatementJustificationWrapper someDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id", "remote_source").
                    context("some context").
                    facing("facing").
                    chosen("test/folder/some_chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("some achieving").
                    accepting("some accepting").
                    build();

    YStatementJustificationWrapper someNonConflictingDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id", "remote_source").
                    context("context").
                    facing("other facing").
                    chosen("test/folder/chosen").
                    neglected("test/folder/neglected_one, test/folder/neglected_two, test/folder/neglected_three").
                    achieving("achieving").
                    accepting("accepting").
                    build();

    YStatementJustificationWrapper someConflictingOtherDecision =
            new YStatementJustificationWrapperBuilder("test/folder/id", "remote_source").
                    context("other context").
                    facing("other facing").
                    chosen("test/folder/other_chosen").
                    neglected("test/folder/neglected_other_one, test/folder/neglected_other_two, test/folder/neglected_other_three").
                    achieving("other achieving").
                    accepting("other accepting").
                    build();
}
