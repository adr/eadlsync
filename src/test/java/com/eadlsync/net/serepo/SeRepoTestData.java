package com.eadlsync.net.serepo;

import com.eadlsync.model.serepo.data.SeItemWithContent;
import com.eadlsync.util.net.MetadataFactory;
import com.eadlsync.util.net.RelationFactory;
import com.eadlsync.util.net.SeRepoConector;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tobias on 15/06/2017.
 */
public class SeRepoTestData {

    static final String TEST_REPO = "test-repo";
    static final String TEST_DATA_FOLDER = "test/folder/";
    static final String TEST_PROBLEM_ID = "problem";
    static final String TEST_OPTION_ID = "option";
    static final String TEST_SOME_OPTION_ID = "some_option";
    static final String TEST_OTHER_OPTION_ID = "other_option";


    List<SeItemWithContent> createTestData() throws UnsupportedEncodingException, UnirestException {
        SeItemWithContent seProblemItem = SeRepoConector.createSeProblemItem(TEST_DATA_FOLDER + TEST_PROBLEM_ID, "context", "facing", MetadataFactory.ProblemState.SOLVED);
        seProblemItem.getRelations().add(RelationFactory.addressedBy("option"));
        seProblemItem.getRelations().add(RelationFactory.addressedBy("some_option"));
        seProblemItem.getRelations().add(RelationFactory.addressedBy("other_option"));
        SeItemWithContent seChosenOption = SeRepoConector.createSeOptionItem(TEST_DATA_FOLDER + TEST_OPTION_ID, "achieving", "accepting", MetadataFactory.OptionState.CHOSEN);
        SeItemWithContent seSomeOption = SeRepoConector.createSeOptionItem(TEST_DATA_FOLDER + TEST_SOME_OPTION_ID, "some achieving", "some accepting", MetadataFactory.OptionState.NEGLECTED);
        SeItemWithContent seOtherOption = SeRepoConector.createSeOptionItem(TEST_DATA_FOLDER + TEST_OTHER_OPTION_ID, "other achieving", "other accepting", MetadataFactory.OptionState.NEGLECTED);
        return Arrays.asList(seProblemItem, seChosenOption, seSomeOption, seOtherOption);
    }
}
