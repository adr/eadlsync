package com.eadlsync.util.net;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.eadlsync.serepo.data.restinterface.commit.Commit;
import com.eadlsync.serepo.data.restinterface.commit.CommitContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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

    private static CommitContainer getCommitContainerByUrl(String commitsUrl) throws UnirestException {
        HttpResponse<CommitContainer> seItemContainerResponse = Unirest.get(commitsUrl).asObject
                (CommitContainer.class);
        CommitContainer commitContainer = seItemContainerResponse.getBody();
        return commitContainer;
    }

    public static String getLatestCommit(String commitsUrl) throws UnirestException {
        List<Commit> commits = getCommitContainerByUrl(commitsUrl).getCommits();
        commits.sort(Comparator.comparing(Commit::getWhen));
        return getCommitIdFromCommit(commits.get(0));
    }

    private static String getCommitIdFromCommit(Commit commit) {
        String id = commit.getId().toString();
        return id.substring(id.lastIndexOf("/") + 1);
    }
}
