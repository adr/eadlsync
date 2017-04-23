package com.eadlsync.cli;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tobias on 23.04.2017.
 */
public class CLIMenuItem {

    private final String number;
    private final List<String> arguments;
    private final String name;

    public CLIMenuItem(String number, String name, List<String> arguments) {
        this.number = number;
        this.arguments = arguments;
        this.name = name;
    }

    public CLIMenuItem(String number, String name) {
        this.number = number;
        this.name = name;
        this.arguments = Collections.emptyList();
    }


    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public List<String> getArguments() {
        return arguments;
    }
}
