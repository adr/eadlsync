package io.github.adr.eadlsync.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hsr.isf.serepo.data.restinterface.seitem.Relation;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import io.github.adr.eadlsync.model.serepo.data.SeItemWithContent;
import io.github.adr.eadlsync.util.net.MetadataFactory;
import io.github.adr.eadlsync.util.net.RelationFactory;
import io.github.adr.eadlsync.util.net.SeRepoConector;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Provides basic test data for testing the sync of eadls with se-items.
 */
public class TestDataProvider {

    public static final String TEST_REPO = "test-repo";

    public static final String TEST_DATA_FOLDER = "test/folder/";
    public static final String TEST_PROBLEM_NAME = "problem";
    public static final String TEST_OPTION_NAME = "option";
    public static final String TEST_SOME_OPTION_NAME = "some_option";
    public static final String TEST_DIFF_OPTION_NAME = "other_option";

    // used for testing against same decision
    public static final String TEST_SAME_DATA_FOLDER = "test/folder/same/";
    public static final String TEST_SAME_PROBLEM_NAME = "same_problem";
    public static final String TEST_SAME_OPTION_NAME = "same_option";
    public static final String TEST_SAME_SOME_OPTION_NAME = "same_some_option";
    public static final String TEST_SAME_OTHER_OPTION_NAME = "same_other_option";

    // used for testing against different id
    public static final String TEST_OTHER_DATA_FOLDER = "test/folder/other/";
    public static final String TEST_OTHER_PROBLEM_NAME = "other_problem";
    public static final String TEST_OTHER_OPTION_NAME = "other_option";
    public static final String TEST_OTHER_SOME_OPTION_NAME = "other_some_option";
    public static final String TEST_OTHER_OTHER_OPTION_NAME = "other_other_option";

    // used for testing against same id with changed fields
    public static final String TEST_CHANGED_DATA_FOLDER = "test/folder/changed/";
    public static final String TEST_CHANGED_PROBLEM_NAME = "changed_problem";
    public static final String TEST_CHANGED_OPTION_NAME = "changed_option";
    public static final String TEST_CHANGED_SOME_OPTION_NAME = "changed_some_option";
    public static final String TEST_CHANGED_OTHER_OPTION_NAME = "changed_other_option";

    // used for testing against removed decision
    public static final String TEST_REMOVED_DATA_FOLDER = "test/folder/removed/";
    public static final String TEST_REMOVED_PROBLEM_NAME = "removed_problem";
    public static final String TEST_REMOVED_OPTION_NAME = "removed_option";
    public static final String TEST_REMOVED_SOME_OPTION_NAME = "removed_some_option";
    public static final String TEST_REMOVED_OTHER_OPTION_NAME = "removed_other_option";

    // used for testing against added decision
    public static final String TEST_ADDED_DATA_FOLDER = "test/folder/added/";
    public static final String TEST_ADDED_PROBLEM_NAME = "added_problem";
    public static final String TEST_ADDED_OPTION_NAME = "added_option";
    public static final String TEST_ADDED_SOME_OPTION_NAME = "added_some_option";
    public static final String TEST_ADDED_OTHER_OPTION_NAME = "added_other_option";

    public static final String CONTEXT = "context";
    public static final String FACING = "facing";
    public static final String ACHIEVING = "achieving";
    public static final String ACCEPTING = "accepting";


