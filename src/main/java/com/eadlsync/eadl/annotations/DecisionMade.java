package com.eadlsync.eadl.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
// @Target({ElementType.TYPE})
@Inherited
@Documented
public @interface DecisionMade {

    String id() default "AD-nn";

    String solvedProblem() default "[not yet captured]";

    String chosenOption();

    String rationale() default "[not yet justified]";

    String[] relatedDecisions() default {};
}
