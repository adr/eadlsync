package com.eadlsync;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.io.JavaDecisionParser;
import org.apache.commons.io.FileUtils;

/**
 *
 */
public class CodeRepoMock extends YStatementTestData {

    private final Path ROOT = Paths.get("").resolve("test-project");
    private final String CLASS_TEMPLATE = "public class %s {\n}";

    public void createCodeRepo() throws IOException {
        Files.createDirectory(ROOT);
    }

    public void deleteCodeRepo() throws IOException {
        FileUtils.deleteDirectory(ROOT.toFile());
    }

    public void createClassWithEadl(String javaClassName, YStatementJustificationWrapper eadl)
            throws IOException {
        Path path = ROOT.resolve(String.format("%s.java", javaClassName));
        writeToFile(String.format(CLASS_TEMPLATE, javaClassName), path);
        DecisionSourceMapping.putLocalSource(eadl.getId(), path.toString());
        JavaDecisionParser.addYStatementToFile(eadl);
    }

    private void writeToFile(String content, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset());
        writer.write(content);
        writer.flush();
        writer.close();
    }
}
