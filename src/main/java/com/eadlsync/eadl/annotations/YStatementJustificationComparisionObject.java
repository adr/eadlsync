package com.eadlsync.eadl.annotations;

/**
 * Created by Tobias on 13.05.2017.
 */
public class YStatementJustificationComparisionObject {

    private YStatementJustificationWrapper codeDecision;
    private YStatementJustificationWrapper seDecision;

    public YStatementJustificationComparisionObject(YStatementJustificationWrapper codeDecision,
                                                    YStatementJustificationWrapper seDecision) {
        this.codeDecision = codeDecision;
        this.seDecision = seDecision;
    }

    public boolean isEqual() {
        return codeDecision != null && codeDecision.equals(seDecision);
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

    private String getContextComparisionString() {
        String comp;
        if (isContextEqual()) {
            comp = "context='" + codeDecision.getContext() + "'";
        } else {
            comp = "**code-context='" + codeDecision.getContext() + "'";
            comp += "**se-context  ='" + seDecision.getContext() + "'";
        }
        return comp;
    }

    private String getFacingComparisionString() {
        String comp;
        if (isFacingEqual()) {
            comp = "facing='" + codeDecision.getFacing() + "'";
        } else {
            comp = "**code-facing='" + codeDecision.getFacing() + "'";
            comp += "**se-facing  ='" + seDecision.getFacing() + "'";
        }
        return comp;
    }

    private String getChosenComparisionString() {
        String comp;
        if (isChosenEqual()) {
            comp = "chosen='" + codeDecision.getChosen() + "'";
        } else {
            comp = "**code-chosen='" + codeDecision.getChosen() + "'";
            comp += "**se-chosen  ='" + seDecision.getChosen() + "'";
        }
        return comp;
    }

    private String getNeglectedComparisionString() {
        String comp;
        if (isNeglectedEqual()) {
            comp = "neglected='" + codeDecision.getNeglected() + "'";
        } else {
            comp = "**code-neglected='" + codeDecision.getNeglected() + "'";
            comp += "**se-neglected  ='" + seDecision.getNeglected() + "'";
        }
        return comp;
    }

    private String getAchievingComparisionString() {
        String comp;
        if (isAchievingEqual()) {
            comp = "context='" + codeDecision.getContext() + "'";
        } else {
            comp = "**code-context='" + codeDecision.getContext() + "'";
            comp += "**se-context  ='" + seDecision.getContext() + "'";
        }
        return comp;
    }

    private String getAcceptingComparisionString() {
        String comp;
        if (isAcceptingEqual()) {
            comp = "accepting='" + codeDecision.getAccepting() + "'";
        } else {
            comp = "**code-accepting='" + codeDecision.getAccepting() + "'";
            comp += "**se-accepting  ='" + seDecision.getAccepting() + "'";
        }
        return comp;
    }

    private String getMoreInformationComparisionString() {
        String comp;
        if (isMoreInformationEqual()) {
            comp = "moreInformation='" + codeDecision.getMoreInformation() + "'";
        } else {
            comp = "**code-moreInformation='" + codeDecision.getMoreInformation() + "'";
            comp += "**se-moreInformation  ='" + seDecision.getMoreInformation() + "'";
        }
        return comp;
    }

    @Override
    public String toString() {
        String compare = "Compare View{";
        compare += "\n\tid='" + codeDecision.getId() + "'";
        compare += "\n\t" + getContextComparisionString();
        compare += "\n\t" + getFacingComparisionString();
        compare += "\n\t" + getChosenComparisionString();
        compare += "\n\t" + getNeglectedComparisionString();
        compare += "\n\t" + getAchievingComparisionString();
        compare += "\n\t" + getAcceptingComparisionString();
        compare += "\n\t" + getMoreInformationComparisionString();
        compare += "}\n";
        return compare;
    }
}
