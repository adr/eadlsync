package com.eadlsync.util;

import com.eadlsync.data.YStatementTestData;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class YStatementJustificationComparatorTest implements YStatementTestData {

    @Test
    public void isContextEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isContextEqual(" context   ", "context "));
    }

    @Test
    public void isFacingEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isFacingEqual(" facing text  ", "facing text"));
    }

    @Test
    public void isChosenEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isChosenEqual(" chosen/id   ", "chosen/id "));
    }

    @Test
    public void isNeglectedEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isNeglectedEqual(" id2, some/id, id", "id,id2,   some/id"));
    }

    @Test
    public void isAchievingEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isAchievingEqual(" achieving   ", "achieving"));
    }

    @Test
    public void isAcceptingEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isAcceptingEqual(" accepting   ", "accepting"));
    }

    @Test
    public void isMoreInformationEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isMoreInformationEqual(" more info   ", "more info"));
    }

    @Test
    public void isContextNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isContextEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isFacingNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isFacingEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isChosenNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isChosenEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isNeglectedNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isNeglectedEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isAchievingNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isAchievingEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isAcceptingNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isAcceptingEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isMoreInformationNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isMoreInformationEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isEqual(baseDecision, baseDecision));
    }

    @Test
    public void isSame() throws Exception {
        assertTrue(YStatementJustificationComparator.isSame(baseDecision, someDecision));
    }

    @Test
    public void isSameButNotEqual() throws Exception {
        assertTrue(YStatementJustificationComparator.isSameButNotEqual(baseDecision, someDecision));
    }

    @Test
    public void isNotEqual() throws Exception {
        assertFalse(YStatementJustificationComparator.isEqual(baseDecision, someDecision));
        assertFalse(YStatementJustificationComparator.isEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isNotSame() throws Exception {
        assertFalse(YStatementJustificationComparator.isSame(baseDecision, differentBaseDecision));
    }

}