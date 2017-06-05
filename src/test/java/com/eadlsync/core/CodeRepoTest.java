package com.eadlsync.core;

import java.io.IOException;

import com.eadlsync.model.repo.CodeRepo;
import com.eadlsync.model.repo.IRepo;
import com.eadlsync.net.restful.MockedAPI;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

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

}
