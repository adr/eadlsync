package com.eadlsync.net;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Tobias on 04.02.2017.
 */
public class MockedAPITest extends MockedAPI{

    @Test
    public void testSEReposResponse() throws UnirestException, IOException {
        String mockedServerPath = "/__files/serepo_repos.json";
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get(SEREPO_REPOS_URL).asJson().getRawBody())));
    }

    @Test
    public void testSETestRepoResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s.json", SEREPO_NAME);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get(TEST_REPO_URL).asJson().getRawBody())));
    }

    @Test
    public void testSETestRepoCommitsResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits.json", SEREPO_NAME);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get(TEST_REPO_COMMITS_URL).asJson().getRawBody())));
    }

    @Test
    public void testSETestCommitResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits_%s.json", SEREPO_NAME, SEREPO_COMMIT_ID);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get(TEST_COMMIT_URL).asJson().getRawBody())));
    }

    @Test
    public void testSEItemsResponse() throws UnirestException, IOException {
        String mockedServerPath = String.format("/__files/serepo_repos_%s_commits_%s_seitems.json", SEREPO_NAME, SEREPO_COMMIT_ID);
        assertTrue(contentEquals(MockedAPITest.class.getResourceAsStream(mockedServerPath), (Unirest.get(TEST_SEITEMS_URL).asJson().getRawBody())));
    }

    @Ignore
    @Test
    public void testErrorForNonMockedAPICalls() {
        // TODO: test for api calls that are not being mocked
    }

    /**
     * code from www.java2s.com/Code/Java/File-Input-Output/ComparethecontentsoftwoStreamstodetermineiftheyareequalornot.htm
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
