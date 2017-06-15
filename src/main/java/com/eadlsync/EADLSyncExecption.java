package com.eadlsync;

/**
 *
 */
public class EADLSyncExecption extends Exception {

    private EADLSyncOperationState state = EADLSyncOperationState.NONE;

    public EADLSyncExecption(EADLSyncOperationState state) {
        super(state.getMessage());
        this.state = state;
    }

    public enum EADLSyncOperationState {
        NON_FORWARD("[NON FORWARD] The se-repo has differed from the base revision, please pull first or use the force commit option"),
        CONFLICT("[CONFLICT] Auto merge failed because the local and the remote diff have merge conflicts"),
        UP_TO_DATE("[UP TO DATE] the local decisions are already up to date."),
        NOTHING_TO_COMMIT("[NOTHING TO COMMIT] no local changes detected, the se-repo is already up to date"),
        SYNC_FAILED("[SYNC FAILED] autmatic sync failed due to conflicts, please use pull and commit commands"),
        NONE("");

        private final String message;

        EADLSyncOperationState(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static EADLSyncExecption ofState(EADLSyncOperationState state) {
        return new EADLSyncExecption(state);
    }

    public EADLSyncOperationState getState() {
        return this.state;
    }

}
