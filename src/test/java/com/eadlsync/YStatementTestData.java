package com.eadlsync;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;

/**
 *
 */
public interface YStatementTestData {

    YStatementJustificationWrapper baseDecision =
            new YStatementJustificationWrapperBuilder("id", "remote_source").
                    context("context").
                    facing("facing").
                    chosen("chosen").
                    neglected("neglected_one, neglected_two, neglected_three").
                    achieving("achieving").
                    accepting("accepting").
                    build();

    YStatementJustificationWrapper someDecision =
            new YStatementJustificationWrapperBuilder("id", "remote_source").
                    context("some context").
                    facing("facing").
                    chosen("some_chosen").
                    neglected("neglected_one, neglected_two, neglected_three").
                    achieving("some achieving").
                    accepting("some accepting").
                    build();

    YStatementJustificationWrapper someNonConflictingDecision =
            new YStatementJustificationWrapperBuilder("id", "remote_source").
                    context("context").
                    facing("other facing").
                    chosen("chosen").
                    neglected("neglected_one, neglected_two, neglected_three").
                    achieving("achieving").
                    accepting("accepting").
                    build();

    YStatementJustificationWrapper someConflictingOtherDecision =
            new YStatementJustificationWrapperBuilder("id", "remote_source").
                    context("other context").
                    facing("other facing").
                    chosen("other_chosen").
                    neglected("neglected_other_one, neglected_other_two, neglected_other_three").
                    achieving("other achieving").
                    accepting("other accepting").
                    build();

    YStatementJustificationWrapper someNonConflictingAppliedDecision =
            new YStatementJustificationWrapperBuilder("id", "remote_source").
                    context("some context").
                    facing("some facing more candy").
                    chosen("some_chosendecision").
                    neglected("neglected_one, neglected_two, neglected_three").
                    achieving("some achieving").
                    accepting("some accepting").
                    build();
}
