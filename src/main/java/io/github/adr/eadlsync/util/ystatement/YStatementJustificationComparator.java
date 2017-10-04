package io.github.adr.eadlsync.util.ystatement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Utility class to compare {@link YStatementJustificationWrapper} and their fields.
 * Should always used instead of equals.
 */
public class YStatementJustificationComparator {

    public static boolean isContextEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isContextEqual(codeDecision.getContext(), seDecision.getContext());
    }

    public static boolean isContextEqual(String context, String differentContext) {
        return context.trim().equals(differentContext.trim());
    }

    public static boolean isFacingEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isFacingEqual(codeDecision.getFacing(), seDecision.getFacing());
    }

    public static boolean isFacingEqual(String facing, String differentFacing) {
        return facing.trim().equals(differentFacing.trim());
    }

    public static boolean isChosenEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isChosenEqual(codeDecision.getChosen(), seDecision.getChosen());
    }

    public static boolean isChosenEqual(String chosen, String differentChosen) {
        return chosen.trim().equals(differentChosen.trim());
    }

    public static boolean isNeglectedEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isNeglectedEqual(codeDecision.getNeglected(), seDecision.getNeglected());
    }

    public static boolean isNeglectedEqual(String neglected, String differentNeglected) {
        List<String> neglectedOptions = trimmedArrayOfString(neglected);
        List<String> differentNeglectedOptions = trimmedArrayOfString(differentNeglected);
        return CollectionUtils.isEqualCollection(neglectedOptions, differentNeglectedOptions);

    }

    private static List<String> trimmedArrayOfString(String input) {
        return Stream.of(input.split(YStatementConstants.DELIMITER)).sorted().map(String::trim).collect(Collectors.toList());
    }

    public static boolean isAchievingEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isAchievingEqual(codeDecision.getAchieving(), seDecision.getAchieving());
    }

    public static boolean isAchievingEqual(String achieving, String differentAchieving) {
        return achieving.trim().equals(differentAchieving.trim());
    }

    public static boolean isAcceptingEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isAcceptingEqual(codeDecision.getAccepting(), seDecision.getAccepting());
    }

    public static boolean isAcceptingEqual(String accepting, String differentAccepting) {
        return accepting.trim().equals(differentAccepting.trim());
    }

    public static boolean isMoreInformationEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isMoreInformationEqual(codeDecision.getMoreInformation(), seDecision.getMoreInformation());
    }

    public static boolean isMoreInformationEqual(String moreInformation, String differentMoreInformation) {
        return moreInformation.trim().equals(differentMoreInformation.trim());
    }

    /**
     * Checks if the id of the given objects are the same.
     *
     * @param codeDecision as first decision
     * @param seDecision as second decision
     * @return true if both objects have the same id and field values
     */
    public static boolean isEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isSame(codeDecision, seDecision) && isContextEqual(codeDecision, seDecision) &&
                isFacingEqual(codeDecision, seDecision) && isChosenEqual(codeDecision, seDecision) &&
                isNeglectedEqual(codeDecision, seDecision) && isAchievingEqual(codeDecision, seDecision) &&
                isAcceptingEqual(codeDecision, seDecision);
    }

    /**
     * Checks if the id of the given objects are the same.
     *
     * @param codeDecision as first decision
     * @param seDecision as second decision
     * @return true if both objects have the same id
     */
    public static boolean isSame(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        if (codeDecision == null) {
            return seDecision == null;
        } else {
            if (seDecision == null) {
                return false;
            }
        }
        return codeDecision.getId().equals(seDecision.getId());

    }

    /**
     * Checks whether the two objects have the same id but different field values.
     *
     * @param codeDecision as first decision
     * @param seDecision as second decision
     * @return true if both objects have the same id but different field values
     */
    public static boolean isSameButNotEqual(YStatementJustificationWrapper codeDecision, YStatementJustificationWrapper seDecision) {
        return isSame(codeDecision, seDecision) && !isEqual(codeDecision, seDecision);
    }
}
