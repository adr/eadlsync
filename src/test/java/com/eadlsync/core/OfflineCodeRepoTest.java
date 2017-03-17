package com.eadlsync.core;

import com.eadlsync.net.APIConnector;
import com.eadlsync.net.restful.MockedAPI;
import com.eadlsync.repo.OfflineCodeRepo;
import com.eadlsync.serepo.data.restinterface.seitem.SeItemContainer;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertFalse;

/**
 * Created by tobias on 09/03/2017.
 */
public class OfflineCodeRepoTest extends MockedAPI {

    @Test
    public void testOfflineCodeRepo() throws MalformedURLException, UnirestException {
        SeItemContainer container = APIConnector.getSeItemContainerByUrl(TEST_SEITEMS_URL);
        OfflineCodeRepo repo = new OfflineCodeRepo("/Users/tobias/git/eadlsync/src/main/java", container.getSeItems());

        assertFalse(repo.getObsoleteEADs(container.getSeItems()).isEmpty());

    }

}
