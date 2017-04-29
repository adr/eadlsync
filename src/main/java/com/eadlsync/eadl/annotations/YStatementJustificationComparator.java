package com.eadlsync.eadl.annotations;

import java.util.Comparator;

/**
 * Created by Tobias on 29.04.2017.
 */
public class YStatementJustificationComparator implements Comparator<YStatementJustificationWrapper> {


    @Override
    public int compare(YStatementJustificationWrapper o1, YStatementJustificationWrapper o2) {
        return 0;
    }

    public boolean equal(YStatementJustificationWrapper o1, YStatementJustificationWrapper o2) {
        // TODO: more detailed comparision
        return o1.equals(o2);
    }
}
