package com.eadlsync.util;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser that parses an html element for the content of a specific string
 */
public class SeItemContentParser {

    private static final Logger LOG = LoggerFactory.getLogger(SeItemContentParser.class);

    public static String parseForContent(String key, Element seItemBody) {
        String content = seItemBody.outerHtml();

        // check if key is found and remove everything in front
        int keyOccurrence = content.toLowerCase().indexOf(key);
        if (keyOccurrence == -1) {
            return "";
        } else {
            content = content.substring(keyOccurrence + key.length());
        }

        // check if line break is found and remove everything in front
        // assumption: content is right after the key and on new line
        int firstLineBreak = content.indexOf("<br>");
        if (firstLineBreak == -1) {
            return "";
        } else {
            content = content.substring(firstLineBreak + 4);
        }
        content = content.replaceAll("\r", "");
        content = content.replaceAll("\n", "");

        // only read until the next line break
        Pattern patternLineBreak = Pattern.compile("(.+?)<br>.*");
        Matcher matcherLineBreak = patternLineBreak.matcher(content);
        // if no line break tag is found just read until the next html tag occurs
        Pattern patternTag = Pattern.compile("(.+?)<.*>.*");
        Matcher matcherTag = patternTag.matcher(content);

        if (matcherLineBreak.find()) {
            content = matcherLineBreak.group(1);
        } else if (matcherTag.find()) {
            content = matcherTag.group(1);
        } else {
            LOG.debug("No end html tag for key '{}' in [{}] parsed from [{}]", key, content, seItemBody.outerHtml());
        }

        content = content.trim();
        return content;
    }

}
