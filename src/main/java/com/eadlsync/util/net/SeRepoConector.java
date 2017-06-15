package com.eadlsync.util.net;

import ch.hsr.isf.serepo.data.restinterface.commit.Commit;
import ch.hsr.isf.serepo.data.restinterface.commit.CommitContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class SeRepoConector {

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

    public static List<Commit> getCommitsByUrl(String commitsUrl) throws UnirestException {
        HttpResponse<CommitContainer> seItemContainerResponse = Unirest.get(commitsUrl).asObject
                (CommitContainer.class);
        return seItemContainerResponse.getBody().getCommits();
    }

    public static String getLatestCommit(String commitsUrl) throws UnirestException {
        List<Commit> commits = getCommitsByUrl(commitsUrl);
        commits.sort(Comparator.comparing(Commit::getWhen).reversed());
        return getCommitIdFromCommit(commits.get(0));
    }

    private static String getCommitIdFromCommit(Commit commit) {
        String id = commit.getId().toString();
        return id.substring(id.lastIndexOf("/") + 1);
    }
}
