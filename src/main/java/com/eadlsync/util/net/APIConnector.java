package com.eadlsync.util.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.collections.ObservableList;

import ch.hsr.isf.serepo.data.restinterface.commit.Commit;
import ch.hsr.isf.serepo.data.restinterface.commit.CommitContainer;
import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import ch.hsr.isf.serepo.data.restinterface.commit.CreateCommit;
import ch.hsr.isf.serepo.data.restinterface.common.Link;
import ch.hsr.isf.serepo.data.restinterface.common.User;
import ch.hsr.isf.serepo.data.restinterface.metadata.MetadataContainer;
import ch.hsr.isf.serepo.data.restinterface.metadata.MetadataEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.RelationContainer;
import ch.hsr.isf.serepo.data.restinterface.seitem.RelationEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItem;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItemContainer;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.model.serepo.data.SeItemWithContent;
import com.eadlsync.util.YStatementConstants;
import com.eadlsync.util.net.MetadataFactory.OptionState;
import com.eadlsync.util.net.MetadataFactory.ProblemState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.net.UrlEscapers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.util.YStatementConstants.DELIMITER;
import static com.eadlsync.util.net.MetadataFactory.GeneralMetadata.STEREOTYPE;
import static com.eadlsync.util.net.MetadataFactory.GeneralMetadata.TAGGED_VALUES;
import static com.eadlsync.util.net.MetadataFactory.OptionState.CHOSEN;
import static com.eadlsync.util.net.MetadataFactory.ProblemState.SOLVED;
import static com.eadlsync.util.net.MetadataFactory.Stereotype.PROBLEM_OCCURRENCE;
import static com.eadlsync.util.net.MetadataFactory.TaggedValues.OPTION_STATE;
import static com.eadlsync.util.net.MetadataFactory.TaggedValues.PROBLEM_STATE;
import static com.eadlsync.util.net.RelationFactory.ADMentorRelationType.ADDRESSED_BY;
import static com.eadlsync.util.net.SeRepoRelationType.SEREPO_METADATA;
import static com.eadlsync.util.net.SeRepoRelationType.SEREPO_RELATIONS;

/**
 * Provides utility methods to communicate with the se-repo restful api
 * <p>
 * Created by Tobias on 31.01.2017.
 */
public class APIConnector {

    private final static Logger LOG = LoggerFactory.getLogger(APIConnector.class);

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

    public static CommitContainer getCommitContainerByUrl(String url) throws UnirestException {
        HttpResponse<CommitContainer> seItemContainerResponse = Unirest.get(url).asObject
                (CommitContainer.class);
        CommitContainer commitContainer = seItemContainerResponse.getBody();
        return commitContainer;
    }

    public static List<Commit> getCommitsByUrl(String url) throws UnirestException {
        return getCommitContainerByUrl(url).getCommits();
    }

    public static String getCommitIdFromCommit(Commit commit) {
        String id = commit.getId().toString();
        return id.substring(id.lastIndexOf("/") + 1);
    }

    public static ch.hsr.isf.serepo.data.restinterface.seitem.SeItemContainer getSeItemContainerByUrl(String url) throws UnirestException {
        HttpResponse<SeItemContainer> seItemContainerResponse = Unirest.get(url).asObject
                (SeItemContainer.class);
        SeItemContainer seItemContainer = seItemContainerResponse.getBody();
        return seItemContainer;
    }

    private static MetadataContainer getMetadataContainerForSeItem(SeItem item) throws UnirestException {
        Link metadataLink = item.getLinks().stream().filter(link -> SEREPO_METADATA.getRelation().
                equals(link.getRel())).collect(Collectors.toList()).get(0);
        HttpResponse<MetadataContainer> seItemContainerResponse = Unirest.get(metadataLink.getHref()).
                asObject(MetadataContainer.class);
        MetadataContainer metadataContainer = seItemContainerResponse.getBody();
        return metadataContainer;
    }

    public static MetadataEntry getMetadataEntry(SeItem item) throws UnirestException {
        return getMetadataContainerForSeItem(item).getMetadata();
    }

