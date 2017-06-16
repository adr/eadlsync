package com.eadlsync.cli.command;

import java.io.IOException;
import java.nio.file.Files;

import com.beust.jcommander.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Parameters(separators = "=", commandDescription = "De-Initializes an eadl code repository")
public class DeInitCommand extends EADLSyncCommand {

    public static final String NAME = "deinit";
    private static final Logger LOG = LoggerFactory.getLogger(DeInitCommand.class);

    public void deInit() throws IOException {
        if (Files.exists(EADL_ROOT)) {
            Files.walk(EADL_ROOT).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    LOG.debug("Unable to delete file or directory", e);
                }
            });
            Files.delete(EADL_ROOT);
        } else {
            LOG.debug("EadlSync directory doesn't exist - make sure to call this in the project root");
        }
        if (Files.exists(EADL_REVISION)) {
            Files.delete(EADL_REVISION);
        } else {
            LOG.debug("EadlSync revision file not found");
        }
    }

}
