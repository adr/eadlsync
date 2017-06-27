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

    enum DiffType{
        WORDS(" "),
        CHARS(""),
        LINES("\n");

        private String separator = "";

        DiffType(String s) {
            separator = s;
        }

        public String getSeparator() {
            return separator;
        }
    }

    public static List<Text> getDiffHighlightedTextNodes(StringProperty baseString, String modifiedString, DiffType type) {
        return getDiffHighlightedTextNodes(baseString.get(), modifiedString, type);
    }

    public static List<Text> getDiffHighlightedTextNodes(String baseString, String modifiedString, DiffType type) {

        List<Text> textFlow = new ArrayList<>();
        String separator = type.getSeparator();

        if ((baseString != null) && (modifiedString != null)) {
            List<String> baseList = Arrays.asList(baseString.split(separator));
            List<Delta<String>> deltaList = new ArrayList<>(DiffUtils.diff(baseList, Arrays.asList
                    (modifiedString.split(separator))).getDeltas());
            int offset = 0;
            for (Delta<String> delta : deltaList) {

                int startPositionDelta = delta.getOriginal().getPosition();
                int endPositionDelta = delta.getOriginal().last();
                StringBuilder normalText = new StringBuilder();

                if (offset < startPositionDelta) {
                    if (offset != 0) normalText.append(separator);
                    for (int i = offset; i < startPositionDelta; i++) {
                        normalText.append(baseList.get(i));
                        normalText.append(separator);
                    }
                    textFlow.add(getNormalTextNode(normalText.toString()));
                }
                offset = endPositionDelta + 1;

                switch (delta.getType()) {

                    case CHANGE:

                        String changedRemoveText = delta.getOriginal().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getDeletionTextNode(changedRemoveText));

                        String changeAddText = delta.getRevised().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getAdditionTextNode(changeAddText));

                        break;
                    case DELETE:

                        String  deletion = delta.getOriginal().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getDeletionTextNode(deletion));

                        break;
                    case INSERT:

                        String suffix = (endPositionDelta == -1) ? separator : "";
                        String addition = delta.getRevised().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getAdditionTextNode(addition.concat(suffix)));

                        break;
                    default:
                        break;
                }
            }
            if (offset < baseList.size()) {
                StringBuilder normalText = new StringBuilder();
                for (int i = offset; i < baseList.size(); i++) {
                    normalText.append(baseList.get(i));
                    if (i + 1 != baseList.size()) normalText.append(separator);
                }
                textFlow.add(getNormalTextNode(normalText.toString()));
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
