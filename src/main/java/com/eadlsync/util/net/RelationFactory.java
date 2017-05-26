package com.eadlsync.util.net;

import com.eadlsync.serepo.data.restinterface.seitem.Relation;

/**
 * Created by Tobias on 26.05.2017.
 */
public class RelationFactory {

    public enum ADMentorRelationType {
        ADDRESSED_BY("Addressed By", "http://admentor.com/relations/addressed_by"),
        RAISES("Raises", "http://admentor.com/relations/raises"),
        SUGGESTS("Suggests", "http://admentor.com/relations/suggests"),
        CONFLICTS_WITH("Conflicts With", "http://admentor.com/relations/conflicts_with");

        private final String name;
        private final String url;

        ADMentorRelationType(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return this.name;
        }

        public String getUrl() {
            return this.url;
        }
    }


    public static Relation addressedBy(String target) {
        return ofTypeAndTarget(ADMentorRelationType.ADDRESSED_BY, target);
    }

    public static Relation raises(String target) {
        return ofTypeAndTarget(ADMentorRelationType.RAISES, target);
    }

    public static Relation suggests(String target) {
        return ofTypeAndTarget(ADMentorRelationType.SUGGESTS, target);
    }

    public static Relation conflictsWith(String target) {
        return ofTypeAndTarget(ADMentorRelationType.CONFLICTS_WITH, target);
    }

    private static Relation ofTypeAndTarget(ADMentorRelationType type, String target) {
        Relation relation = new Relation();
        relation.setType(type.getName());
        relation.setTarget(target);
        return relation;
    }

}
