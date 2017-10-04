package io.github.adr.eadlsync.util;

import io.github.adr.eadlsync.data.YStatementTestData;
import io.github.adr.eadlsync.util.ystatement.YStatementJustificationComparator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class YStatementJustificationComparatorTest extends YStatementTestData {

    @Test
    public void isContextEqual() {
        assertTrue(YStatementJustificationComparator.isContextEqual(" context   ", "context "));
    }

    @Test
    public void isFacingEqual() {
        assertTrue(YStatementJustificationComparator.isFacingEqual(" facing text  ", "facing text"));
    }

    @Test
    public void isChosenEqual() {
        assertTrue(YStatementJustificationComparator.isChosenEqual(" chosen/id   ", "chosen/id "));
    }

    @Test
    public void isNeglectedEqual() {
        assertTrue(YStatementJustificationComparator.isNeglectedEqual(" id2, some/id, id", "id,id2,   some/id"));
    }

    @Test
    public void isAchievingEqual() {
        assertTrue(YStatementJustificationComparator.isAchievingEqual(" achieving   ", "achieving"));
    }

    @Test
    public void isAcceptingEqual() {
        assertTrue(YStatementJustificationComparator.isAcceptingEqual(" accepting   ", "accepting"));
    }

    @Test
    public void isMoreInformationEqual() {
        assertTrue(YStatementJustificationComparator.isMoreInformationEqual(" more info   ", "more info"));
    }

    @Test
    public void isContextNotEqual() {
        assertFalse(YStatementJustificationComparator.isContextEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isFacingNotEqual() {
        assertFalse(YStatementJustificationComparator.isFacingEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isChosenNotEqual() {
        assertFalse(YStatementJustificationComparator.isChosenEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isNeglectedNotEqual() {
        assertFalse(YStatementJustificationComparator.isNeglectedEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isAchievingNotEqual() {
        assertFalse(YStatementJustificationComparator.isAchievingEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isAcceptingNotEqual() {
        assertFalse(YStatementJustificationComparator.isAcceptingEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isMoreInformationNotEqual() {
        assertFalse(YStatementJustificationComparator.isMoreInformationEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isEqual() {
        assertTrue(YStatementJustificationComparator.isEqual(baseDecision, baseDecision));
    }

    @Test
    public void isSame() {
        assertTrue(YStatementJustificationComparator.isSame(baseDecision, someDecision));
    }

    @Test
    public void isSameButNotEqual() {
        assertTrue(YStatementJustificationComparator.isSameButNotEqual(baseDecision, someDecision));
    }

    @Test
    public void isNotEqual() {
        assertFalse(YStatementJustificationComparator.isEqual(baseDecision, someDecision));
        assertFalse(YStatementJustificationComparator.isEqual(baseDecision, differentBaseDecision));
    }

    @Test
    public void isNotSame() {
        assertFalse(YStatementJustificationComparator.isSame(baseDecision, differentBaseDecision));
    }

}