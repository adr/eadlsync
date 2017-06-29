package com.eadlsync.model.config;

import ch.hsr.isf.serepo.data.restinterface.common.User;

/**
 * Config model
 */
public class Config {

    private ConfigUser user;

    private ConfigCore core;

    public ConfigCore getCore() {
        return core;
    }

    public void setCore(ConfigCore core) {
        this.core = core;
    }

    public ConfigUser getUserConfig() {
        return user;
    }

    public User getUser() {
        return new User(user.getName(), user.getEmail());
    }

    public void setUser(ConfigUser user) {
        this.user = user;
    }
}
