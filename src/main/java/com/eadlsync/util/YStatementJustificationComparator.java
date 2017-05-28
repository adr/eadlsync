package com.eadlsync.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Created by Tobias on 28.05.2017.
 */
public class YStatementJustificationComparator {

    private static final String delimiter = ",";

    public static boolean isContextEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isContextEqual(wrapper.getContext(), differentWrapper.getContext());
    }

    public static boolean isContextEqual(String context, String differentContext) {
        return context.trim().equals(differentContext.trim());
    }

    public static boolean isFacingEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isFacingEqual(wrapper.getFacing(), differentWrapper.getFacing());
    }

    public static boolean isFacingEqual(String facing, String differentFacing) {
        return facing.trim().equals(differentFacing.trim());
    }

    public static boolean isChosenEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isChosenEqual(wrapper.getChosen(), differentWrapper.getChosen());
    }

    public static boolean isChosenEqual(String chosen, String differentChosen) {
        return chosen.trim().equals(differentChosen.trim());
    }

    public static boolean isNeglectedEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isNeglectedEqual(wrapper.getNeglected(), differentWrapper.getNeglected());
    }

    public static boolean isNeglectedEqual(String neglected, String differentNeglected) {
        List<String> neglectedOptions = trimmedArrayOfString(neglected);
        List<String> differentNeglectedOptions = trimmedArrayOfString(differentNeglected);
        return CollectionUtils.isEqualCollection(neglectedOptions, differentNeglectedOptions);

    }

    private static List<String> trimmedArrayOfString(String input) {
        return Stream.of(input.split(delimiter)).sorted().map(String::trim).collect(Collectors.toList());
    }

    public static boolean isAchievingEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isAchievingEqual(wrapper.getAchieving(), differentWrapper.getAchieving());
    }

    public static boolean isAchievingEqual(String achieving, String differentAchieving) {
        return achieving.trim().equals(differentAchieving.trim());
    }

    public static boolean isAcceptingEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isAcceptingEqual(wrapper.getAccepting(), differentWrapper.getAccepting());
    }

    public static boolean isAcceptingEqual(String accepting, String differentAccepting) {
        return accepting.trim().equals(differentAccepting.trim());
    }

    public static boolean isMoreInformationEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return isMoreInformationEqual(wrapper.getMoreInformation(), differentWrapper.getMoreInformation());
    }

    public static boolean isMoreInformationEqual(String moreInformation, String differentMoreInformation) {
        return moreInformation.trim().equals(differentMoreInformation.trim());
    }

    public static boolean isEqual(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return wrapper.equals(differentWrapper);
    }

    public static boolean isSame(YStatementJustificationWrapper wrapper, YStatementJustificationWrapper differentWrapper) {
        return wrapper.getId().equals(differentWrapper.getId());
    }

}
