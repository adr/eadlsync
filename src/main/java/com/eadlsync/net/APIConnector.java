package com.eadlsync.net;

import java.io.IOException;
import java.util.stream.Collectors;

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

/**
 * Created by Tobias on 31.01.2017.
 */
public class APIConnector {

    static {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

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
        HttpResponse<SeItemContainer> seItemContainerResponse = Unirest.get(url).asObject(SeItemContainer.class);
        SeItemContainer seItemContainer = seItemContainerResponse.getBody();
        return seItemContainer;
    }

    public static MetadataContainer getMetadataContainerForSeItem(SeItem item) throws UnirestException {
        Link metadataLink = item.getLinks().stream().filter(link -> link.getRel().endsWith("serepo_metadata")).
                collect(Collectors.toList()).get(0);
        HttpResponse<MetadataContainer> seItemContainerResponse = Unirest.get(metadataLink.getHref()).
                asObject(MetadataContainer.class);
        MetadataContainer metadataContainer = seItemContainerResponse.getBody();
        return metadataContainer;
    }

    public static MetadataEntry getMetadataEntry(SeItem item) throws UnirestException {
        return getMetadataContainerForSeItem(item).getMetadata();
    }

    public static RelationContainer getRelationContainerForSeItem(SeItem item) throws UnirestException {
        Link relationsLink = item.getLinks().stream().filter(link -> link.getRel().endsWith("serepo_relations")).
                collect(Collectors.toList()).get(0);
        HttpResponse<RelationContainer> seItemContainerResponse = Unirest.get(relationsLink.getHref()).
                asObject(RelationContainer.class);
        RelationContainer relationsContainer = seItemContainerResponse.getBody();
        return relationsContainer;
    }

    public static RelationEntry getRelationEntry(SeItem item) throws UnirestException {
        return getRelationContainerForSeItem(item).getEntry();
    }
}
