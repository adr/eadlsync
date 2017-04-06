package com.eadlsync.core;

import java.net.MalformedURLException;

import com.eadlsync.model.repo.OfflineCodeRepo;
import com.eadlsync.net.APIConnector;
import com.eadlsync.net.restful.MockedAPI;
import com.eadlsync.serepo.data.restinterface.seitem.SeItemContainer;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by tobias on 09/03/2017.
 */
public class OfflineCodeRepoTest extends MockedAPI {

    @Test
    public void testOfflineCodeRepo() throws MalformedURLException, UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        OfflineCodeRepo repo = new OfflineCodeRepo("/Users/tobias/git/eadlsync/src/main/java", container.getSeItems());

        assertFalse(repo.findObsoleteEADs(container.getSeItems()).isEmpty());

    }

}
