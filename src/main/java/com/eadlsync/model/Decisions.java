package com.eadlsync.model;

import com.eadlsync.eadl.annotations.DecisionMade;
import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobias on 21/03/2017.
 */
public class Decisions {

    private List<DecisionMade> decisionsMade = new ArrayList<>();
    private List<YStatementJustification> yStatements = new ArrayList<>();

    public boolean includesSeItem(SeItem item) {
        return false;
    }
}
