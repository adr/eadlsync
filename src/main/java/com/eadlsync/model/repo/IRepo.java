package com.eadlsync.model.repo;

import javafx.beans.property.ListProperty;

import com.eadlsync.model.decision.YStatementJustificationWrapper;

/**
 * Created by tobias on 07/03/2017.
 * <p>
 * This interface specifies the methods a repository with architectural decisions has to implement to
 * synchronize and update its embedded architectural decisions.
 */
public interface IRepo {

    /**
     * Sets the context field for the decision with the given id to the given String.
     *
     * @param context as String
     * @param id
     */
    void updateContext(String context, String id);

    /**
     * Sets the facing field for the decision with the given id to the given String.
     *
     * @param facing as String
     * @param id
     */
    void updateFacing(String facing, String id);

    /**
     * Sets the chosen field for the decision with the given id to the given String.
     *
     * @param chosen as String
     * @param id
     */
    void updateChosen(String chosen, String id);

    /**
     * Sets the neglected field for the decision with the given id to the given String.
     *
     * @param neglected as String
     * @param id
     */
    void updateNeglected(String neglected, String id);

    /**
     * Sets the achieving field for the decision with the given id to the given String.
     *
     * @param achieving as String
     * @param id
     */
    void updateAchieving(String achieving, String id);

    /**
     * Sets the accepting field for the decision with the given id to the given String.
     *
     * @param accepting as String
     * @param id
     */
    void updateAccepting(String accepting, String id);

    /**
     * Sets the moreInformation field for the decision with the given id to the given String.
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
     * Commits the changes of the decisions to the repository with the given commit message.
     *
     * @param message for the commit
     * @throws Exception
     */
    void commit(String message) throws Exception;

    /**
     * Reloads the embedded ads for this repository.
     *
     * @throws Exception
     */
    void reloadEADs() throws Exception;

    /**
     * List property of all the embedded ads of this repository.
     *
     * @return the eads of this repository
     */
    ListProperty<YStatementJustificationWrapper> yStatementJustificationsProperty();

}
