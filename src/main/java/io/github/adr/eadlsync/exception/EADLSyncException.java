package io.github.adr.eadlsync.exception;

/**
 *
 */
public class EADLSyncException extends Exception {

    private EADLSyncOperationState state = EADLSyncOperationState.NONE;

    public EADLSyncException(EADLSyncOperationState state) {
        this.state = state;
    }

    public enum EADLSyncOperationState {
        PULL_FIRST,
        COMMIT,
        CONFLICT,
        UP_TO_DATE,
        SYNCED,
        NONE
    }

    public static EADLSyncException ofState(EADLSyncOperationState state) {
        return new EADLSyncException(state);
    }

    public EADLSyncOperationState getState() {
        return this.state;
    }

}
