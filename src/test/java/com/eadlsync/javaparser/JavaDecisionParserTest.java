package com.eadlsync.javaparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import com.eadlsync.util.OS;
import com.eadlsync.util.YStatement;
import com.eadlsync.util.io.JavaDecisionParser;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Created by Tobias on 15.05.2017.
 */
public class JavaDecisionParserTest {

    private final String sampleClassWithAnnotation =
            "package com.sample.project;\n" +
            "import com.eadlsync.eadl.annotations.YStatementJustification;\n" +
            "@YStatementJustification( id=\"my_sample_id\", context=\"my_context\" )\n" +
            "public class SampleClass {\n" +
            "}";
    private final YStatementJustificationWrapper sampleDecision;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public JavaDecisionParserTest() {
        sampleDecision = new YStatementJustificationWrapperBuilder("my_sample_id").
                context("my_modified_context").chosen("my_new_chosen").build();
    }

    @Test
    public void testModifyAnnotation() throws IOException {
        File srcFile = folder.newFile();
        Path srcPath = srcFile.toPath();

        writeToFile(sampleClassWithAnnotation, srcPath);

        JavaDecisionParser.writeModifiedYStatementToFile(sampleDecision, srcPath);

        String content = readFromFile(srcPath);

        Assert.assertTrue(content.contains(sampleDecision.getId()));
        Assert.assertTrue(content.contains(sampleDecision.getContext()));
        Assert.assertTrue(content.contains(sampleDecision.getChosen()));

        Assert.assertFalse(content.contains(YStatement.FACING));
    }

    private void writeToFile(String content, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset());
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String readFromFile(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());
        String content = reader.lines().collect(Collectors.joining(OS.LS));
        reader.close();
        return content;
    }

}