    private static RelationContainer getRelationContainerForSeItem(SeItem item) throws UnirestException {
        Link relationsLink = item.getLinks().stream().filter(link -> SEREPO_RELATIONS.getRelation().
                equals(link.getRel())).collect(Collectors.toList()).get(0);
        HttpResponse<RelationContainer> seItemContainerResponse = Unirest.get(relationsLink.getHref()).
                asObject(RelationContainer.class);
        RelationContainer relationsContainer = seItemContainerResponse.getBody();
        return relationsContainer;
    }

    public static RelationEntry getRelationEntry(SeItem item) throws UnirestException {
        return getRelationContainerForSeItem(item).getEntry();
    }

    public static List<YStatementJustificationWrapper> getYStatementJustifications(String url) throws
            UnirestException {
        SeItemContainer container = getSeItemContainerByUrl(url);

        // find all se-items that are problem occurrences
        List<SeItem> problemItems = new ArrayList<>();
        for (SeItem item : container.getSeItems()) {
            MetadataEntry metadata = getMetadataEntry(item);
            Object state = metadata.getMap().get(STEREOTYPE.getName());
            if (PROBLEM_OCCURRENCE.getName().equals(state)) {
                state = metadata.getMap().get(TAGGED_VALUES.getName());
                boolean solved = SOLVED.getName().equals(((Map<String, String>) state).get(PROBLEM_STATE.getName()));
                if (solved) {
                    // only add problems which are solved, this means if a neglected option raises
                    // another problem we do not want that problem to appear in our decisions
                    problemItems.add(item);
                }
            }
        }

        List<YStatementJustificationWrapper> yStatementJustifications = new ArrayList<>();

        // iterate over the problem occurrences
        for (SeItem problemItem : problemItems) {
            RelationEntry relation = getRelationEntry(problemItem);
            List<Link> relationLinks = relation.getLinks().stream().filter(link -> ADDRESSED_BY.getName()
                    .equals(link.getTitle())).collect(Collectors.toList());

            // find the chosen option item for the current problem occurrence
            SeItem chosenOptionItem = null;
            List<String> neglectedIds = new ArrayList<>();
            for (Link link : relationLinks) {
                SeItem relationSeItem = container.getSeItems().stream().filter(seItem -> seItem.getId().
                        toString().equals(link.getHref())).collect(Collectors.toList()).get(0);

                MetadataEntry entry = getMetadataEntry(relationSeItem);
                Map<String, String> taggedValues = (Map<String, String>) entry.getMap().get(TAGGED_VALUES.getName());
                Object state = taggedValues.get(OPTION_STATE.getName());
                if (CHOSEN.getName().equals(state)) {
                    chosenOptionItem = container.getSeItems().stream().filter(seItem -> seItem
                            .getId().toString().equals(link.getHref())).collect(Collectors.toList()).get(0);
                } else {
                    neglectedIds.add(getId(relationSeItem.getFolder(), relationSeItem.getName()));
                }

            }

            // create a new YStatementJustification object
            YStatementJustificationWrapper yStatementJustification = createYStatementJustification
                    (problemItem, chosenOptionItem, neglectedIds);
            yStatementJustifications.add(yStatementJustification);

        }

        return yStatementJustifications;
    }

    private static YStatementJustificationWrapper createYStatementJustification(SeItem problemItem,
                                                                                SeItem chosenOptionItem,
                                                                                List<String> neglected) {
        Element problemBody = getSeItemContentBody(problemItem);
        String id = getId(problemItem.getFolder(), problemItem.getName());
        String context = parseForContent(YStatementConstants.SEITEM_CONTEXT, problemBody);
        String facing = parseForContent(YStatementConstants.SEITEM_FACING, problemBody);
        String neglectedIds = neglected.stream().collect(Collectors.joining(DELIMITER));
        String source = problemItem.getId().toString();

        if (chosenOptionItem != null) {
            Element optionBody = getSeItemContentBody(chosenOptionItem);
            String chosen = getId(chosenOptionItem.getFolder(), chosenOptionItem.getName());
            String achieving = parseForContent(YStatementConstants.SEITEM_ACHIEVING, optionBody);
            String accepting = parseForContent(YStatementConstants.SEITEM_ACCEPTING, optionBody);
            return new YStatementJustificationWrapperBuilder(id, source).context(context).facing(facing)
                    .chosen(chosen).neglected(neglectedIds).achieving(achieving).accepting(accepting)
                    .build();
        } else {
            return new YStatementJustificationWrapperBuilder(id, source).context(context).facing(facing).
                    neglected(neglectedIds).build();
        }
    }

