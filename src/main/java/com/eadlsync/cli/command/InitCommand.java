package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.eadlsync.model.config.Config;
import com.eadlsync.model.config.ConfigCore;
import com.eadlsync.model.config.ConfigUser;
import com.eadlsync.util.net.SeRepoConector;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eadlsync.cli.command.InitCommand.DESCRIPTION;
import static com.eadlsync.util.ystatement.YStatementConstants.SEREPO_URL_COMMITS;

/**
 * Init command used to initialize an eadl-sync repository.
 *
 * @option help
 * @option baseUrl for the se-repo
 * @option name of the se-repo project
 */
@Parameters(commandDescription = DESCRIPTION)
public class InitCommand extends EADLSyncCommand {

    public static final String NAME = "init";
    public static final String DESCRIPTION = "use 'eadlsync init -u <base-url> -p <project>' to initialize eadlsync in this directory";
    private static final Logger LOG = LoggerFactory.getLogger(InitCommand.class);

    @Parameter(names = {"-u", "--url"}, required = true, description = "url of the se-repo, has to end with '/se-repo' except if localhost is specified")
    private String baseUrl;

    @Parameter(names = {"-p", "--project"}, required = true, description = "name of the project in the se-repo")
    private String name;

    @Parameter(names = {"-s", "--source"}, description = "specify the source directory of the project as relative or absolute path")
    private String source = PROJECT_ROOT.toString();

    public void initialize() throws IOException, UnirestException {
        baseUrl = baseUrl.contains("localhost") ? "http://localhost:8080/serepo" : baseUrl;
        Path absolute = Paths.get(source);
        if (!absolute.isAbsolute()) source = PROJECT_ROOT.resolve(source).toString();
        if (Files.exists(EADL_ROOT)) {
            LOG.debug("EadlSync directory already exists for this project");
        } else {
            Files.createDirectory(EADL_ROOT);
            createConfigFile(EADL_CONFIG);
        }
        String commitsUrl = String.format(SEREPO_URL_COMMITS, baseUrl, name);
        updateCommitId(SeRepoConector.getLatestCommit(commitsUrl));
    }

    private void createConfigFile(Path filePath) throws IOException {
        Files.createFile(filePath);
        config = createConfig();
        updateConfig();
    }

    private Config createConfig() {
        ConfigUser user = new ConfigUser();
        user.setName("EADLSynchronizer");
        user.setEmail("eadl@sync.com");

        ConfigCore core = new ConfigCore();
        core.setProjectRoot(source);
        core.setBaseUrl(baseUrl);
        core.setProjectName(name);

        Config config = new Config();
        config.setConfigCore(core);
        config.setConfigUser(user);
        return config;
    }

}
