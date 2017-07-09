package com.eadlsync.net.serepo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import ch.hsr.isf.serepo.data.restinterface.repository.Repository;
import ch.hsr.isf.serepo.data.restinterface.repository.RepositoryContainer;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.eadlsync.data.TestDataProvider.TEST_REPO;
import static com.eadlsync.data.TestDataProvider.createTestSeItemsWithContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class SeRepoTestServerTest {

    private static final SeRepoTestServer seRepoTestServer = new SeRepoTestServer();

    @BeforeClass
    public static void classSetUp() throws Exception {
        seRepoTestServer.start();
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        seRepoTestServer.stop();
    }

    @Before
    public void methodSetUp() throws UnsupportedEncodingException, UnirestException {
        seRepoTestServer.createRepository();
        seRepoTestServer.createCommit(createTestSeItemsWithContent(), CommitMode.ADD_UPDATE);
    }

    @After
    public void methodTearDown() {
        seRepoTestServer.deleteRepository();
    }

    @Test
    public void testIsServerRunning() throws IOException, UnirestException {
        HttpResponse<RepositoryContainer> response = Unirest.get(seRepoTestServer.LOCALHOST_REPOS)
                .asObject(RepositoryContainer.class);
        RepositoryContainer repos = response.getBody();

        assertEquals(seRepoTestServer.LOCALHOST_REPOS, repos.getId().toString());
    }

    @Test
    public void testIsTestRepositoryAvailable() throws IOException, UnirestException {
        HttpResponse<RepositoryContainer> response = Unirest.get(seRepoTestServer.LOCALHOST_REPOS)
                .asObject(RepositoryContainer.class);
        RepositoryContainer repos = response.getBody();

        assertTrue(repos.getRepositories().stream().map(Repository::getName).collect(Collectors.toList
                ()).contains(TEST_REPO));
    }
}
