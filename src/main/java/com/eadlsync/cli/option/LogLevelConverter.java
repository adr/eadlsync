package com.eadlsync.cli.option;

import ch.qos.logback.classic.Level;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.List;

/**
 * Log level converter to convert a string value to a log level.
 */
public class LogLevelConverter implements IStringConverter<Level> {

    private final List<String> supportedLogLevels = Arrays.asList("ALL", "DEBUG", "INFO", "WARN", "ERROR", "OFF");

    @Override
    public Level convert(String value) {
        if (supportedLogLevels.contains(value.toUpperCase())) {
            return Level.toLevel(value, Level.OFF);
        }
        throw new ParameterException(String.format("Your value (%s) is not a supported log level %s", value, supportedLogLevels));
    }
}
