package com.eadlsync.cli;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import static com.eadlsync.util.ystatement.YStatementConstants.COMMIT_ID_PATTERN;

/**
 * Validates if the given value of the command line parameter matches the pattern for a valid commit
 * id of the se-repo.
 */
public class CommitIdValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!COMMIT_ID_PATTERN.matcher(value).matches()) {
            throw new ParameterException(String.format("The Parameter %s does not match the required "
                    + "pattern for a commit id of the se-repo (found %s)", name, value));
        }
    }
}
