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
import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.util.ystatement.YStatementConstants;
import com.eadlsync.util.ystatement.YStatementJustificationComparator;
import com.eadlsync.util.io.JavaDecisionParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
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
    private YStatementJustificationWrapper initialDecision = null;
    private YStatementJustificationWrapper sampleDecision = null;

    private static File sampleFile;
    private static Path sampleFilePath;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void classSetUp() throws IOException {
        sampleFile = folder.newFile();
        sampleFilePath = sampleFile.toPath();
    }

    @Before
    public void setUp() throws IOException {
        DecisionSourceMapping.putLocalSource("my_sample_id", sampleFilePath.toString());
        initialDecision = new YStatementJustificationWrapperBuilder("my_sample_id").
                context("my_context").
                build();
        sampleDecision = new YStatementJustificationWrapperBuilder("my_sample_id").
                context("my_modified_context").
                chosen("my_new_chosen").
                build();
        writeToFile(sampleClassWithAnnotation, sampleFilePath);
    }

    @Test
    public void testParseClass() throws IOException {
        YStatementJustificationWrapper readDecision = JavaDecisionParser.readYStatementFromFile(sampleFilePath);

        Assert.assertTrue(readDecision.toString(), YStatementJustificationComparator.isEqual(initialDecision, readDecision));
    }

    @Test
    public void testModifyAnnotation() throws IOException {
        JavaDecisionParser.writeModifiedYStatementToFile(sampleDecision);

        String content = readFromFile(sampleFilePath);

        Assert.assertTrue(content.contains(sampleDecision.getId()));
        Assert.assertTrue(content.contains(sampleDecision.getContext()));
        Assert.assertTrue(content.contains(sampleDecision.getChosen()));

        Assert.assertFalse(content.contains(YStatementConstants.FACING));
    }

    @Test
    public void testClearAnnotation() throws IOException {
        JavaDecisionParser.removeYStatementFromFile(initialDecision);

        Assert.assertNull(JavaDecisionParser.readYStatementFromFile(sampleFilePath));
    }

    private void writeToFile(String content, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset());
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String readFromFile(Path path) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset());
        String content = reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
        reader.close();
        return content;
    }

}