    private static String parseForContent(String key, Element seItemBody) {
        String content = seItemBody.outerHtml();

        // check if key is found and remove everything in front
        int keyOccurrence = content.toLowerCase().indexOf(key);
        if (keyOccurrence == -1) {
            return "";
        } else {
            content = content.substring(keyOccurrence + key.length());
        }

        // check if line break is found and remove everything in front
        // assumption: content is right after the key and on new line
        int firstLineBreak = content.indexOf("<br>");
        if (firstLineBreak == -1) {
            return "";
        } else {
            content = content.substring(firstLineBreak + 4);
        }
        content = content.replaceAll("\r", "");
        content = content.replaceAll("\n", "");

        // only read until the next line break
        Pattern patternLineBreak = Pattern.compile("(.+?)<br>.*");
        Matcher matcherLineBreak = patternLineBreak.matcher(content);
        // if no line break tag is found just read until the next html tag occurs
        Pattern patternTag = Pattern.compile("(.+?)<.*>.*");
        Matcher matcherTag = patternTag.matcher(content);

        if (matcherLineBreak.find()) {
            content = matcherLineBreak.group(1);
        } else if (matcherTag.find()) {
            content = matcherTag.group(1);
        } else {
            LOG.info("No end html tag for key '{}' in [{}] parsed from [{}]", key, content, seItemBody.outerHtml());
        }

        content = content.trim();
        return content;
    }

    private static Element getSeItemContentBody(SeItem item) {
        Document doc;
        Element body = null;
        try {
            String decodedURL = URLDecoder.decode(item.getId().toString(), "UTF-8");
            doc = Jsoup.connect(decodedURL).get();
            body = doc.body();
        } catch (IOException e) {
            LOG.debug("Error parsing se-item '{}'", item);
        }
        return body;
    }

    public static String commitYStatement(ObservableList<YStatementJustificationWrapper> yStatementJustificationWrappers, String repositoryUrl, String repositoryBaseUrl, String repositoryProjectName, String message) throws UnirestException, UnsupportedEncodingException {
        Set<SeItemWithContent> allSeItems = new HashSet<>();

        for (YStatementJustificationWrapper wrapper : yStatementJustificationWrappers) {
            List<SeItemWithContent> addressedByItems = new ArrayList<>();

            // add the chosen option to the set
            SeItemWithContent chosenItem = createSeOptionItem(wrapper.getChosen(), wrapper.getAchieving(), wrapper.getAccepting(),
                    CHOSEN);
            addressedByItems.add(chosenItem);
            allSeItems.add(chosenItem);

            // add neglected options to the set
            String[] neglectedOptions = wrapper.getNeglected().split(DELIMITER);
            for (String id : neglectedOptions) {
                SeItemWithContent neglectedItem = createSeOptionItem(id, "", "", OptionState.NEGLECTED);
                allSeItems.add(neglectedItem);
                addressedByItems.add(neglectedItem);
            }

            // create problem item
            SeItemWithContent problem = createSeProblemItem(wrapper.getId(), wrapper
                    .getContext(), wrapper.getFacing(), SOLVED);

            // set the relations for the problem item
            for (SeItemWithContent seItem : addressedByItems) {
                String encodedName = UrlEscapers.urlFragmentEscaper().escape(seItem.getName());
                problem.getRelations().add(RelationFactory.addressedBy(encodedName));
            }

            // add the problem to the set
            allSeItems.add(problem);
        }

        CreateCommit createCommit = createCommit(message, CommitMode.ADD_UPDATE_DELETE);
        MultipartFormDataOutput multipartFormDataOutput = createMultipart(createCommit, new ArrayList<>(allSeItems));
        return commit(multipartFormDataOutput, repositoryBaseUrl, repositoryProjectName);
    }

