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
        return getDiffHighlightedTextNodes(baseString.get(), modifiedString, " ");
    }

    public static List<Text> getDiffHighlightedTextNodes(String baseString, String modifiedString, String separator) {

        List<Text> textFlow = new ArrayList<>();

        if ((baseString != null) && (modifiedString != null)) {
            List<String> baseList = Arrays.asList(baseString.split(separator));
            List<Delta<String>> deltaList = new ArrayList<>(DiffUtils.diff(baseList, Arrays.asList
                    (modifiedString.split(separator))).getDeltas());
            int offset = 0;
            for (Delta<String> delta : deltaList) {

                int startPositionDelta = delta.getOriginal().getPosition();
                int endPositionDelta = delta.getOriginal().last();
                String normalText = "";

                switch (delta.getType()) {

                    case CHANGE:

                        if (offset < startPositionDelta) {
                            normalText = (offset == 0) ? normalText : separator.concat(normalText);
                            for (int i = offset; i < startPositionDelta; i++) {
                                normalText += baseList.get(i).concat(separator);
                            }
                            textFlow.add(getNormalTextNode(normalText));
                        }
                        offset = endPositionDelta + 1;

                        System.out.println(String.format("change original  at %d to %d with %s", delta.getOriginal().getPosition(), delta.getOriginal().last(), delta.getOriginal().getLines()));
                        System.out.println(String.format("change revised at %d to %d with %s", delta.getRevised().getPosition(), delta.getRevised().last(), delta.getRevised().getLines()));

                        String changedRemoveText = delta.getOriginal().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getDeletionTextNode(changedRemoveText));

                        String changeAddText = delta.getRevised().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getAdditionTextNode(changeAddText));

                        break;
                    case DELETE:


                        if (offset < startPositionDelta) {
                            normalText = (offset == 0) ? normalText : separator.concat(normalText);
                            for (int i = offset; i < startPositionDelta; i++) {
                                normalText += baseList.get(i).concat(separator);
                            }
                            textFlow.add(getNormalTextNode(normalText));
                        }
                        offset = endPositionDelta + 1;

                        System.out.println(String.format("delete original  at %d to %d with %s", delta.getOriginal().getPosition(), delta.getOriginal().last(), delta.getOriginal().getLines()));
                        System.out.println(String.format("delete revised at %d to %d with %s", delta.getRevised().getPosition(), delta.getRevised().last(), delta.getRevised().getLines()));

                        String  deletion = delta.getOriginal().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getDeletionTextNode(deletion));


                        break;
                    case INSERT:

                        if (offset < startPositionDelta) {
                            normalText = (offset == 0) ? normalText : separator.concat(normalText);
                            for (int i = offset; i < startPositionDelta; i++) {
                                normalText += baseList.get(i).concat(separator);
                            }
                            textFlow.add(getNormalTextNode(normalText));
                        }
                        offset = endPositionDelta + 1;

                        System.out.println(String.format("insert original  at %d to %d with %s", delta.getOriginal().getPosition(), delta.getOriginal().last(), delta.getOriginal().getLines()));
                        System.out.println(String.format("insert revised at %d to %d with %s", delta.getRevised().getPosition(), delta.getRevised().last(), delta.getRevised().getLines()));

                        String addition = delta.getRevised().getLines().stream().collect(Collectors.joining(separator));
                        textFlow.add(getAdditionTextNode(addition));

                        break;
                    default:
                        break;
                }
            }
            System.out.println(offset);
            if (offset < baseList.size()) {
                String normalText = "";
                for (int i = offset; i < baseList.size(); i++) {
                    normalText += baseList.get(i).concat(i + 1 == baseList.size() ? "" : separator);
                }
                textFlow.add(getNormalTextNode(normalText));
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
