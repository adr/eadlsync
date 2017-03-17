package com.eadlsync.net;

import com.eadlsync.net.restful.MockedAPI;
import com.eadlsync.serepo.data.restinterface.common.Link;
import com.eadlsync.serepo.data.restinterface.metadata.MetadataContainer;
import com.eadlsync.serepo.data.restinterface.seitem.SeItem;
import com.eadlsync.serepo.data.restinterface.seitem.SeItemContainer;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Created by Tobias on 04.02.2017.
 */
public class APIConnectorTest extends MockedAPI {

    @Test
    public void testAPIConnectorgetSeItemContainer() throws UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        List<SeItem> items = container.getSeItems();
        items.forEach(item -> {
            List<Link> links = item.getLinks();
            System.out.println("SE-Item: " + item.getId());
            links.forEach(link -> {
                System.out.println("link: " + link.getRel());
                if (link.getRel().endsWith("serepo_metadata")) {
                    try {
                        MetadataContainer mContainer = APIConnector.getMetadataContainerForSeItem(item);
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        assertFalse(container.getSeItems().isEmpty());

    }

}