    /**
     * Creates an extended SE-Item Java Object which can hold content. This object is only used for simple programming purpose.
     *
     * @return
     */
    private static SeItemWithContent createSeOptionItem(String id, String achieve, String accepting, OptionState state) throws UnirestException, UnsupportedEncodingException {
        SeItemWithContent createSeItem = new SeItemWithContent();
        String name = getNameFromId(id);
        createSeItem.setName(name);
        String folder = getFolderFromId(id);
        createSeItem.setFolder(folder);
        createSeItem.getMetadata().putAll(MetadataFactory.getOptionMap(state));

        String markdown = "";
        if (state == CHOSEN) {
            markdown = String.format("#%s\n%s\n\n#%s\n%s", YStatementConstants.SEITEM_ACHIEVING, achieve, YStatementConstants.SEITEM_ACCEPTING, accepting);
        }
        createSeItem.setContent(markdown.getBytes());
        createSeItem.setMimeType("text/markdown");
        return createSeItem;
    }

    private static String getId(String folder, String name) {
        return folder.trim() + UrlEscapers.urlFragmentEscaper().escape(name.trim());
    }

    private static String getNameFromId(String id) throws UnsupportedEncodingException {
        return URLDecoder.decode(id, "UTF-8").substring(id.lastIndexOf("/") + 1).trim();
    }

//    private static String getFolderFromId(String id) {
//        return id.substring(id.lastIndexOf("/seitems") + 9, id.lastIndexOf("/") + 1);
//    }

    private static String getFolderFromId(String id) {
        return id.trim().substring(0, id.lastIndexOf("/") + 1);
    }

    /**
     * Creates an extended SE-Item Java Object which can hold content. This object is only used for simple programming purpose.
     *
     * @return
     */
    private static SeItemWithContent createSeProblemItem(String id, String context, String facing, ProblemState state) throws UnirestException, UnsupportedEncodingException {
        SeItemWithContent createSeItem = new SeItemWithContent();
        String name = getNameFromId(id);
        createSeItem.setName(name);
        String folder = getFolderFromId(id);
        createSeItem.setFolder(folder);
        createSeItem.getMetadata().putAll(MetadataFactory.getProblemMap(state));

        String markdown = "";
        if (state == SOLVED) {
            markdown = String.format("#%s\n%s\n\n#%s\n%s", YStatementConstants.SEITEM_CONTEXT, context, YStatementConstants.SEITEM_FACING, facing);
        }
        createSeItem.setContent(markdown.getBytes());
        createSeItem.setMimeType("text/markdown");
        return createSeItem;
    }

    /**
     * Commits the SE-Items (within the Multipart) to the given repository on SE-Repo.
     *
     * @param multipart
     * @return
     */
    private static String commit(MultipartFormDataOutput multipart, String repositoryBaseUrl, String repositoryProjectName) {
        WebTarget api = ClientBuilder.newClient()
                .target(repositoryBaseUrl)
                .path("/repos/" + repositoryProjectName + "/commits");

        Response response = api.request(MediaType.TEXT_PLAIN_TYPE)
                .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));

        String commitId;
        if (response.hasEntity()) {
            commitId = response.readEntity(String.class);
        } else {
            commitId = "";
        }
        response.close();
        return commitId;

    }

    /**
     * Creates a Multipart which will be needed for the SE-Repo API call to commit SE-Items.
     *
     * @param commit
     * @param seItemsWithContent
     * @return
     */
    private static MultipartFormDataOutput createMultipart(CreateCommit commit, List<SeItemWithContent> seItemsWithContent) {
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

    /**
     * Creates a Commit object representing a commitToBaseRepo which can be sent to the SE-Repo API.
     *
     * @param message
     * @param mode
     * @return
     */
    private static CreateCommit createCommit(String message, CommitMode mode) {
        CreateCommit commit = new CreateCommit();
        commit.setMessage(message);
        commit.setMode(mode);
        User user = new User("eADL-Synchronizer", "eadl@sync.com");
        commit.setUser(user);
        return commit;
    }
}
