package com.eadlsync.cli;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import radar.ad.annotations.YStatementJustification;


/**
 * Created by Tobias on 23.04.2017.
 */
@YStatementJustification(
        id = "S_GUI/java/com/eadlsync/cli/User%20Interface",
        context = "How should the synchronization tool be used",
        facing = "Used by software engineers",
        chosen = "S_GUI/java/com/eadlsync/cli/Console%20Interface",
        neglected = "S_GUI/java/com/eadlsync/cli/Self%20made%20GUI, S_GUI/java/com/eadlsync/cli/IntelliJ%20Add-In",
        achieving = "Not much implementation effort",
        accepting = "Bad usabillity"
)
public abstract class ACLIMenu {

    protected final BooleanProperty running = new SimpleBooleanProperty(false);
    protected final StringProperty option = new SimpleStringProperty();
    protected final String name;
    protected final Scanner scanner = new Scanner(System.in);
    protected List<CLIMenuItem> menuItems = Collections.emptyList();

    protected ACLIMenu(String name) {
        this.name = name;
    }

    // TODO: really use this method to clear screen???
    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        while (running.get()) {
            System.out.println("EADLSync - " + this.name);
            System.out.println("********");
            menuItems.forEach(System.out::println);
            option.setValue(scanner.nextLine());
            clearScreen();
            evaluate(option.get());
        }
    }

    protected void bindLoop(BooleanBinding or) {
        running.bind(or);
    }

    public void setMenuItems(List<CLIMenuItem> items) {
        this.menuItems = items;
    }

    protected abstract void evaluate(String s);
}
