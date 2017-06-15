package com.eadlsync.cli;

import com.beust.jcommander.JCommander;
import com.eadlsync.cli.command.InitCommand;
import com.eadlsync.cli.option.MainOption;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Tobias on 23.04.2017.
 */
public class EADLSyncMain {

    private static final Logger LOG = LoggerFactory.getLogger(EADLSyncMain.class);

    public static void main(String[] args) {
        args = new String[]{"init", "-u=\"http://localhost:8080/serepo\"", "-p=\"embedded-adl\""};

        InitCommand initCommand = new InitCommand();
        MainOption option = new MainOption();
        JCommander commander = JCommander.newBuilder().addObject(option).addCommand("init", initCommand).build();

        commander.parse(args);

        if ("init".equals(commander.getParsedCommand())) {
            try {
                initCommand.initialize();
            } catch (IOException e) {
                LOG.debug("Error writing files", e);
            } catch (UnirestException e) {
                LOG.debug("Error connecting to the se-repo", e);
            }
        }

    }

}
