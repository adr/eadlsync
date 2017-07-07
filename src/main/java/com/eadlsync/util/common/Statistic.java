package com.eadlsync.util.common;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.io.JavaDecisionParser;
import com.eadlsync.util.ystatement.YStatementField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.eadlsync.util.ystatement.YStatementConstants.DELIMITER;
import static com.eadlsync.util.ystatement.YStatementField.*;

/**
 * Utility class to provide statistics about embedded architectural decisions of a project.
 */
public class Statistic {

    private static final Logger LOG = LoggerFactory.getLogger(Statistic.class);
    private final Path root;

    private List<Integer> sumContext = new ArrayList<>();
    private List<Integer> sumFacing = new ArrayList<>();
    private List<Integer> sumNeglected = new ArrayList<>();
    private List<Integer> sumAchieving = new ArrayList<>();
    private List<Integer> sumAccepting = new ArrayList<>();

    private HashMap<String, Integer> decisionsInPackage = new HashMap<>();
    private HashMap<String, Integer> classesInPackage = new HashMap<>();

    private int classes = 0;
    private int packages = 0;
    private int decisions = 0;

    private HashMap<YStatementField, Integer> decisionsWithoutField = new HashMap<>();
    private HashMap<YStatementField, Float> avgFieldLengthOrOptions = new HashMap<>();
    private HashMap<YStatementField, Float> medFieldLengthOrOptions = new HashMap<>();
    private HashMap<YStatementField, Integer> minFieldLengthOrOptions = new HashMap<>();
    private HashMap<YStatementField, Integer> maxFieldLengthOrOptions = new HashMap<>();

    public Statistic(String path) {
        root = Paths.get(path);
        for (YStatementField field : YStatementField.values()) {
            decisionsWithoutField.put(field, 0);
            avgFieldLengthOrOptions.put(field, 0f);
            medFieldLengthOrOptions.put(field, 0f);
            minFieldLengthOrOptions.put(field, 0);
            maxFieldLengthOrOptions.put(field, 0);
        }
    }

