package com.eadlsync.core;

import java.io.IOException;
import java.net.MalformedURLException;

import com.eadlsync.model.repo.CodeRepo;
import com.eadlsync.model.repo.IRepo;
import com.eadlsync.model.repo.SeRepo;
import com.eadlsync.net.restful.MockedAPI;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tobias on 09/03/2017.
 */
public class CodeRepoTest extends MockedAPI {

    @Test
    public void testCodeRepo() throws IOException, UnirestException {
        IRepo repo = new CodeRepo("/Users/tobias/git/eadlsync/src/main/java");
        assertTrue(repo.yStatementJustificationsProperty().isEmpty());
    }

    @Test
    public void testSeRepo() throws MalformedURLException, UnirestException {
        IRepo repo = new SeRepo(TEST_SEITEMS_URL);
        assertFalse(repo.yStatementJustificationsProperty().isEmpty());
    }

}
