package io.github.adr.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class InitCommandTest extends CommandTest {

    @Test
    public void testParsedInitCommand() {
        Assert.assertEquals(InitCommand.NAME, commander.getParsedCommand());
    }

    @Test
    public void testEadlSyncDirectoryExists() {
        Assert.assertTrue(Files.isDirectory(EADLSyncCommand.EADL_ROOT));
    }

    @Test
    public void testEadlConfigFileExists() {
        Assert.assertTrue(Files.exists(EADLSyncCommand.EADL_CONFIG));
    }

    @Test
    public void testEadlCommitIdFileExists() {
        Assert.assertTrue(Files.exists(EADLSyncCommand.EADL_REVISION));
    }

    @Test
    public void testCommitIdFileHasLatestCommit() throws IOException {
        String commitId = Files.readAllLines(EADLSyncCommand.EADL_REVISION).stream().collect
                (Collectors.joining());
        Assert.assertEquals(seRepoTestServer.lastCommit, commitId);
    }
}