    public void collectStats() throws IOException {
        final String[] currentFolder = {""};
        Files.walk(root, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
            String folder = root.relativize(path).toString();
            folder = folder.isEmpty() ? "<no package>" : folder;

            if (Files.isDirectory(path)) {
                currentFolder[0] = path.toString();
                classesInPackage.put(folder, 0);
                decisionsInPackage.put(folder, 0);
                packages++;
            }
            if (isPathToJavaFile(path)) {
                folder = root.relativize((path.getParent())).toString();
                folder = folder.isEmpty() ? "<no package>" : folder;
                classesInPackage.put(folder, classesInPackage.get(folder) + 1);
                try {
                    YStatementJustificationWrapper decision = JavaDecisionParser.readYStatementFromFile(path);
                    if (decision != null) {
                        decisionsInPackage.put(folder, decisionsInPackage.get(folder) + 1);
                        decisions++;
                        if (!decision.getContext().isEmpty()) {
                            sumContext.add(decision.getContext().length());
                        }
                        if (!decision.getFacing().isEmpty()) {
                            sumFacing.add(decision.getFacing().length());
                        }
                        if (!decision.getNeglected().isEmpty()) {
                            sumNeglected.add(decision.getNeglected().split(DELIMITER).length);
                        }
                        if (!decision.getAchieving().isEmpty()) {
                            sumAchieving.add(decision.getAchieving().length());
                        }
                        if (!decision.getAccepting().isEmpty()) {
                            sumAccepting.add(decision.getAccepting().length());
                        }
                    }
                    classes++;
                } catch (IOException e) {
                    LOG.debug("Failed to read annotations, skipping file {}", path);
                }
            }
        });
    }

    private boolean isPathToJavaFile(Path path) {
        return path.toString().endsWith(".java") && !Files.isDirectory(path);
    }

    public void processStats() {
        if (decisions != 0) {
            decisionsWithoutField.put(CONTEXT, decisions - sumContext.size());
            decisionsWithoutField.put(FACING, decisions - sumFacing.size());
            decisionsWithoutField.put(ACHIEVING, decisions - sumAchieving.size());
            decisionsWithoutField.put(ACCEPTING, decisions - sumAccepting.size());

            avgFieldLengthOrOptions.put(CONTEXT, calculateAverage(sumContext));
            avgFieldLengthOrOptions.put(FACING, calculateAverage(sumFacing));
            avgFieldLengthOrOptions.put(NEGLECTED, calculateAverage(sumNeglected));
            avgFieldLengthOrOptions.put(ACHIEVING, calculateAverage(sumAchieving));
            avgFieldLengthOrOptions.put(ACCEPTING, calculateAverage(sumAccepting));

            medFieldLengthOrOptions.put(CONTEXT, calculateMedian(sumContext));
            medFieldLengthOrOptions.put(FACING, calculateMedian(sumFacing));
            medFieldLengthOrOptions.put(NEGLECTED, calculateMedian(sumNeglected));
            medFieldLengthOrOptions.put(ACHIEVING, calculateMedian(sumAchieving));
            medFieldLengthOrOptions.put(ACCEPTING, calculateMedian(sumAccepting));

            minFieldLengthOrOptions.put(CONTEXT, calculateMin(sumContext));
            minFieldLengthOrOptions.put(FACING, calculateMin(sumFacing));
            minFieldLengthOrOptions.put(NEGLECTED, calculateMin(sumNeglected));
            minFieldLengthOrOptions.put(ACHIEVING, calculateMin(sumAchieving));
            minFieldLengthOrOptions.put(ACCEPTING, calculateMin(sumAccepting));

            maxFieldLengthOrOptions.put(CONTEXT, calculateMax(sumContext));
            maxFieldLengthOrOptions.put(FACING, calculateMax(sumFacing));
            maxFieldLengthOrOptions.put(NEGLECTED, calculateMax(sumNeglected));
            maxFieldLengthOrOptions.put(ACHIEVING, calculateMax(sumAchieving));
            maxFieldLengthOrOptions.put(ACCEPTING, calculateMax(sumAccepting));
        }

    }

    private float calculateAverage(List<Integer> items) {
        if (decisions != 0) {
            return items.stream().reduce((x, y) -> x + y).orElse(0) / decisions;
        }
        return 0;
    }

    private float calculateMedian(List<Integer> items) {
        if (items.size() == 0) {
            return 0;
        }
        Collections.sort(items);
        int middle = items.size() / 2;
        if (items.size() % 2 == 1) {
            return items.get(middle);
        } else {
            return (items.get(middle - 1) + items.get(middle)) / 2;
        }
    }

    private int calculateMin(List<Integer> items) {
        Collections.sort(items);
        return (items.size() > 0) ? items.get(0) : 0;
    }

    private int calculateMax(List<Integer> items) {
        Collections.sort(items);
        int size = items.size();
        return (size > 0) ? items.get(size - 1) : 0;
    }

    public HashMap<String, Integer> getDecisionsInPackage() {
        return decisionsInPackage;
    }

    public HashMap<String, Integer> getClassesInPackage() {
        return classesInPackage;
    }

    public int getClasses() {
        return classes;
    }

    public int getPackages() {
        return packages;
    }

    public int getDecisions() {
        return decisions;
    }

    public HashMap<YStatementField, Integer> getDecisionsWithoutField() {
        return decisionsWithoutField;
    }

    public HashMap<YStatementField, Float> getAvgFieldLengthOrOptions() {
        return avgFieldLengthOrOptions;
    }

    public HashMap<YStatementField, Float> getMedFieldLengthOrOptions() {
        return medFieldLengthOrOptions;
    }

    public HashMap<YStatementField, Integer> getMinFieldLengthOrOptions() {
        return minFieldLengthOrOptions;
    }

    public HashMap<YStatementField, Integer> getMaxFieldLengthOrOptions() {
        return maxFieldLengthOrOptions;
    }
}
