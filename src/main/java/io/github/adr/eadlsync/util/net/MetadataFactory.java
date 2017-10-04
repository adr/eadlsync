package io.github.adr.eadlsync.util.net;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class MetadataFactory {

    public enum GeneralMetadata {
        AUTHOR("author", ""),
        COMPLEXITY("complexity","1"),
        PHASE("phase","1.0"),
        STATUS("status", "Proposed"),
        STEREOTYPE("stereotype", ""),
        TAGGED_VALUES("taggedValues", ""),
        VERSION("version", "1.0"),

        ORIGINAL_NAME("original_name", "");

        private final String name;
        private final String defaultValue;

        GeneralMetadata(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    public enum TaggedValues {
        INTELLECTUAL_PROPERTY_RIGHTS("Intellectual Property Rights", "Unrestricted"),
        XMODELID("XModelId","ADMentor:3"),

        ORGANISATIONAL_REACH("Organisational Reach","Project"),
        OWNER_ROLE("Owner Role", "Any"),
        PROBLEM_STATE("Problem State", ""),
        OPTION_STATE("Option State", ""),
        STAKEHOLDER_ROLES("Stakeholder Roles", "All");

        private final String name;
        private final String defaultValue;

        TaggedValues(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    public enum Stereotype {
        PROBLEM_OCCURRENCE("Problem Occurrence"),
        OPTION_OCCURRENECE("Option Occurrence"),
        SOLUTION_SPACE_PACKAGE("Solution Space Package");

        private final String name;

        Stereotype(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum OptionState {
        CHOSEN("Chosen"),
        NEGLECTED("Neglected"),
        ELIGIBLE("Eligible");

        private final String name;

        OptionState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public enum ProblemState {
        SOLVED("Solved"),
        NOT_APPLICABLE("Not Applicable");

        private final String name;

        ProblemState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final Map<String, Object> defaultProblemOptionMap = new HashMap<>();
    private static final Map<String, Object> defaultSolutionPackageMap = new HashMap<>();
    private static final Map<String, Object> defaultTaggedValuesMap = new HashMap<>();


    static {
        defaultSolutionPackageMap.put(GeneralMetadata.AUTHOR.getName(), GeneralMetadata.AUTHOR.getDefaultValue());
        defaultSolutionPackageMap.put(GeneralMetadata.COMPLEXITY.getName(), GeneralMetadata.COMPLEXITY.getDefaultValue());
        defaultSolutionPackageMap.put(GeneralMetadata.PHASE.getName(), GeneralMetadata.PHASE.getDefaultValue());
        defaultSolutionPackageMap.put(GeneralMetadata.STATUS.getName(), GeneralMetadata.STATUS.getDefaultValue());

        defaultProblemOptionMap.putAll(defaultSolutionPackageMap);
        defaultProblemOptionMap.put(GeneralMetadata.VERSION.getName(), GeneralMetadata.VERSION.getDefaultValue());

        defaultTaggedValuesMap.put(TaggedValues.INTELLECTUAL_PROPERTY_RIGHTS.getName(), TaggedValues.INTELLECTUAL_PROPERTY_RIGHTS.getDefaultValue());
        defaultTaggedValuesMap.put(TaggedValues.XMODELID.getName(), TaggedValues.XMODELID.getDefaultValue());
    }

    public static Map<String, Object> getOptionMap(OptionState optionState) {
        Map<String, Object> optionMap = new HashMap<>(defaultProblemOptionMap);
        optionMap.put(GeneralMetadata.STEREOTYPE.getName(), Stereotype.OPTION_OCCURRENECE.getName());
        Map<String, Object> optionTaggedValuesMap = new HashMap<>(defaultTaggedValuesMap);
        optionTaggedValuesMap.put(TaggedValues.OPTION_STATE.getName(), optionState.getName());
        optionMap.put(GeneralMetadata.TAGGED_VALUES.getName(), optionTaggedValuesMap);
        return optionMap;
    }

    public static Map<String, Object> getProblemMap(ProblemState problemState) {
        Map<String, Object> problemMap = new HashMap<>(defaultProblemOptionMap);
        problemMap.put(GeneralMetadata.STEREOTYPE.getName(), Stereotype.PROBLEM_OCCURRENCE.getName());
        Map<String, Object> problemTaggedValuesMap = new HashMap<>(defaultTaggedValuesMap);
        problemTaggedValuesMap.put(TaggedValues.ORGANISATIONAL_REACH.getName(), TaggedValues.ORGANISATIONAL_REACH.getDefaultValue());
        problemTaggedValuesMap.put(TaggedValues.OWNER_ROLE.getName(), TaggedValues.OWNER_ROLE.getDefaultValue());
        problemTaggedValuesMap.put(TaggedValues.PROBLEM_STATE.getName(), problemState.getName());
        problemTaggedValuesMap.put(TaggedValues.STAKEHOLDER_ROLES.getName(), TaggedValues.STAKEHOLDER_ROLES.getDefaultValue());
        problemMap.put(GeneralMetadata.TAGGED_VALUES.getName(), problemTaggedValuesMap);
        return problemMap;
    }

    public static Map<String, Object> getSolutionSpacePackageMap(String name) {
        Map<String, Object> solutionSpacePackageMap = new HashMap<>(defaultSolutionPackageMap);
        solutionSpacePackageMap.put(GeneralMetadata.STEREOTYPE.getName(), Stereotype.SOLUTION_SPACE_PACKAGE.getName());
        solutionSpacePackageMap.put(GeneralMetadata.ORIGINAL_NAME.getName(), name);
        solutionSpacePackageMap.put(GeneralMetadata.TAGGED_VALUES.getName(), defaultTaggedValuesMap);
        return solutionSpacePackageMap;
    }

}
