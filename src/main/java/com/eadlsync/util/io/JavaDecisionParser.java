package com.eadlsync.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.util.ystatement.YStatementConstants;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import radar.ad.annotations.YStatementJustification;

/**
 * Java decision parser that provides utility methods to add, modify and delete
 * {@link YStatementJustification} annotations from java files.
 */
public class JavaDecisionParser {

    private static final Logger LOG = LoggerFactory.getLogger(JavaDecisionParser.class);

    public static YStatementJustificationWrapper readYStatementFromFile(Path path) throws IOException {
        AnnotationSource annotation;
        try (InputStream stream = Files.newInputStream(path)) {
            final JavaSource javaSource = (JavaSource) Roaster.parse(stream);
            LOG.debug("Read YStatementJustification from {}", path);
            annotation = javaSource.getAnnotation(YStatementJustification.class);
        }
        if (annotation == null) {
            return null;
        }

        YStatementJustificationWrapper decision = createYStatementJustificationFromAnnotationSource
                (annotation);
        LOG.info("Found YStatementJustification {}", decision);
        DecisionSourceMapping.putLocalSource(decision.getId(), path.toString());
        return decision;
    }

    private static YStatementJustificationWrapper createYStatementJustificationFromAnnotationSource
            (AnnotationSource source) {
        String id = source.getStringValue(YStatementConstants.ID);
        String context = source.getStringValue(YStatementConstants.CONTEXT);
        String facing = source.getStringValue(YStatementConstants.FACING);
        String chosen = source.getStringValue(YStatementConstants.CHOSEN);
        String neglected = source.getStringValue(YStatementConstants.NEGLECTED);
        String achieving = source.getStringValue(YStatementConstants.ACHIEVING);
        String accepting = source.getStringValue(YStatementConstants.ACCEPTING);
        return new YStatementJustificationWrapperBuilder(id).context(context).facing(facing).chosen
                (chosen).neglected(neglected).achieving(achieving).accepting(accepting).build();
    }

    public static void writeModifiedYStatementToFile(YStatementJustificationWrapper yStatement) throws
            IOException {
        Path path = Paths.get(DecisionSourceMapping.getLocalSource(yStatement.getId()));
        LOG.debug("Modify YStatementJustification {} from {}", yStatement.getId(), path);
        try (InputStream stream = Files.newInputStream(path)) {
            final JavaSource javaSource = (JavaSource) Roaster.parse(stream);
            AnnotationSource annotation = javaSource.getAnnotation(YStatementJustification.class);
            if (YStatementJustificationComparator.isSameButNotEqual(yStatement,
                    createYStatementJustificationFromAnnotationSource(annotation))) {
                javaSource.removeAnnotation(annotation);
                addYStatementToClassSource(yStatement, javaSource);
                Files.write(path, javaSource.toString().getBytes(Charset.defaultCharset()));
            }
        }
    }

    public static void removeYStatementFromFile(YStatementJustificationWrapper yStatement) throws
            IOException {
        Path path = Paths.get(DecisionSourceMapping.getLocalSource(yStatement.getId()));
        LOG.debug("Remove YStatementJustification {} from {}", yStatement.getId(), path);
        try (InputStream stream = Files.newInputStream(path)) {
            final JavaSource javaSource = (JavaSource) Roaster.parse(stream);
            AnnotationSource annotation = javaSource.getAnnotation(YStatementJustification.class);
            if (YStatementJustificationComparator.isSame(yStatement,
                    createYStatementJustificationFromAnnotationSource(annotation))) {
                javaSource.removeAnnotation(annotation);
                Files.write(path, javaSource.toString().getBytes(Charset.defaultCharset()));
            }
        }
    }

    private static void addYStatementToClassSource(YStatementJustificationWrapper yStatement,
                                                   JavaSource javaSource) {
        LOG.debug("YStatementJustification values {}", yStatement);
        AnnotationSource newAnnotation = javaSource.addAnnotation(YStatementJustification.class);
        newAnnotation.setStringValue(YStatementConstants.ID, yStatement.getId());
        if (!yStatement.getContext().isEmpty())
            newAnnotation.setStringValue(YStatementConstants.CONTEXT, yStatement.getContext());
        if (!yStatement.getFacing().isEmpty())
            newAnnotation.setStringValue(YStatementConstants.FACING, yStatement.getFacing());
        if (!yStatement.getChosen().isEmpty())
            newAnnotation.setStringValue(YStatementConstants.CHOSEN, yStatement.getChosen());
        if (!yStatement.getNeglected().isEmpty())
            newAnnotation.setStringValue(YStatementConstants.NEGLECTED, yStatement.getNeglected());
        if (!yStatement.getAchieving().isEmpty())
            newAnnotation.setStringValue(YStatementConstants.ACHIEVING, yStatement.getAchieving());
        if (!yStatement.getAccepting().isEmpty())
            newAnnotation.setStringValue(YStatementConstants.ACCEPTING, yStatement.getAccepting());
    }

    public static void addYStatementToFile(YStatementJustificationWrapper yStatement) throws
            IOException {
        Path path = Paths.get(DecisionSourceMapping.getLocalSource(yStatement.getId()));
        LOG.debug("Add YStatementJustification {} to {}", yStatement.getId(), path);
        try (InputStream stream = Files.newInputStream(path)) {
            final JavaSource javaSource = (JavaSource) Roaster.parse(stream);
            addYStatementToClassSource(yStatement, javaSource);
            Files.write(path, javaSource.toString().getBytes(Charset.defaultCharset()));
        }
    }

    public static List<YStatementJustificationWrapper> readYStatementsFromDirectory(Path srcPath) throws IOException {
        List<YStatementJustificationWrapper> localYStatements = new ArrayList<>();
        Files.walk(srcPath, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
            if (isPathToJavaFile(path)) {
                try {
                    YStatementJustificationWrapper decision = JavaDecisionParser.readYStatementFromFile(path);
                    if (decision != null) localYStatements.add(decision);
                } catch (IOException e) {
                    LOG.debug("Failed to read annotations, skipping file {}", path);
                }
            }
        });
        return localYStatements;
    }

    private static boolean isPathToJavaFile(Path path) {
        return path.toString().endsWith(".java") && !Files.isDirectory(path);
    }
}
