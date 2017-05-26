package com.eadlsync.util.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.YStatementFields;
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
        if (annotation.getStringValue(YStatementFields.ID).equals(yStatement.getId())) {
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
        newAnnotation.setStringValue(YStatementFields.ID, yStatement.getId());
        if (!yStatement.getContext().isEmpty())
            newAnnotation.setStringValue(YStatementFields.CONTEXT, yStatement.getContext());
        if (!yStatement.getFacing().isEmpty())
            newAnnotation.setStringValue(YStatementFields.FACING, yStatement.getFacing());
        if (!yStatement.getChosen().isEmpty())
            newAnnotation.setStringValue(YStatementFields.CHOSEN, yStatement.getChosen());
        if (!yStatement.getNeglected().isEmpty())
            newAnnotation.setStringValue(YStatementFields.NEGLECTED, yStatement.getNeglected());
        if (!yStatement.getAchieving().isEmpty())
            newAnnotation.setStringValue(YStatementFields.ACHIEVING, yStatement.getAchieving());
        if (!yStatement.getAccepting().isEmpty())
            newAnnotation.setStringValue(YStatementFields.ACCEPTING, yStatement.getAccepting());
        //        if (!yStatement.getMoreInformation().isEmpty())
        //            newAnnotation.setStringValue(YStatementFields.MORE_INFORMATION);
    }
}