    public static List<SeItemWithContent> getBasicDecisionsAsSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        List<SeItemWithContent> basics = new ArrayList<>();
        basics.addAll(createTestSeItemsWithContent());
        basics.addAll(createSameTestSeItemsWithContent());
        basics.addAll(createOtherTestSeItemsWithContent());
        basics.addAll(createChangedTestSeItemsWithContent());
        return basics;
    }

    public static List<SeItemWithContent> createTestSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_DATA_FOLDER + TEST_PROBLEM_NAME, RelationFactory.addressedBy(TEST_OPTION_NAME), RelationFactory.addressedBy(TEST_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_DIFF_OPTION_NAME), TEST_DATA_FOLDER + TEST_OPTION_NAME, TEST_DATA_FOLDER + TEST_SOME_OPTION_NAME, TEST_DATA_FOLDER + TEST_DIFF_OPTION_NAME);
    }

    public static List<SeItemWithContent> createSameTestSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_SAME_DATA_FOLDER + TEST_SAME_PROBLEM_NAME, RelationFactory.addressedBy(TEST_SAME_OPTION_NAME), RelationFactory.addressedBy(TEST_SAME_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_SAME_OTHER_OPTION_NAME), TEST_SAME_DATA_FOLDER + TEST_SAME_OPTION_NAME, TEST_SAME_DATA_FOLDER + TEST_SAME_SOME_OPTION_NAME, TEST_SAME_DATA_FOLDER + TEST_SAME_OTHER_OPTION_NAME);
    }

    public static List<SeItemWithContent> createOtherTestSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_OTHER_DATA_FOLDER + TEST_OTHER_PROBLEM_NAME, RelationFactory.addressedBy(TEST_OTHER_OPTION_NAME), RelationFactory.addressedBy(TEST_OTHER_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_OTHER_OTHER_OPTION_NAME), TEST_OTHER_DATA_FOLDER + TEST_OTHER_OPTION_NAME, TEST_OTHER_DATA_FOLDER + TEST_OTHER_SOME_OPTION_NAME, TEST_OTHER_DATA_FOLDER + TEST_OTHER_OTHER_OPTION_NAME);
    }

    public static List<SeItemWithContent> createChangedTestSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_PROBLEM_NAME, RelationFactory.addressedBy(TEST_CHANGED_OPTION_NAME), RelationFactory.addressedBy(TEST_CHANGED_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_CHANGED_OTHER_OPTION_NAME), TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_OPTION_NAME, TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_SOME_OPTION_NAME, TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_OTHER_OPTION_NAME);
    }

    public static List<SeItemWithContent> createChangedTestSeItemsWithContentModified() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_PROBLEM_NAME, RelationFactory.addressedBy(TEST_CHANGED_OPTION_NAME), RelationFactory.addressedBy(TEST_CHANGED_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_CHANGED_OTHER_OPTION_NAME), TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_OTHER_OPTION_NAME, TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_OPTION_NAME, TEST_CHANGED_DATA_FOLDER + TEST_CHANGED_SOME_OPTION_NAME);
    }

    public static List<SeItemWithContent> createRemovedTestSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_REMOVED_DATA_FOLDER + TEST_REMOVED_PROBLEM_NAME, RelationFactory.addressedBy(TEST_REMOVED_OPTION_NAME), RelationFactory.addressedBy(TEST_REMOVED_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_REMOVED_OTHER_OPTION_NAME), TEST_REMOVED_DATA_FOLDER + TEST_REMOVED_OPTION_NAME, TEST_REMOVED_DATA_FOLDER + TEST_REMOVED_SOME_OPTION_NAME, TEST_REMOVED_DATA_FOLDER + TEST_REMOVED_OTHER_OPTION_NAME);
    }

    public static List<SeItemWithContent> createAddedestSeItemsWithContent() throws UnsupportedEncodingException, UnirestException {
        return getSeItemsWithContent(TEST_ADDED_DATA_FOLDER + TEST_ADDED_PROBLEM_NAME, RelationFactory.addressedBy(TEST_ADDED_OPTION_NAME), RelationFactory.addressedBy(TEST_ADDED_SOME_OPTION_NAME), RelationFactory.addressedBy(TEST_ADDED_OTHER_OPTION_NAME), TEST_ADDED_DATA_FOLDER + TEST_ADDED_OPTION_NAME, TEST_ADDED_DATA_FOLDER + TEST_ADDED_SOME_OPTION_NAME, TEST_ADDED_DATA_FOLDER + TEST_ADDED_OTHER_OPTION_NAME);
    }

    private static List<SeItemWithContent> getSeItemsWithContent(String id, Relation chosenRelation, Relation neglected1Relation, Relation neglected2Relation, String chosenId, String neglectedId1, String neglectedId2) throws UnsupportedEncodingException {
        SeItemWithContent seProblemItem = SeRepoConector.createSeProblemItem(id, CONTEXT, FACING, MetadataFactory.ProblemState.SOLVED);
        seProblemItem.getRelations().add(chosenRelation);
        seProblemItem.getRelations().add(neglected1Relation);
        seProblemItem.getRelations().add(neglected2Relation);
        SeItemWithContent seChosenOption = SeRepoConector.createSeOptionItem(chosenId, ACHIEVING, ACCEPTING, MetadataFactory.OptionState.CHOSEN);
        SeItemWithContent seSomeOption = SeRepoConector.createSeOptionItem(neglectedId1, "some achieving", "some accepting", MetadataFactory.OptionState.NEGLECTED);
        SeItemWithContent seOtherOption = SeRepoConector.createSeOptionItem(neglectedId2, "other achieving", "other accepting", MetadataFactory.OptionState.NEGLECTED);
        return new ArrayList<>(Arrays.asList(seProblemItem, seChosenOption, seSomeOption, seOtherOption));
    }

    public static List<YStatementJustificationWrapper> getBasicDecisionsAsEadl() {
        List<YStatementJustificationWrapper> basics = new ArrayList<>();
        basics.add(createTestYStatementJustificationWrapper());
        basics.add(createTestSameYStatementJustificationWrapper());
        basics.add(createTestOtherYStatementJustificationWrapper());
        basics.add(createTestChangedYStatementJustificationWrapper());
        return basics;
    }

    public static YStatementJustificationWrapper createTestYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_DATA_FOLDER, TEST_PROBLEM_NAME, TEST_OPTION_NAME, TEST_SOME_OPTION_NAME, TEST_DIFF_OPTION_NAME);
    }

    public static YStatementJustificationWrapper createTestSameYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_SAME_DATA_FOLDER, TEST_SAME_PROBLEM_NAME, TEST_SAME_OPTION_NAME,TEST_SAME_SOME_OPTION_NAME, TEST_SAME_OTHER_OPTION_NAME);
    }

    public static YStatementJustificationWrapper createTestOtherYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_OTHER_DATA_FOLDER, TEST_OTHER_PROBLEM_NAME, TEST_OTHER_OPTION_NAME,TEST_OTHER_SOME_OPTION_NAME, TEST_OTHER_OTHER_OPTION_NAME);
    }

    public static YStatementJustificationWrapper createTestChangedYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_CHANGED_DATA_FOLDER, TEST_CHANGED_PROBLEM_NAME, TEST_CHANGED_OPTION_NAME,TEST_CHANGED_SOME_OPTION_NAME, TEST_CHANGED_OTHER_OPTION_NAME);
    }

    public static YStatementJustificationWrapper createTestChangedModifiedYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_CHANGED_DATA_FOLDER, TEST_CHANGED_PROBLEM_NAME, TEST_CHANGED_OTHER_OPTION_NAME, TEST_CHANGED_OPTION_NAME,TEST_CHANGED_SOME_OPTION_NAME);
    }

    public static YStatementJustificationWrapper createTestRemovedYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_REMOVED_DATA_FOLDER, TEST_REMOVED_PROBLEM_NAME, TEST_REMOVED_OPTION_NAME,TEST_REMOVED_SOME_OPTION_NAME, TEST_REMOVED_OTHER_OPTION_NAME);
    }

    public static YStatementJustificationWrapper createTestAddedYStatementJustificationWrapper() {
        return createYStatementJustificationWrapper(TEST_ADDED_DATA_FOLDER, TEST_ADDED_PROBLEM_NAME, TEST_ADDED_OPTION_NAME,TEST_ADDED_SOME_OPTION_NAME, TEST_ADDED_OTHER_OPTION_NAME);
    }

    private static YStatementJustificationWrapper createYStatementJustificationWrapper(String folder, String name, String chosen, String neglected1, String neglected2) {
        return new YStatementJustificationWrapperBuilder(folder + name)
                .context(CONTEXT)
                .facing(FACING)
                .chosen(folder + chosen)
                .neglected(String.format("%s%s, %s%s",folder, neglected1, folder, neglected2))
                .achieving(ACHIEVING)
                .accepting(ACCEPTING)
                .build();
    }

}
