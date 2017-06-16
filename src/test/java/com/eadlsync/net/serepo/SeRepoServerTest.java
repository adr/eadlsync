package com.eadlsync.net.serepo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import ch.hsr.isf.serepo.data.restinterface.common.User;
import ch.hsr.isf.serepo.data.restinterface.repository.CreateRepository;
import ch.hsr.isf.serepo.data.restinterface.repository.Repository;
import ch.hsr.isf.serepo.data.restinterface.repository.RepositoryContainer;
import ch.hsr.isf.serepo.server.SeRepoServer;
import com.eadlsync.data.SeRepoTestData;
import com.eadlsync.model.serepo.data.SeItemWithContent;
import com.eadlsync.util.net.SeRepoConector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ch.hsr.isf.serepo.data.restinterface.commit.CommitMode.ADD_UPDATE_DELETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tobias on 03.06.2017.
 */
public class SeRepoServerTest extends SeRepoTestData {

    private static final int PORT = 8080;
    private static final String LOCALHOST_SEREPO = String.format("http://localhost:%s/serepo", PORT);
    private static final String LOCALHOST_REPOS = String.format("%s/repos", LOCALHOST_SEREPO);
    private static final String LOCALHOST_COMMITS = String.format("%s/%s/commits", LOCALHOST_REPOS, TEST_REPO);
    private static final User TEST_USER = new User(SeRepoServerTest.class.getName(), String.format("%s@test.com", SeRepoServerTest.class.getName()));
    private static final Logger LOG = LoggerFactory.getLogger(SeRepoServerTest.class);
    private static SeRepoServer server;
    String lastCommit;


    static {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com
                    .fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    @BeforeClass
    public static void classSetUp() throws Exception {
        server = SeRepoServer.create(PORT);
        server.start();
        LOG.debug("Server successfully started on with Port {}", PORT);
    }

    @Before
    public void methodSetUp() throws UnsupportedEncodingException, UnirestException {
        createRepository();
        lastCommit = createCommit(createTestData());
    }

    @After
    public void methodTearDown() {
        deleteRepository();
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        server.stop();
        LOG.debug("Server successfully stopped");
    }

    @Test
    public void testIsServerRunning() throws IOException, UnirestException {
        HttpResponse<RepositoryContainer> response = Unirest.get(LOCALHOST_REPOS).asObject(RepositoryContainer.class);
        RepositoryContainer repos = response.getBody();

        assertEquals(LOCALHOST_REPOS, repos.getId().toString());
    }

    @Test
    public void testIsTestRepositoryAvailable() throws IOException, UnirestException {
        HttpResponse<RepositoryContainer> response = Unirest.get(LOCALHOST_REPOS).asObject(RepositoryContainer.class);
        RepositoryContainer repos = response.getBody();

        assertTrue(repos.getRepositories().stream().map(Repository::getName).collect(Collectors.toList()).contains(TEST_REPO));
    }

    private static void createRepository() {
        CreateRepository createRepository = new CreateRepository();
        createRepository.setName(TEST_REPO);
        createRepository.setDescription(TEST_REPO);
        createRepository.setUser(TEST_USER);

        WebTarget api = ClientBuilder.newClient().target(LOCALHOST_SEREPO).path("/repos/");
        int status = api.request().post(Entity.entity(createRepository, MediaType.APPLICATION_JSON_TYPE)).getStatus();
        LOG.debug("Creating repository {} ({})", TEST_REPO, status);
    }

    private void deleteRepository() {
        WebTarget api = ClientBuilder.newClient().target(LOCALHOST_SEREPO).path(String.format("/repos/%s", TEST_REPO));
        int status = api.request().delete().getStatus();
        LOG.debug("Deleting repository {} ({})", TEST_REPO, status);
    }

    private String createCommit(List<SeItemWithContent> testData) {
        return SeRepoConector.commit("test commit", testData, TEST_USER, ADD_UPDATE_DELETE, LOCALHOST_SEREPO, TEST_REPO);
    }

}
