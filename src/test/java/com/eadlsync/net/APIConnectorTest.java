package com.eadlsync.net;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.eadlsync.net.restful.MockedAPI;
import com.eadlsync.serepo.data.restinterface.common.Link;
import com.eadlsync.serepo.data.restinterface.metadata.MetadataEntry;
import com.eadlsync.serepo.data.restinterface.seitem.RelationEntry;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;
import com.eadlsync.util.net.APIConnector;
import com.eadlsync.util.net.SeRepoUrlObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static com.eadlsync.util.net.RelationFactory.ADMentorRelationType.ADDRESSED_BY;
import static com.eadlsync.util.net.RelationFactory.ADMentorRelationType.RAISES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Tobias on 04.02.2017.
 */
public class APIConnectorTest extends MockedAPI {

    private SeRepoUrlObject urlObject = new SeRepoUrlObject(SEREPO_URL, SEREPO_NAME, SEREPO_COMMIT_ID);

    @Test
    public void testAPIConnectorgetSeItemContainer() throws UnirestException {
        List<SeItem> items = APIConnector.withSeRepoUrl(urlObject).getSeItemsByUrl();
        assertFalse(items.isEmpty());
    }

    @Test
    public void testGetMetadataForSeItem() throws UnirestException {
        List<SeItem> items = APIConnector.withSeRepoUrl(urlObject).getSeItemsByUrl();
        SeItem guiItem = items.stream().filter(item -> item.getName().equals("GUI")).
                collect(Collectors.toList()).get(0);
        MetadataEntry metadata = APIConnector.withSeRepoUrl(urlObject).getMetadataEntry(guiItem);
        assertNotNull(metadata);
        assertFalse(metadata.getMap().isEmpty());
    }

    @Test
    public void testGetRelationsForSeItem() throws UnirestException {
        List<SeItem> items = APIConnector.withSeRepoUrl(urlObject).getSeItemsByUrl();
        SeItem guiItem = items.stream().filter(item -> item.getName().equals("GUI")).
                collect(Collectors.toList()).get(0);
        RelationEntry relation = APIConnector.withSeRepoUrl(urlObject).getRelationEntry(guiItem);
        assertNotNull(relation);
    }

    @Test
    public void testRelationsPresent() throws UnirestException {
        List<SeItem> items = APIConnector.withSeRepoUrl(urlObject).getSeItemsByUrl();
        SeItem guiItem = items.stream().filter(item -> item.getName().equals("GUI")).
                collect(Collectors.toList()).get(0);
        RelationEntry relation = APIConnector.withSeRepoUrl(urlObject).getRelationEntry(guiItem);
        List<String> relations = relation.getLinks().stream().map(Link::getTitle).collect(Collectors.toList());
        assertEquals(relations, Arrays.asList(ADDRESSED_BY.getName(), RAISES.getName()));
    }

}
