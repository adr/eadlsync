package com.eadlsync.sync;

/**
 * Created by tobias on 01/06/2017.
 */
public interface IEADLSync {

    String init(InitOptions options);

    String reset(ResetOptions options);



}
