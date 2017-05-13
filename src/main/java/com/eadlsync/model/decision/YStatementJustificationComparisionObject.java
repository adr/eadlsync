package com.eadlsync.model.decision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 13.05.2017.
 */
public class YStatementJustificationComparisionObject {

    private static final String HEAD = "<<<<<<< HEAD:code repo<to be replaced with path>";
    private static final String TAIL = ">>>>>>> conflicts:se-repo<to be replaced with url>";
    private static final String SEPARATOR = "=======";
    private YStatementJustificationWrapper codeDecision;
    private YStatementJustificationWrapper seDecision;
    private List<String> lastWrittenFields = new ArrayList<>();
    private boolean[] differences;

    public YStatementJustificationComparisionObject(YStatementJustificationWrapper codeDecision,
                                                    YStatementJustificationWrapper seDecision) {
        this.codeDecision = codeDecision;
        this.seDecision = seDecision;
        this.differences = new boolean[]{!isContextEqual(), !isFacingEqual(), !isChosenEqual(),
                !isNeglectedEqual(), !isAchievingEqual(), !isAcceptingEqual(), !isMoreInformationEqual
                ()};
    }

    public boolean hasSameObjectWithDifferentFields() {
        return !codeDecision.equals(seDecision);
    }

    private boolean isContextEqual() {
        return codeDecision != null && codeDecision.getContext().equals(seDecision.getContext());
    }

    private boolean isFacingEqual() {
        return codeDecision != null && codeDecision.getFacing().equals(seDecision.getFacing());
    }

    private boolean isChosenEqual() {
        return codeDecision != null && codeDecision.getChosen().equals(seDecision.getChosen());
    }

    private boolean isNeglectedEqual() {
        return codeDecision != null && codeDecision.getNeglected().equals(seDecision.getNeglected());
    }

    private boolean isAchievingEqual() {
        return codeDecision != null && codeDecision.getAchieving().equals(seDecision.getAchieving());
    }

    private boolean isAcceptingEqual() {
        return codeDecision != null && codeDecision.getAccepting().equals(seDecision.getAccepting());
    }

    private boolean isMoreInformationEqual() {
        return codeDecision != null && codeDecision.getMoreInformation().equals(seDecision
                .getMoreInformation());
    }

    private String addSeparatorAndDifferencesIfNeeded(int index, String differentField) {
        if (!isPreviousDifferent(index)) {
            lastWrittenFields.clear();
        }
        if (isNextDifferent(index)) {
            lastWrittenFields.add(differentField);
            return "";
        } else {
            lastWrittenFields.add(differentField);
            String sepAndDiff;
            sepAndDiff = "\n" + SEPARATOR;
            for (String field : lastWrittenFields) {
                sepAndDiff += "\n" + field;
            }
            return sepAndDiff;
        }
    }

    private String addHeadIfNeeded(int index) {
        return isPreviousDifferent(index) ? "" : HEAD + "\n";
    }

    private String addTailIfNeeded(int index) {
        return isNextDifferent(index) ? "" : "\n" + TAIL;
    }

    private boolean isPreviousDifferent(int index) {
        if (index - 1 < 0) {
            return false;
        }
        return differences[index - 1];
    }

    private boolean isNextDifferent(int index) {
        if (index + 1 >= differences.length) {
            return false;
        }
        return differences[index + 1];
    }

    private String getContextComparisionString() {
        String comp;
        if (isContextEqual()) {
            comp = "context='" + codeDecision.getContext() + "'";
        } else {
            comp = addHeadIfNeeded(0);
            comp += "context='" + codeDecision.getContext() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(0, "context='" + seDecision.getContext() + "'");
            comp += addTailIfNeeded(0);
        }
        return comp;
    }

    private String getFacingComparisionString() {
        String comp;
        if (isFacingEqual()) {
            comp = "facing='" + codeDecision.getFacing() + "'";
        } else {
            comp = addHeadIfNeeded(1);
            comp += "facing='" + codeDecision.getFacing() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(1, "facing='" + seDecision.getFacing() + "'");
            comp += addTailIfNeeded(1);
        }
        return comp;
    }

    private String getChosenComparisionString() {
        String comp;
        if (isChosenEqual()) {
            comp = "chosen='" + codeDecision.getChosen() + "'";
        } else {
            comp = addHeadIfNeeded(2);
            comp += "chosen='" + codeDecision.getChosen() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(2, "chosen='" + seDecision.getChosen() + "'");
            comp += addTailIfNeeded(2);
        }
        return comp;
    }

    private String getNeglectedComparisionString() {
        String comp;
        if (isNeglectedEqual()) {
            comp = "neglected='" + codeDecision.getNeglected() + "'";
        } else {
            comp = addHeadIfNeeded(3);
            comp += "neglected='" + codeDecision.getNeglected() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(3, "neglected='" + seDecision.getNeglected() +
                    "'");
            comp += addTailIfNeeded(3);
        }
        return comp;
    }

    private String getAchievingComparisionString() {
        String comp;
        if (isAchievingEqual()) {
            comp = "achieving='" + codeDecision.getAchieving() + "'";
        } else {
            comp = addHeadIfNeeded(4);
            comp += "achieving='" + codeDecision.getAchieving() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(4, "achieving='" + seDecision.getAchieving() +
                    "'");
            comp += addTailIfNeeded(4);
        }
        return comp;
    }

    private String getAcceptingComparisionString() {
        String comp;
        if (isAcceptingEqual()) {
            comp = "accepting='" + codeDecision.getAccepting() + "'";
        } else {
            comp = addHeadIfNeeded(5);
            comp += "accepting='" + codeDecision.getAccepting() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(5, "accepting='" + seDecision.getAccepting() +
                    "'");
            comp += addTailIfNeeded(5);
        }
        return comp;
    }

    private String getMoreInformationComparisionString() {
        String comp;
        if (isMoreInformationEqual()) {
            comp = "moreInformation='" + codeDecision.getMoreInformation() + "'";
        } else {
            comp = addHeadIfNeeded(6);
            comp += "moreInformation='" + codeDecision.getMoreInformation() + "'";
            comp += addSeparatorAndDifferencesIfNeeded(6, "moreInformation='" + seDecision
                    .getMoreInformation() + "'");
            comp += addTailIfNeeded(6);
        }
        return comp;
    }

    @Override
    public String toString() {
        String compare = "Compare View{\n";
        compare += "\tid='" + codeDecision.getId() + "'\n";
        compare += "\t" + getContextComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getFacingComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getChosenComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getNeglectedComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getAchievingComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getAcceptingComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getMoreInformationComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "}";
        return compare;
    }
}
