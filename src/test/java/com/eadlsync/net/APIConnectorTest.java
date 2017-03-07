package com.eadlsync.net;

import com.eadlsync.net.restful.MockedAPI;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by Tobias on 04.02.2017.
 */
public class APIConnectorTest extends MockedAPI {

    @Test
    public void testAPIConnectorgetSeItems() {
        APIConnector apiConnector = new APIConnector();
        try {
            assertFalse(apiConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL).getSeItems().isEmpty());
        } catch (UnirestException e) {
            Assert.fail(e.getMessage());
        }
    }

}
