package com.eadlsync.util.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.hsr.isf.serepo.data.restinterface.commit.Commit;
import ch.hsr.isf.serepo.data.restinterface.commit.CommitContainer;
import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import ch.hsr.isf.serepo.data.restinterface.commit.CreateCommit;
import ch.hsr.isf.serepo.data.restinterface.common.User;
import ch.hsr.isf.serepo.data.restinterface.metadata.MetadataContainer;
import ch.hsr.isf.serepo.data.restinterface.metadata.MetadataEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.Relation;
import ch.hsr.isf.serepo.data.restinterface.seitem.RelationContainer;
import ch.hsr.isf.serepo.data.restinterface.seitem.RelationEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItem;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItemContainer;
import com.eadlsync.model.serepo.data.SeItemWithContent;
import com.eadlsync.util.ystatement.YStatementConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.net.UrlEscapers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.util.net.MetadataFactory.OptionState.CHOSEN;
import static com.eadlsync.util.net.MetadataFactory.ProblemState.SOLVED;

/**
 * Provides utility methods to communicate with the se-repo rest api
 */
public class SeRepoConector {

    private static final Logger LOG = LoggerFactory.getLogger(SeRepoConector.class);

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

    private static HttpResponse loggedUnirestCall(String url, Class clazz) throws UnirestException {
        try {
            return Unirest.get(url).asObject(clazz);
        } catch (UnirestException e) {
            LOG.error("Error occurred on object mapping.", e);
            throw e;
        }
    }

    public static List<SeItem> getSeItemsByUrl(String seItemsUrl) throws UnirestException {
        HttpResponse<SeItemContainer> seItemContainerResponse = loggedUnirestCall(seItemsUrl, SeItemContainer.class);
        return seItemContainerResponse.getBody().getSeItems();
    }

    public static RelationEntry getRelationEntryForUrl(String relationUrl) throws UnirestException {
        HttpResponse<RelationContainer> relationsContainerResponse = loggedUnirestCall(relationUrl, RelationContainer.class);
        return relationsContainerResponse.getBody().getEntry();
    }

    public static MetadataEntry getMetadataEntryForUrl(String relationUrl) throws UnirestException {
        HttpResponse<MetadataContainer> metadataContainerResponse = loggedUnirestCall(relationUrl, MetadataContainer.class);
        return metadataContainerResponse.getBody().getMetadata();
    }

    public static List<Commit> getCommitsByUrl(String commitsUrl) throws UnirestException {
        HttpResponse<CommitContainer> seItemContainerResponse = loggedUnirestCall(commitsUrl, CommitContainer.class);
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

    /**
     * Creates a Commit object representing a commitToBaseRepo which can be sent to the SE-Repo API.
     *
     * @param message
     * @param mode
     * @return
     */
    public static CreateCommit createCommit(String message, CommitMode mode, User user) {
        CreateCommit commit = new CreateCommit();
        commit.setMessage(message);
        commit.setMode(mode);
        commit.setUser(user);
        return commit;
    }

    /**
     * Creates an extended SE-Item Java Object which can hold content. This object is only used for simple programming purpose.
     *
     * @return
     */
    public static SeItemWithContent createSeOptionItem(String id, String achieve, String accepting, MetadataFactory.OptionState state) throws UnsupportedEncodingException {
        SeItemWithContent createSeItem = new SeItemWithContent();
        String name = getNameFromId(id);
        createSeItem.setName(name);
        String folder = getFolderFromId(id);
        createSeItem.setFolder(folder);
        createSeItem.getMetadata().putAll(MetadataFactory.getOptionMap(state));

        String html = "";
        if (state == CHOSEN) {
            html = String.format("<html><body><b>%s</b><br>%s<br><b>%s</b><br>%s<br></body></html>", YStatementConstants.SEITEM_ACHIEVING, achieve, YStatementConstants.SEITEM_ACCEPTING, accepting);
        }
        createSeItem.setContent(html.getBytes());
        createSeItem.setMimeType("text/html");
        return createSeItem;
    }

    /**
     * Creates an extended SE-Item Java Object which can hold content. This object is only used for simple programming purpose.
     *
     * @return
     */
    public static SeItemWithContent createSeProblemItem(String id, String context, String facing, MetadataFactory.ProblemState state) throws UnsupportedEncodingException {
        SeItemWithContent createSeItem = new SeItemWithContent();
        String name = getNameFromId(id);
        createSeItem.setName(name);
        String folder = getFolderFromId(id);
        createSeItem.setFolder(folder);
        createSeItem.getMetadata().putAll(MetadataFactory.getProblemMap(state));

        String html = "";
        if (state == SOLVED) {
            html = String.format("<html><body><b>%s</b><br>%s<br><b>%s</b><br>%s<br></body></html>", YStatementConstants.SEITEM_CONTEXT, context, YStatementConstants.SEITEM_FACING, facing);
        }
        createSeItem.setContent(html.getBytes());
        createSeItem.setMimeType("text/html");
        return createSeItem;
    }

    private static String getNameFromId(String id) throws UnsupportedEncodingException {
        return URLDecoder.decode(id, "UTF-8").substring(id.lastIndexOf("/") + 1).trim();
    }

    private static String getFolderFromId(String id) {
        return id.trim().substring(0, id.lastIndexOf("/") + 1);
    }

    public static String getIdFromFolderAndName(String folder, String name) {
        return folder.trim() + UrlEscapers.urlFragmentEscaper().escape(name.trim());
    }

    public static String commit(String message, List<SeItemWithContent> items, User user, CommitMode mode, String seRepoBaseUrl, String seRepoName) {
        WebTarget api = ClientBuilder.newClient()
                .target(seRepoBaseUrl)
                .path("/repos/" + seRepoName + "/commits");

        CreateCommit createCommit = createCommit(message, mode, user);
        MultipartFormDataOutput multipart = createMultipart(createCommit, items);
        Response response = api.request(MediaType.TEXT_PLAIN_TYPE)
                .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));

        String commitId;
        if (response.hasEntity()) {
            commitId = response.readEntity(String.class);
        } else {
            commitId = "";
        }
        response.close();
        LOG.debug("Committed {} items - {}", items.size(), commitId);
        items.stream().map(se -> "Item " + se.getName() + " in " + se.getFolder() + " related to " + se.getRelations().stream().map(Relation::getTarget).collect(Collectors.joining(", "))).forEach(LOG::debug);
        return commitId;
    }

    /**
     * Creates a Multipart which will be needed for the SE-Repo API call to commit SE-Items.
     *
     * @param commit
     * @param seItemsWithContent
     * @return
     */
    protected static MultipartFormDataOutput createMultipart(CreateCommit commit, List<SeItemWithContent> seItemsWithContent) {
        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        multipart.addFormData("commit", commit, MediaType.APPLICATION_JSON_TYPE);
        int partCounter = 0;
        // We create for each SE-Item a "multipart-pair" with JSON data (metadata_) and "binary" data (content_)
        for (SeItemWithContent seItem : seItemsWithContent) {
            multipart.addFormData("metadata_" + partCounter, seItem, MediaType.APPLICATION_JSON_TYPE);
            multipart.addFormData("content_" + partCounter, seItem.getContent(), MediaType.valueOf(seItem.getMimeType()), seItem.getName());
            partCounter++;
        }

        return multipart;
    }


}
