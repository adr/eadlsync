package com.eadlsync.util.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.util.ystatement.YStatementConstants;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import radar.ad.annotations.YStatementJustification;

/**
 * Created by Tobias on 16.05.2017.
 */
public class JavaDecisionParser {

    private static final Logger LOG = LoggerFactory.getLogger(JavaDecisionParser.class);

    public static YStatementJustificationWrapper readYStatementFromFile(Path path) throws IOException {
        final JavaClassSource javaClass = (JavaClassSource) Roaster.parse(Files.newInputStream(path));
        LOG.debug("Read YStatementJustification from {}", path);
        AnnotationSource annotation = javaClass.getAnnotation(YStatementJustification.class);
        if (annotation == null) {
            return null;
        }

        YStatementJustificationWrapper decision = createYStatementJustificationFromAnnotationSource(annotation);
        DecisionSourceMapping.putLocalSource(decision.getId(), path.toString());
        return decision;
    }

    private static YStatementJustificationWrapper createYStatementJustificationFromAnnotationSource(AnnotationSource source) {
        String id = source.getStringValue(YStatementConstants.ID);
        String context = source.getStringValue(YStatementConstants.CONTEXT);
        String facing = source.getStringValue(YStatementConstants.FACING);
        String chosen = source.getStringValue(YStatementConstants.CHOSEN);
        String neglected = source.getStringValue(YStatementConstants.NEGLECTED);
        String achieving = source.getStringValue(YStatementConstants.ACHIEVING);
        String accepting = source.getStringValue(YStatementConstants.ACCEPTING);
        return new YStatementJustificationWrapperBuilder(id).context(context).facing
                (facing).chosen(chosen).neglected(neglected).achieving(achieving).accepting(accepting)
                .build();
    }

    public static void writeModifiedYStatementToFile(YStatementJustificationWrapper yStatement) throws IOException {
        Path path = Paths.get(DecisionSourceMapping.getLocalSource(yStatement.getId()));
        LOG.debug("Modify YStatementJustification {} from {}", yStatement.getId(), path);
        final JavaClassSource javaClass = (JavaClassSource) Roaster.parse(Files.newInputStream(path));
        AnnotationSource annotation = javaClass.getAnnotation(YStatementJustification.class);
        if (YStatementJustificationComparator.isSameButNotEqual(yStatement, createYStatementJustificationFromAnnotationSource(annotation))) {
            javaClass.removeAnnotation(annotation);
            addYStatementToClassSource(yStatement, javaClass);
            Files.write(path, javaClass.toString().getBytes(Charset.defaultCharset()));
        }
    }

    public static void removeYStatementFromFile(YStatementJustificationWrapper yStatement) throws IOException {
        Path path = Paths.get(DecisionSourceMapping.getLocalSource(yStatement.getId()));
        LOG.debug("Remove YStatementJustification {} from {}", yStatement.getId(), path);
        final JavaClassSource javaClass = (JavaClassSource) Roaster.parse(Files.newInputStream(path));
        AnnotationSource annotation = javaClass.getAnnotation(YStatementJustification.class);
        if (annotation.getStringValue(YStatementConstants.ID).equals(yStatement.getId())) {
            javaClass.removeAnnotation(annotation);
            Files.write(path, javaClass.toString().getBytes(Charset.defaultCharset()));
        }
    }

    private static void addYStatementToClassSource(YStatementJustificationWrapper yStatement,
                                                   JavaClassSource javaClass) {
        LOG.debug("YStatementJustification values {}", yStatement);
        AnnotationSource newAnnotation = javaClass.addAnnotation(YStatementJustification.class);
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
}
