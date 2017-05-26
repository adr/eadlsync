package com.eadlsync.util.net;

/**
 * Created by Tobias on 26.05.2017.
 */
public enum SeRepoRelationType {

    SEREPO_SELF("self"),
    SEREPO_CONTENT("https://www.ifs.hsr.ch/serepo/api/rels/serepo_content"),
    SEREPO_RELATIONS("https://www.ifs.hsr.ch/serepo/api/rels/serepo_relations"),
    SEREPO_METADATA("https://www.ifs.hsr.ch/serepo/api/rels/serepo_metadata");

    private final String rel;

    SeRepoRelationType(String rel) {
        this.rel = rel;
    }

    public String getRelation() {
        return this.rel;
    }

}
