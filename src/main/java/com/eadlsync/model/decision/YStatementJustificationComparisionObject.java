package com.eadlsync.model.decision;

import com.eadlsync.util.ystatement.YStatementJustificationComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds two {@link YStatementJustificationWrapper} and provides a unified diff view.
 * This class has to be used with one decision not being {@code null}.
 */
public class YStatementJustificationComparisionObject {

    private static final String HEAD = "<<<<<<< HEAD:%s";
    private static final String TAIL = ">>>>>>> conflicts:%s";
    private static final String SEPARATOR = "=======";
    private YStatementJustificationWrapper codeDecision;
    private YStatementJustificationWrapper seDecision;
    private List<String> lastWrittenFields = new ArrayList<>();
    private boolean[] differences;

    public YStatementJustificationComparisionObject(YStatementJustificationWrapper codeDecision,
                                                    YStatementJustificationWrapper seDecision) {
        if (codeDecision == null) {
            codeDecision = new YStatementJustificationWrapperBuilder(seDecision.getId()).build();
        }
        if (seDecision == null) {
            seDecision= new YStatementJustificationWrapperBuilder(codeDecision.getId()).build();
        }
        this.codeDecision = codeDecision;
        this.seDecision = seDecision;
        this.differences = new boolean[]{!isContextEqual(), !isFacingEqual(), !isChosenEqual(),
                !isNeglectedEqual(), !isAchievingEqual(), !isAcceptingEqual(), !isMoreInformationEqual
                ()};
    }

    public boolean hasSameObjectWithDifferentFields() {
        return YStatementJustificationComparator.isSameButNotEqual(codeDecision, seDecision);
    }

    private boolean isContextEqual() {
        return YStatementJustificationComparator.isContextEqual(codeDecision, seDecision);
    }

    private boolean isFacingEqual() {
        return YStatementJustificationComparator.isFacingEqual(codeDecision, seDecision);
    }

    private boolean isChosenEqual() {
        return YStatementJustificationComparator.isChosenEqual(codeDecision, seDecision);
    }

    private boolean isNeglectedEqual() {
        return YStatementJustificationComparator.isNeglectedEqual(codeDecision, seDecision);
    }

    private boolean isAchievingEqual() {
        return YStatementJustificationComparator.isAchievingEqual(codeDecision, seDecision);
    }

    private boolean isAcceptingEqual() {
        return YStatementJustificationComparator.isAcceptingEqual(codeDecision, seDecision);
    }

    private boolean isMoreInformationEqual() {
        return YStatementJustificationComparator.isMoreInformationEqual(codeDecision, seDecision);
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
            StringBuilder sepAndDiff;
            sepAndDiff = new StringBuilder("\n" + SEPARATOR);
            for (String field : lastWrittenFields) {
                sepAndDiff.append("\n").append(field);
            }
            return sepAndDiff.toString();
        }
    }

    private String addHeadIfNeeded(int index) {
        return isPreviousDifferent(index) ? "" : String.format(HEAD, DecisionSourceMapping.getLocalSource(codeDecision.getId())) + "\n";
    }

    private String addTailIfNeeded(int index) {
        return isNextDifferent(index) ? "" : "\n" + String.format(TAIL, DecisionSourceMapping.getRemoteSource(seDecision.getId()));
    }

    private boolean isPreviousDifferent(int index) {
        return index - 1 >= 0 && differences[index - 1];
    }

    private boolean isNextDifferent(int index) {
        return index + 1 < differences.length && differences[index + 1];
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

    public YStatementJustificationWrapper getCodeDecision() {
        return codeDecision;
    }

    public YStatementJustificationWrapper getSeDecision() {
        return seDecision;
    }

    @Override
    public String toString() {
        String compare = String.format("Compare View %s {\n", codeDecision.getId());
        compare += "\t" + getContextComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getFacingComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getChosenComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getNeglectedComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getAchievingComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "\t" + getAcceptingComparisionString().replaceAll("\n", "\n\t") + "\n";
        compare += "}";
        return compare;
    }
}
