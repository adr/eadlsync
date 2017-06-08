package com.eadlsync.model.repo;

import com.eadlsync.model.decision.YStatementJustificationWrapper;

/**
 * Created by tobias on 07/03/2017.
 * <p>
 * This interface specifies the methods a repository with architectural decisions has to implement to
 * synchronize and update its embedded architectural decisions.
 */
public interface IRepo {

    /**
     * Sets the context YStatementField for the decision with the given id to the given String.
     *
     * @param context as String
     * @param id
     */
    void updateContext(String context, String id);

    /**
     * Sets the facing YStatementField for the decision with the given id to the given String.
     *
     * @param facing as String
     * @param id
     */
    void updateFacing(String facing, String id);

    /**
     * Sets the chosen YStatementField for the decision with the given id to the given String.
     *
     * @param chosen as String
     * @param id
     */
    void updateChosen(String chosen, String id);

    /**
     * Sets the neglected YStatementField for the decision with the given id to the given String.
     *
     * @param neglected as String
     * @param id
     */
    void updateNeglected(String neglected, String id);

    /**
     * Sets the achieving YStatementField for the decision with the given id to the given String.
     *
     * @param achieving as String
     * @param id
     */
    void updateAchieving(String achieving, String id);

    /**
     * Sets the accepting YStatementField for the decision with the given id to the given String.
     *
     * @param accepting as String
     * @param id
     */
    void updateAccepting(String accepting, String id);

    /**
     * Sets the moreInformation YStatementField for the decision with the given id to the given String.
     *
     * @param moreInformation as String
     * @param id
     */
    void updateMoreInformation(String moreInformation, String id);

    /**
     * Updates the decision with the same id as the given decision.
     *
     * @param decision as YStatementJustificationWrapper
     */
    void updateDecision(YStatementJustificationWrapper decision);

    /**
     * Commits and pushes the local changes to the se-repo
     *
     * @param message for the commitToBaseRepo
     * @return the commit id
     * @throws Exception
     */
    String commit(String message) throws Exception;

    /**
     * Pulls the changes of the latest commit of the se-repo and applies it to the local decisions.
     *
     * @throws Exception
     */
    void pull() throws Exception;

    /**
     * Merge the local decisions with the decisions of the se-repo of the revision with the given commit id.
     *
     * @param commitId
     * @throws Exception
     */
    void merge(String commitId) throws Exception;

    /**
     * Resets the decisions to the decisions of the se-repo with the given commit id.
     *
     * @param commitId
     * @throws Exception
     */
    void reset(String commitId) throws Exception;

    /**
     * Reloads the embedded ads for this repository.
     *
     * @throws Exception
     */
    void reloadEADs() throws Exception;

}
