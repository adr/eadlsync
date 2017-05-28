package com.eadlsync.util.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.YStatementConstants;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 * Created by Tobias on 16.05.2017.
 */
public class JavaDecisionParser {

    public static YStatementJustificationWrapper readModifiedYStatementFromFile(Path path) {
        // TODO: evaluate if this is better option than using java class loader
        return null;
    }

    public static void writeModifiedYStatementToFile(YStatementJustificationWrapper yStatement, Path
            path) throws IOException {
        final JavaClassSource javaClass = (JavaClassSource) Roaster.parse(Files.newInputStream(path));
        AnnotationSource annotation = javaClass.getAnnotation(YStatementJustification.class);
        if (annotation.getStringValue(YStatementConstants.ID).equals(yStatement.getId())) {
            // TODO: check if annotation sourec has same fields and values as yStatement
            // YStatementComparator.equals(annotation, yStatement);
            javaClass.removeAnnotation(annotation);
            addYStatement(yStatement, javaClass);
            Files.write(path, javaClass.toString().getBytes(Charset.defaultCharset()));
        }
    }

    private static void addYStatement(YStatementJustificationWrapper yStatement, JavaClassSource
            javaClass) {
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
        //        if (!yStatement.getMoreInformation().isEmpty())
        //            newAnnotation.setStringValue(YStatementConstants.MORE_INFORMATION);
    }
}
