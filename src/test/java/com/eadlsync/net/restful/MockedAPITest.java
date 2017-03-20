package com.eadlsync.net.restful;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tobias on 04.02.2017.
 */
public class MockedAPITest extends MockedAPI {

    private Logger LOG = LoggerFactory.getLogger(MockedAPITest.class);

    @Test
    public void testSEReposResponse() throws UnirestException, IOException {
        String mockedServerPath = "/__files/serepo_repos.json";
        LOG.info("test repositories on {}", SEREPO_REPOS_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (SEREPO_REPOS_URL).asJson().getRawBody())));
    }

    @Test
    public void testSETestRepoResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s.json", SEREPO_NAME);
        LOG.info("test repository {} on {}", SEREPO_NAME, TEST_REPO_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (TEST_REPO_URL).asJson().getRawBody())));
    }

    @Test
    public void testSETestRepoCommitsResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits.json", SEREPO_NAME);
        LOG.info("test commits in repository {} on {}", SEREPO_NAME, TEST_REPO_COMMITS_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (TEST_REPO_COMMITS_URL).asJson().getRawBody())));
    }

    @Test
    public void testSETestCommitResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits_%s.json", SEREPO_NAME,
                SEREPO_COMMIT_ID);
        LOG.info("test commit {} in repository {} on {}", SEREPO_COMMIT_ID, SEREPO_NAME, TEST_COMMIT_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (TEST_COMMIT_URL).asJson().getRawBody())));
    }

    @Test
    public void testSEItemsResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits_%s_seitems.json", SEREPO_NAME,
                SEREPO_COMMIT_ID);
        LOG.info("test se-items for commit {} in repository {} on {}", SEREPO_COMMIT_ID, SEREPO_NAME, TEST_SEITEMS_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (TEST_SEITEMS_URL).asJson().getRawBody())));
    }

    @Test
    public void testSeItemRelations() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits_%s_gui_relations.json",
                SEREPO_NAME, SEREPO_COMMIT_ID);
        LOG.info("test relations for single se-item for commit {} in repository {} on {}", SEREPO_COMMIT_ID,
                SEREPO_NAME, TEST_RELATIONS_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (TEST_RELATIONS_URL).asJson().getRawBody())));
    }

    @Test
    public void testSeItemMetadata() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits_%s_gui_metadata.json", SEREPO_NAME,
                SEREPO_COMMIT_ID);
        LOG.info("test metadata for single se-item for commit {} in repository {} on {}", SEREPO_COMMIT_ID,
                SEREPO_NAME, TEST_METADATA_URL);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get
                (TEST_METADATA_URL).asJson().getRawBody())));
    }

    @Ignore
    @Test
    public void testErrorForNonMockedAPICalls() throws UnirestException {
        // TODO: mock responds with 404 while serepo responds with internal server error 500
        assertEquals(404, Unirest.get(TEST_SEITEMS_URL + "/error").asString().getStatus());
    }

    /**
     * code from www.java2s
     * .com/Code/Java/File-Input-Output/ComparethecontentsoftwoStreamstodetermineiftheyareequalornot.htm
     */
    private boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (-1 != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return (ch2 == -1);
    }

}
