package com.eadlsync.model.config;

/**
 * Created by tobias on 02/06/2017.
 */
public class Config {

    private ConfigUser user;

    private ConfigCore core;

    private ConfigSync sync;

    public ConfigCore getCore() {
        return core;
    }

    public void setCore(ConfigCore core) {
        this.core = core;
    }

    public ConfigUser getUser() {
        return user;
    }

    public void setUser(ConfigUser user) {
        this.user = user;
    }

    public ConfigSync getSync() {
        return sync;
    }

    public void setSync(ConfigSync sync) {
        this.sync = sync;
    }
}
