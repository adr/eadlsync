package com.eadlsync.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import ch.hsr.isf.serepo.data.restinterface.repository.RepositoryContainer;
import ch.hsr.isf.serepo.server.SeRepoServer;
import com.google.gson.Gson;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tobias on 03.06.2017.
 */
public class SeRepoServerTest {

    private static final int PORT = 8080;
    private static final String LOCALHOST_SEREPO = String.format("http://localhost:%s/serepo/repos", PORT);
    private static SeRepoServer server;

    @BeforeClass
    public static void classSetUp() throws Exception {
        server = SeRepoServer.create(PORT);
        server.start();
    }

    @Test
    public void testIsServerRunning() throws IOException {
        InputStream input = new URL(LOCALHOST_SEREPO).openStream();
        Reader reader = new InputStreamReader(input, "UTF-8");
        RepositoryContainer repos = new Gson().fromJson(reader, RepositoryContainer.class);

        assertEquals(LOCALHOST_SEREPO, repos.getId().toString());
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        server.stop();
    }



}
