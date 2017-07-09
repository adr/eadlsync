package com.eadlsync;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.eadlsync.data.YStatementTestData;
import com.eadlsync.model.decision.DecisionSourceMapping;
import com.eadlsync.model.decision.YStatementJustificationWrapper;
import com.eadlsync.util.io.JavaDecisionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mocks a code repository and provides easy access methods to write eadls
 */
public class CodeRepoMock extends YStatementTestData {

    private static final Logger LOG = LoggerFactory.getLogger(CodeRepoMock.class);

    private final Path CODE_BASE;

    public CodeRepoMock(Path codeBase) {
        this.CODE_BASE = codeBase;
    }

    public List<YStatementJustificationWrapper> readLocalDecisions() throws IOException {
        List<YStatementJustificationWrapper> localDecisions = new ArrayList<>();
        Files.walk(CODE_BASE, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
            try {
                if (Files.isRegularFile(path)) {
                    localDecisions.add(JavaDecisionParser.readYStatementFromFile(path));
                }
            } catch (IOException e) {
                LOG.debug("Error reading decisions from file.", e);
            }
        });
        return localDecisions;
    }

    public void createClassesForEadls(List<YStatementJustificationWrapper> eadls) throws IOException {
        for (int i = 0; i < eadls.size(); i++) {
            YStatementJustificationWrapper yStatementJustificationWrapper = eadls.get(i);
            createClassWithEadl("DecisionClass" + UUID.randomUUID(), yStatementJustificationWrapper);
        }
    }

    private void createClassWithEadl(String javaClassName, YStatementJustificationWrapper eadl) throws
            IOException {
        Path path = CODE_BASE.resolve(String.format("%s.java", javaClassName));
        Files.write(path, String.format("public class %s {\n}", javaClassName).getBytes());
        DecisionSourceMapping.putLocalSource(eadl.getId(), path.toString());
        JavaDecisionParser.addYStatementToFile(eadl);
    }
}
