package com.eadlsync.model.diff;

/**
 *
 */
public class StringDiff {

    private int changeStart;
    private int changeEnd;
    private String baseString;
    private String changeString;
    private String diffString;

    private StringDiff(int changeStart, int changeEnd, String baseString, String changeString, String diffString) {
        this.changeStart = changeStart;
        this.changeEnd = changeEnd;
        this.baseString = baseString;
        this.changeString = changeString;
        this.diffString = diffString;
    }

    public static StringDiff of(String fieldValue, String fieldNewValue) {
        int startPosition = getStartPosition(fieldValue, fieldNewValue);
        int endPosition = startPosition;
        if (startPosition != fieldValue.length()) {
            endPosition = getEndPosition(fieldValue, fieldNewValue, startPosition);
        }
        String diff;

        if (startPosition == fieldValue.length()) {
            diff = fieldNewValue.substring(startPosition);
        } else if (endPosition == 0) {
            diff = fieldNewValue.substring(0, fieldNewValue.indexOf(fieldValue));
        } else {
            diff = fieldNewValue.substring(startPosition, fieldNewValue.lastIndexOf(fieldValue.substring(endPosition)));
        }
        return new StringDiff(startPosition, endPosition, fieldValue, fieldNewValue, diff);
    }

    private static int getEndPosition(String fieldValue, String fieldNewValue, int start) {
        String base = fieldValue.substring(start);
        for (int index = 0; index < base.length(); index++) {
            if (fieldNewValue.endsWith(base.substring(index))) {
                return start + index;
            }
        }
        return fieldValue.length();
    }

    private static int getStartPosition(String fieldValue, String fieldNewValue) {
        StringBuilder start = new StringBuilder();
        for (char current : fieldValue.toCharArray()) {
            start.append(current);
            if (!fieldNewValue.startsWith(start.toString())) {
                return start.length() - 1;
            }
        }
        return fieldNewValue.length() > fieldValue.length() ? fieldValue.length() : 0;
    }

    public boolean conflictsWith(StringDiff stringDiff) {
        if (this.changeStart == stringDiff.changeStart && this.changeEnd == stringDiff.changeEnd &&
                this.changeString.equals(stringDiff.changeString)) {
            return false;
        } else if (this.changeStart == stringDiff.changeStart && this.changeEnd == stringDiff.changeEnd) {
            return true;
        } else if (this.changeStart <= stringDiff.changeStart && this.changeEnd >= stringDiff
                .changeStart) {
            return true;
        } else if (stringDiff.changeStart <= this.changeStart && stringDiff.changeEnd >= this
                .changeStart) {
            return true;
        }
        return false;
    }

    public String applyNonConflicting(StringDiff stringDiff) {
        StringBuilder builder = new StringBuilder();

        int offset = changeString.length() - baseString.length();
        int newStart = stringDiff.changeStart;
        int newEnd = stringDiff.changeEnd;
        if (this.changeStart <= stringDiff.changeStart) {
             newStart += offset;
             newEnd += offset;
        }

        if (newStart > 0) {
            builder.append(changeString.substring(0, newStart));
        }
        builder.append(stringDiff.diffString);
        if (newEnd < changeString.length()) {
            builder.append(changeString.substring(newEnd));
        }
        return builder.toString();
    }

    public String apply() {
        return this.changeString;
    }

    public String getDiffString() {
        return diffString;
    }
}
