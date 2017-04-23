package com.eadlsync.core;

import java.net.MalformedURLException;
import java.util.List;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.repo.CodeRepo;
import com.eadlsync.net.APIConnector;
import com.eadlsync.net.restful.MockedAPI;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by tobias on 09/03/2017.
 */
public class CodeRepoTest extends MockedAPI {

    @Test
    public void testCodeRepo() throws MalformedURLException, UnirestException {
        List<YStatementJustification> statements = APIConnector.getYStatementJustifications(TEST_SEITEMS_URL);
        CodeRepo repo = CodeRepo.getInstance();
        repo.initializeFromPath("/Users/tobias/git/eadlsync/src/main/java");
        repo.setSeRepoYStatements(statements);

        assertFalse(repo.findObsoleteEADs().isEmpty());

    }

}
