package com.eadlsync.net;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.eadlsync.net.restful.MockedAPI;
import ch.hsr.isf.serepo.data.restinterface.common.Link;
import ch.hsr.isf.serepo.data.restinterface.metadata.MetadataEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.RelationEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItem;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItemContainer;
import com.eadlsync.util.net.APIConnector;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Tobias on 04.02.2017.
 */
public class APIConnectorTest extends MockedAPI {

    @Test
    public void testAPIConnectorgetSeItemContainer() throws UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        List<SeItem> items = container.getSeItems();
        assertFalse(container.getSeItems().isEmpty());

    }

    @Test
    public void testGetMetadataForSeItem() throws UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        SeItem guiItem = container.getSeItems().stream().filter(item -> item.getName().equals("GUI")).
                collect(Collectors.toList()).get(0);
        MetadataEntry metadata = APIConnector.getMetadataEntry(guiItem);
        assertNotNull(metadata);
        assertFalse(metadata.getMap().isEmpty());
    }

    @Test
    public void testGetRelationsForSeItem() throws UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        SeItem guiItem = container.getSeItems().stream().filter(item -> item.getName().equals("GUI")).
                collect(Collectors.toList()).get(0);
        RelationEntry relation = APIConnector.getRelationEntry(guiItem);
        assertNotNull(relation);
    }

    @Test
    public void testRelationsPresent() throws UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        SeItem guiItem = container.getSeItems().stream().filter(item -> item.getName().equals("GUI")).
                collect(Collectors.toList()).get(0);
        RelationEntry relation = APIConnector.getRelationEntry(guiItem);
        List<String> relations = relation.getLinks().stream().map(Link::getTitle).collect(Collectors.toList());
        System.out.println(relation.getId());
        assertEquals(relations, Arrays.asList("Addressed By", "Raises"));
    }

}
