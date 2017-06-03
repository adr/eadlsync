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

/**
 * Created by Tobias on 03.06.2017.
 */
public class SeRepoServerTest {

    private static SeRepoServer server;

    @BeforeClass
    public static void classSetUp() throws Exception {
        server = SeRepoServer.create(8080);
        server.start();
    }

    @Test
    public void testIsServerRunning() throws IOException {
        InputStream input = new URL("http://localhost:8080/serepo/repos").openStream();
        Reader reader = new InputStreamReader(input, "UTF-8");
        RepositoryContainer repos = new Gson().fromJson(reader, RepositoryContainer.class);
        System.out.println(repos.getId());
    }

    @AfterClass
    public static void classTearDown() throws Exception {
        server.stop();
    }



}
