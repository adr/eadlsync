package com.eadlsync.util.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.eadlsync.eadl.annotations.YStatementJustification;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.util.YStatementConstants;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 * Created by Tobias on 16.05.2017.
 */
public class JavaDecisionParser {

    public static YStatementJustificationWrapper readModifiedYStatementFromFile(Path path) throws
            IOException {
        // TODO: evaluate if roaster is better option for parsing java classes than the java class loader
        final JavaClassSource javaClass = (JavaClassSource) Roaster.parse(Files.newInputStream(path));
        AnnotationSource annotation = javaClass.getAnnotation(YStatementJustification.class);
        String id = annotation.getStringValue(YStatementConstants.ID);
        String context = annotation.getStringValue(YStatementConstants.CONTEXT);
        String facing = annotation.getStringValue(YStatementConstants.FACING);
        String chosen = annotation.getStringValue(YStatementConstants.CHOSEN);
        String neglected = annotation.getStringValue(YStatementConstants.NEGLECTED);
        String achieving = annotation.getStringValue(YStatementConstants.ACHIEVING);
        String accepting = annotation.getStringValue(YStatementConstants.ACCEPTING);
        return new YStatementJustificationWrapperBuilder(id, path.toString()).context(context).facing
                (facing).chosen(chosen).neglected(neglected).achieving(achieving).accepting(accepting)
                .build();
    }

    public static void writeModifiedYStatementToFile(YStatementJustificationWrapper yStatement, Path
            path) throws IOException {
        final JavaClassSource javaClass = (JavaClassSource) Roaster.parse(Files.newInputStream(path));
        AnnotationSource annotation = javaClass.getAnnotation(YStatementJustification.class);
        if (annotation.getStringValue(YStatementConstants.ID).equals(yStatement.getId())) {
            // TODO: check if annotation soure has same fields and values as yStatement
            //YStatementJustificationComparator.equals(annotation, yStatement);
            javaClass.removeAnnotation(annotation);
            addYStatementToClassSource(yStatement, javaClass);
            Files.write(path, javaClass.toString().getBytes(Charset.defaultCharset()));
        }
    }

    private static void addYStatementToClassSource(YStatementJustificationWrapper yStatement,
                                                   JavaClassSource javaClass) {
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
