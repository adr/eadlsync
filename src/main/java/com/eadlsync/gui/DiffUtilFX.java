package com.eadlsync.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

import difflib.Delta;
import difflib.DiffUtils;

import static javafx.scene.paint.Color.DARKGREEN;
import static javafx.scene.paint.Color.DARKRED;

/**
 * Provides utility methods to create a chain of text nodes that a already formatted to represent a diff.
 */
public class DiffUtilFX {

    public static List<Text> getDiffHighlightedTextNodes(StringProperty baseString, String modifiedString) {
        return getDiffHighlightedTextNodes(baseString.get(), modifiedString);
    }

    public static List<Text> getDiffHighlightedTextNodes(String baseString, String modifiedString) {

        List<Text> textFlow = new ArrayList<>();

        if ((baseString != null) && (modifiedString != null)) {
            List<Delta<String>> deltaList = new ArrayList<>(DiffUtils.diff(Arrays.asList(baseString.split("")), Arrays.asList
                    (modifiedString.split(""))).getDeltas());
            int offset = -1;
            for (Delta<String> delta : deltaList) {

                int startPositionDelta = delta.getOriginal().getPosition();

                switch (delta.getType()) {
                    case CHANGE:

                        if (offset < startPositionDelta) {
                            String normalText = baseString.substring(offset + 1, startPositionDelta);
                            textFlow.add(getNormalTextNode(normalText));
                        }
                        offset = delta.getOriginal().last();

                        String changedRemoveText = delta.getOriginal().getLines().stream().collect(Collectors.joining());
                        textFlow.add(getDeletionTextNode(changedRemoveText));

                        String changeAddText = delta.getRevised().getLines().stream().collect(Collectors.joining());
                        textFlow.add(getAdditionTextNode(changeAddText));

                        break;
                    case DELETE:

                        if (offset < startPositionDelta) {
                            String normalText = baseString.substring(offset + 1, startPositionDelta);
                            textFlow.add(getNormalTextNode(normalText));
                        }
                        offset = delta.getOriginal().last();

                        String  deletion = delta.getOriginal().getLines().stream().collect(Collectors.joining());
                        textFlow.add(getDeletionTextNode(deletion));


                        break;
                    case INSERT:

                        if (offset < startPositionDelta) {
                            String normalText = baseString.substring(offset + 1, startPositionDelta);
                            textFlow.add(getNormalTextNode(normalText));
                        }
                        offset = delta.getOriginal().last();

                        String addition = delta.getRevised().getLines().stream().collect(Collectors.joining());
                        textFlow.add(getAdditionTextNode(addition));

                        break;
                    default:
                        break;
                }
            }
            if (offset < baseString.length()) {
                Text normalText = new Text(baseString.substring(offset + 1));
                textFlow.add(normalText);
            }
        } else {
            textFlow.add(new Text(modifiedString));
        }
        return textFlow;
    }

    private static Text getAdditionTextNode(String text) {
        Text node = new Text(text);
        node.setFill(DARKGREEN);
        return node;
    }

    private static Text getDeletionTextNode(String text) {
        Text node = new Text(text);
        node.setFill(DARKRED);
        node.setStrikethrough(true);
        return node;
    }

    private static Text getNormalTextNode(String text) {
        return new Text(text);
    }
}
