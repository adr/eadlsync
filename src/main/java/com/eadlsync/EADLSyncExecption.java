package com.eadlsync;

/**
 *
 */
public class EADLSyncExecption extends Exception {

    public EADLSyncExecption(String message) {
        super(message);
    }

    public enum EADLSyncOperationState {
        FORWARD("[FORWARD] committing to the se-repo"),
        NON_FORWARD("[NON FORWARD] the latest commit of the se-repo is ahead of the local changes."),
        UP_TO_DATE("[UP TO DATE] the local decisions are already up to date."),
        NOTHING_TO_COMMIT("[NOTHING TO COMMIT] the decisions in the se-repo match with the local decisions");

        private final String message;

        EADLSyncOperationState(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static EADLSyncExecption ofState(EADLSyncOperationState state) {
        return new EADLSyncExecption(state.getMessage());
    }

}
