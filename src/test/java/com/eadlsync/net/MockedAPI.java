package com.eadlsync.net;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * Created by Tobias on 04.02.2017.
 */
public class MockedAPI {

    protected final static String SEREPO_NAME = "TestRepo";
    protected final static String SEREPO_COMMIT_ID = "6366fa63316999b3ecfedaeb9751f50a3bbf4761";
    protected final static String SEREPO_URL = "http://localhost:8080/serepo";
    protected final static String SEREPO_REPOS_URL = String.format("%s/repos", SEREPO_URL);
    protected final static String TEST_REPO_URL = String.format("%s/%s", SEREPO_REPOS_URL, SEREPO_NAME);
    protected final static String TEST_REPO_COMMITS_URL = String.format("%s/commits", TEST_REPO_URL);
    protected final static String TEST_COMMIT_URL = String.format("%s/%s", TEST_REPO_COMMITS_URL, SEREPO_COMMIT_ID);
    protected final static String TEST_SEITEMS_URL = String.format("%s/seitems", TEST_COMMIT_URL);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);


    @Before
    public void setUp() {

        // TODO: check if local runtime is running otherwise mock the api

        String mockedServerPath = "/serepo/repos";
        String responseFilePath = mockedServerPath.substring(1).replaceAll("/", "_") + ".json";
        wireMockRule.stubFor(get(urlEqualTo(mockedServerPath))
                .willReturn(aResponse().withBodyFile(responseFilePath)));
        mockedServerPath = String.format("/serepo/repos/%s", SEREPO_NAME);
        responseFilePath = mockedServerPath.substring(1).replaceAll("/", "_") + ".json";
        wireMockRule.stubFor(get(urlEqualTo(mockedServerPath))
                .willReturn(aResponse().withBodyFile(responseFilePath)));
        mockedServerPath = String.format("/serepo/repos/%s/commits", SEREPO_NAME);
        responseFilePath = mockedServerPath.substring(1).replaceAll("/", "_") + ".json";
        wireMockRule.stubFor(get(urlEqualTo(mockedServerPath))
                .willReturn(aResponse().withBodyFile(responseFilePath)));
        mockedServerPath = String.format("/serepo/repos/%s/commits/%s", SEREPO_NAME, SEREPO_COMMIT_ID);
        responseFilePath = mockedServerPath.substring(1).replaceAll("/", "_") + ".json";
        wireMockRule.stubFor(get(urlEqualTo(mockedServerPath))
                .willReturn(aResponse().withBodyFile(responseFilePath)));
        mockedServerPath = String.format("/serepo/repos/%s/commits/%s/seitems", SEREPO_NAME, SEREPO_COMMIT_ID);
        responseFilePath = mockedServerPath.substring(1).replaceAll("/", "_") + ".json";
        wireMockRule.stubFor(get(urlEqualTo(mockedServerPath))
                .willReturn(aResponse().withBodyFile(responseFilePath)));

    }


}
