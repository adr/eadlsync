package com.eadlsync;

import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.io.JavaDecisionParser;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Mocks a code repository and provides easy access methods to get a clean working state
 */
public class CodeRepoMock extends YStatementTestData {

    private final Path ROOT = Paths.get("").resolve("test-project");

    public void createCodeRepo() throws IOException {
        Files.createDirectory(ROOT);
    }

    public void deleteCodeRepo() throws IOException {
        FileUtils.deleteDirectory(ROOT.toFile());
    }

    public void createClassesForEadls(List<YStatementJustificationWrapper> eadls)
            throws IOException {
        for (int i = 0; i < eadls.size(); i++) {
            YStatementJustificationWrapper yStatementJustificationWrapper = eadls.get(i);
            createClassWithEadl("DecisionClass" + i, yStatementJustificationWrapper);
        }
    }

    private void createClassWithEadl(String javaClassName, YStatementJustificationWrapper eadl)
            throws IOException {
        String existingPath = DecisionSourceMapping.getLocalSource(eadl.getId());
        Path path;
        if (existingPath == null) {
            path = ROOT.resolve(String.format("%s.java", javaClassName));
            writeToFile(String.format("public class %s {\n}", javaClassName), path);
            DecisionSourceMapping.putLocalSource(eadl.getId(), path.toString());
            JavaDecisionParser.addYStatementToFile(eadl);
        } else {
            JavaDecisionParser.writeModifiedYStatementToFile(eadl);
        }
    }

    private void writeToFile(String content, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset());
        writer.write(content);
        writer.flush();
        writer.close();
    }
}
