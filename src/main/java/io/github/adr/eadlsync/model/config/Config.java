package io.github.adr.eadlsync.model.config;

/**
 * Config model
 */
public class Config {

    private ConfigUser user;

    private ConfigCore core;

    public ConfigCore getConfigCore() {
        return core;
    }

    public void setConfigCore(ConfigCore core) {
        this.core = core;
    }

    public ConfigUser getConfigUser() {
        return user;
    }

    public void setConfigUser(ConfigUser user) {
        this.user = user;
    }
}
