package com.eadlsync.net.serepo;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import ch.hsr.isf.serepo.data.restinterface.common.User;
import ch.hsr.isf.serepo.data.restinterface.repository.CreateRepository;
import ch.hsr.isf.serepo.server.SeRepoServer;
import com.eadlsync.model.serepo.data.SeItemWithContent;
import com.eadlsync.util.net.SeRepoConector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.data.TestDataProvider.TEST_REPO;

/**
 * Mocks a se-repo server and needs to be started before testing using the se-repo rest interface.
 */
public class SeRepoTestServer {

    private final int PORT = 8080;
    public final String LOCALHOST_SEREPO = String.format("http://localhost:%s/serepo", PORT);
    protected final String LOCALHOST_REPOS = String.format("%s/repos", LOCALHOST_SEREPO);
    private final User TEST_USER = new User(SeRepoTestServer.class.getName(), String.format("%s@test.com", SeRepoTestServer.class.getName()));
    private final Logger LOG = LoggerFactory.getLogger(SeRepoTestServer.class);
    private SeRepoServer server;
    public String lastCommit;


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

    public void start() throws Exception {
        server = SeRepoServer.create(PORT);
        server.start();
        LOG.debug("Server successfully started with Port {}", PORT);
    }

    public void stop() throws Exception {
        server.stop();
        LOG.debug("Server successfully stopped");
    }

    public void createRepository() {
        CreateRepository createRepository = new CreateRepository();
        createRepository.setName(TEST_REPO);
        createRepository.setDescription(TEST_REPO);
        createRepository.setUser(TEST_USER);

        WebTarget api = ClientBuilder.newClient().target(LOCALHOST_SEREPO).path("/repos/");
        int status = api.request().post(Entity.entity(createRepository, MediaType.APPLICATION_JSON_TYPE)).getStatus();
        LOG.debug("Creating repository {} ({})", TEST_REPO, status);
    }

    public void deleteRepository() {
        WebTarget api = ClientBuilder.newClient().target(LOCALHOST_SEREPO).path(String.format("/repos/%s", TEST_REPO));
        int status = api.request().delete().getStatus();
        LOG.debug("Deleting repository {} ({})", TEST_REPO, status);
    }

    public String createCommit(List<SeItemWithContent> testData, CommitMode mode) {
        lastCommit = SeRepoConector.commit("test commit " + testData.hashCode(), testData, TEST_USER, mode, LOCALHOST_SEREPO, TEST_REPO);
        return lastCommit;
    }

}
