package com.eadlsync.net;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eadlsync.eadl.annotations.YStatementJustificationWrapper;
import com.eadlsync.eadl.annotations.YStatementJustificationWrapperBuilder;
import com.eadlsync.serepo.data.restinterface.common.Link;
import com.eadlsync.serepo.data.restinterface.metadata.MetadataContainer;
import com.eadlsync.serepo.data.restinterface.metadata.MetadataEntry;
import com.eadlsync.serepo.data.restinterface.seitem.RelationContainer;
import com.eadlsync.serepo.data.restinterface.seitem.RelationEntry;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;
import com.eadlsync.serepo.data.restinterface.seitem.SeItemContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Provides utility methods to communicate with the se-repo restful api
 * <p>
 * Created by Tobias on 31.01.2017.
 */
public class APIConnector {

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

    public static SeItemContainer getSeItemContainerByUrl(String url) throws UnirestException {
        HttpResponse<SeItemContainer> seItemContainerResponse = Unirest.get(url).asObject
                (SeItemContainer.class);
        SeItemContainer seItemContainer = seItemContainerResponse.getBody();
        return seItemContainer;
    }

    private static MetadataContainer getMetadataContainerForSeItem(SeItem item) throws UnirestException {
        Link metadataLink = item.getLinks().stream().filter(link -> link.getRel().endsWith
                ("serepo_metadata")).
                collect(Collectors.toList()).get(0);
        HttpResponse<MetadataContainer> seItemContainerResponse = Unirest.get(metadataLink.getHref()).
                asObject(MetadataContainer.class);
        MetadataContainer metadataContainer = seItemContainerResponse.getBody();
        return metadataContainer;
    }

    public static MetadataEntry getMetadataEntry(SeItem item) throws UnirestException {
        return getMetadataContainerForSeItem(item).getMetadata();
    }

    private static RelationContainer getRelationContainerForSeItem(SeItem item) throws UnirestException {
        Link relationsLink = item.getLinks().stream().filter(link -> link.getRel().endsWith
                ("serepo_relations")).
                collect(Collectors.toList()).get(0);
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
            Object o = metadata.getMap().get("stereotype");
            String stereotype = (o == null) ? "" : o.toString();
            if (stereotype.toLowerCase().equals("problem occurrence")) {
                problemItems.add(item);
            }
        }

        List<YStatementJustificationWrapper> yStatementJustifications = new ArrayList<>();

        // iterate over the problem occurrences
        for (SeItem problemItem : problemItems) {
            RelationEntry relation = getRelationEntry(problemItem);
            List<Link> relationLinks = relation.getLinks().stream().filter(link -> "addressed by"
                    .equals(link.getTitle().toLowerCase())).collect(Collectors.toList());

            // find the chosen option item for the current problem occurrence
            SeItem chosenOptionItem = null;
            for (Link link : relationLinks) {
                List<SeItem> relationSeItems = container.getSeItems().stream().filter(seItem -> seItem
                        .getId().equals(link.getHref())).collect(Collectors.toList());
                for (SeItem relationSeItem : relationSeItems) {
                    MetadataEntry entry = getMetadataEntry(relationSeItem);
                    Object o = entry.getMap().get("Option State");
                    String state = (o == null) ? "" : o.toString();
                    if ("chosen".equals(state.toLowerCase())) {
                        chosenOptionItem = container.getSeItems().stream().filter(seItem -> seItem
                                .getId().equals(link.getHref())).collect(Collectors.toList()).get(0);
                    }
                }
            }

            // create a new YStatementJustification object
            YStatementJustificationWrapper yStatementJustification = createYStatementJustification
                    (problemItem, chosenOptionItem);
            yStatementJustifications.add(yStatementJustification);

        }

        return yStatementJustifications;
    }

    private static YStatementJustificationWrapper createYStatementJustification(SeItem problemItem,
                                                                                SeItem chosenOptionItem) {
        Element problemBody = getSeItemContent(problemItem);
        String id = problemItem.getId().toString();
        String context = parseForContent("in the context of", problemBody);
        String facing = parseForContent("facing", problemBody);

        if (chosenOptionItem != null) {
            Element optionBody = getSeItemContent(chosenOptionItem);
            String chosen = parseForContent("we decided for", optionBody);
            String neglected = parseForContent("and neglected", optionBody);
            String achieving = parseForContent("to achieve", optionBody);
            String accepting = parseForContent("accepting that", optionBody);
            return new YStatementJustificationWrapperBuilder(id).context(context).facing(facing)
                    .chosen(chosen).neglected(neglected).achieving(achieving).accepting(accepting)
                    .build();
        } else {
            return new YStatementJustificationWrapperBuilder(id).context(context).facing(facing).build();
        }
    }

    private static String parseForContent(String key, Element seItemBody) {
        String content = "";
        seItemBody.getAllElements().stream().filter(element -> key.equals(element.val())).forEach
                (element -> {
            // TODO: implement parsing the html boy for the content of the key
        });
        return content;
    }

    private static Element getSeItemContent(SeItem item) {
        Document doc;
        Element body = null;
        try {
            String decodedURL = URLDecoder.decode(item.getId().toString(), "UTF-8");
            doc = Jsoup.connect(decodedURL).get();
            body = doc.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

}
