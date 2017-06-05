package com.eadlsync.diff;

import com.eadlsync.model.diff.StringDiff;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class StringDiffTest {

    final String BASE_STRING = "this is a normal sentence";

    final String ADD_START_DIFF = "a new start but still";
    final String ADD_START_DIFF_STRING = ADD_START_DIFF + BASE_STRING;
    final String ADD_END_DIFF = "with something new in the end";
    final String ADD_END_DIFF_STRING = BASE_STRING + ADD_END_DIFF;
    final String ADD_MID_DIFF = "extended";
    final String ADD_MID_DIFF_STRING = "this is a norextendedmal sentence";
    final String START_DIFF_STRING = "here is a normal sentence";
    final String START_DIFF = "here";
    final String END_DIFF_STRING = "this is a normal notebook";
    final String END_DIFF = "notebook";
    final String MID_DIFF_STRING = "this is a plural sentence";
    final String MID_DIFF = "plur";

    final String CONFLICT_START_DIFF_STRING_A = START_DIFF_STRING;
    final String CONFLICT_START_DIFF_STRING_B = ADD_START_DIFF_STRING;
    final String CONFLICT_MID_DIFF_STRING_A = MID_DIFF_STRING;
    final String CONFLICT_MID_DIFF_STRING_B = ADD_MID_DIFF_STRING;
    final String CONFLICT_END_DIFF_STRING_A = END_DIFF_STRING;
    final String CONFLICT_END_DIFF_STRING_B = ADD_END_DIFF_STRING;

    final String APPLY_NON_CONFLICT_MODIFICATIONS = "here is a normal notebook";
    final String APPLY_NON_CONFLICT_DELETIONS = "this is a plural sentence";
    final String APPLY_NON_CONFLICT_MIXED = "this is a normal sentencewith something new in the end";

    @Test
    public void testStartAddition() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, ADD_START_DIFF_STRING);
        Assert.assertEquals(ADD_START_DIFF, stringDiff.getDiffString());
    }

    @Test
    public void testEndAddition() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, ADD_END_DIFF_STRING);
        Assert.assertEquals(ADD_END_DIFF, stringDiff.getDiffString());
    }

    @Test
    public void testMiddleAddition() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, ADD_MID_DIFF_STRING);
        Assert.assertEquals(ADD_MID_DIFF, stringDiff.getDiffString());
    }

    @Test
    public void testStartDeletion() {
        StringDiff stringDiff = StringDiff.of(ADD_START_DIFF_STRING, BASE_STRING);
        Assert.assertTrue(stringDiff.getDiffString().isEmpty());
    }

    @Test
    public void testEndDeletion() {
        StringDiff stringDiff = StringDiff.of(ADD_END_DIFF_STRING, BASE_STRING);
        Assert.assertTrue(stringDiff.getDiffString().isEmpty());
    }

    @Test
    public void testMiddleDeletion() {
        StringDiff stringDiff = StringDiff.of(ADD_MID_DIFF_STRING, BASE_STRING);
        Assert.assertTrue(stringDiff.getDiffString().isEmpty());
    }

    @Test
    public void testStartModification() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, START_DIFF_STRING);
        Assert.assertEquals(START_DIFF, stringDiff.getDiffString());
    }

    @Test
    public void testEndModification() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, END_DIFF_STRING);
        Assert.assertEquals(END_DIFF, stringDiff.getDiffString());
    }

    @Test
    public void testMiddleModification() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, MID_DIFF_STRING);
        Assert.assertEquals(MID_DIFF, stringDiff.getDiffString());
    }

    @Test
    public void testStartConflictDiff() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, CONFLICT_START_DIFF_STRING_A);
        StringDiff newStringDiff = StringDiff.of(BASE_STRING, CONFLICT_START_DIFF_STRING_B);
        Assert.assertTrue(stringDiff.conflictsWith(newStringDiff));
    }

    @Test
    public void testEndConflictDiff() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, CONFLICT_END_DIFF_STRING_A);
        StringDiff newStringDiff = StringDiff.of(BASE_STRING, CONFLICT_END_DIFF_STRING_B);
        Assert.assertTrue(stringDiff.conflictsWith(newStringDiff));
    }

    @Test
    public void testMidConflictDiff() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, CONFLICT_MID_DIFF_STRING_A);
        StringDiff newStringDiff = StringDiff.of(BASE_STRING, CONFLICT_MID_DIFF_STRING_B);
        Assert.assertTrue(stringDiff.conflictsWith(newStringDiff));
    }

    @Test
    public void testNoClonflictDiff() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, START_DIFF_STRING);
        StringDiff newStringDiff = StringDiff.of(BASE_STRING, END_DIFF_STRING);
        Assert.assertFalse(stringDiff.conflictsWith(newStringDiff));
    }

    @Test
    public void testApplyNonConflictingDiffs() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, START_DIFF_STRING);
        StringDiff newStringDiff = StringDiff.of(BASE_STRING, END_DIFF_STRING);
        Assert.assertEquals(APPLY_NON_CONFLICT_MODIFICATIONS, stringDiff.applyNonConflicting(newStringDiff));
    }

    @Test
    public void testApplyNonConflictingDiffsAdditions() {
        StringDiff stringDiff = StringDiff.of(BASE_STRING, ADD_START_DIFF_STRING);
        StringDiff newStringDiff = StringDiff.of(BASE_STRING, ADD_MID_DIFF_STRING);
        Assert.assertEquals(ADD_START_DIFF + ADD_MID_DIFF_STRING, stringDiff.applyNonConflicting(newStringDiff));
    }

    @Test
    public void testApplyNonConflictingDiffsDeletions() {
        StringDiff stringDiff = StringDiff.of(ADD_START_DIFF_STRING, BASE_STRING);
        StringDiff newStringDiff = StringDiff.of(ADD_START_DIFF_STRING, MID_DIFF_STRING);
        Assert.assertEquals(APPLY_NON_CONFLICT_DELETIONS, stringDiff.applyNonConflicting(newStringDiff));
    }

    @Test
    public void testApplyNonConflictingDiffsDeletionAddition() {
        StringDiff stringDiff = StringDiff.of(ADD_START_DIFF_STRING, BASE_STRING);
        StringDiff newStringDiff = StringDiff.of(ADD_START_DIFF_STRING, ADD_END_DIFF_STRING);
        Assert.assertEquals(APPLY_NON_CONFLICT_MIXED, stringDiff.applyNonConflicting(newStringDiff));
    }

}
