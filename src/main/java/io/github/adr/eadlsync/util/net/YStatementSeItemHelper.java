package io.github.adr.eadlsync.util.net;

import ch.hsr.isf.serepo.data.restinterface.commit.CommitMode;
import ch.hsr.isf.serepo.data.restinterface.common.Link;
import ch.hsr.isf.serepo.data.restinterface.common.User;
import ch.hsr.isf.serepo.data.restinterface.metadata.MetadataEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.RelationEntry;
import ch.hsr.isf.serepo.data.restinterface.seitem.SeItem;
import io.github.adr.eadlsync.model.decision.DecisionSourceMapping;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapper;
import io.github.adr.eadlsync.model.decision.YStatementJustificationWrapperBuilder;
import io.github.adr.eadlsync.model.serepo.data.SeItemWithContent;
import io.github.adr.eadlsync.util.ystatement.YStatementConstants;
import com.google.common.net.UrlEscapers;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.adr.eadlsync.util.ystatement.SeItemContentParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A helper class that converts se-items to ystatements and the other way round
 */
public class YStatementSeItemHelper {

    private static final Logger LOG = LoggerFactory.getLogger(YStatementSeItemHelper.class);

    public static List<YStatementJustificationWrapper> getYStatementJustifications(String url, String
            project, String commit) throws UnirestException {
        List<SeItem> seItems = SeRepoConector.getSeItemsByUrl(SeRepoLinkFactory.seItemsUrl(url, project, commit));

        // find all se-items that are problem occurrences
        List<SeItem> problemItems = new ArrayList<>();
        for (SeItem item : seItems) {
            MetadataEntry metadata = SeRepoConector.getMetadataEntryForUrl(SeRepoLinkFactory.metadataUrl(item.getId().toString()));
            Object state = metadata.getMap().get(MetadataFactory.GeneralMetadata.STEREOTYPE.getName());
            if (MetadataFactory.Stereotype.PROBLEM_OCCURRENCE.getName().equals(state)) {
                state = metadata.getMap().get(MetadataFactory.GeneralMetadata.TAGGED_VALUES.getName());
                boolean solved = MetadataFactory.ProblemState.SOLVED.getName().equals(((Map<String, String>) state).get(MetadataFactory.TaggedValues.PROBLEM_STATE.getName()));
                if (solved) {
                    // only add problems which are solved, this means if a neglected option raises
                    // another problem we do not want that problem to appear in our decisions
                    problemItems.add(item);
                }
            }
        }

        List<YStatementJustificationWrapper> yStatementJustifications = new ArrayList<>();

        // iterate over the problem occurrences
        for (SeItem problemItem : problemItems) {
            RelationEntry relation = SeRepoConector.getRelationEntryForUrl(SeRepoLinkFactory.relationsUrl(problemItem.getId().toString()));
            List<Link> relationLinks = relation.getLinks().stream().filter(link -> RelationFactory
                    .ADMentorRelationType.ADDRESSED_BY.getName()
                    .equals(link.getTitle())).collect(Collectors.toList());

            // find the chosen option item for the current problem occurrence
            SeItem chosenOptionItem = null;
            List<String> neglectedIds = new ArrayList<>();
            for (Link link : relationLinks) {
                SeItem relationSeItem = seItems.stream().filter(seItem -> seItem.getId().
                        toString().equals(link.getHref())).collect(Collectors.toList()).get(0);

                MetadataEntry entry = SeRepoConector.getMetadataEntryForUrl(SeRepoLinkFactory.metadataUrl(relationSeItem.getId().toString()));
                Map<String, String> taggedValues = (Map<String, String>) entry.getMap().get
                        (MetadataFactory.GeneralMetadata.TAGGED_VALUES.getName());
                Object state = taggedValues.get(MetadataFactory.TaggedValues.OPTION_STATE.getName());
                if (MetadataFactory.OptionState.CHOSEN.getName().equals(state)) {
                    chosenOptionItem = seItems.stream().filter(seItem -> seItem
                            .getId().toString().equals(link.getHref())).collect(Collectors.toList()).get(0);
                } else {
                    neglectedIds.add(getIdFromFolderAndName(relationSeItem.getFolder(), relationSeItem.getName()));
                }

            }

            // create a new YStatementJustification object
            YStatementJustificationWrapper yStatementJustification = createYStatementJustification
                    (problemItem, chosenOptionItem, neglectedIds);
            yStatementJustifications.add(yStatementJustification);

        }

        LOG.debug("Fetched YStatements from {}", commit);
        yStatementJustifications.stream().map(YStatementJustificationWrapper::toString).forEach(LOG::debug);
        return yStatementJustifications;
    }

    private static YStatementJustificationWrapper createYStatementJustification(SeItem problemItem,
                                                                                SeItem chosenOptionItem,
                                                                                List<String> neglected) {
        Element problemBody = getSeItemContentBody(problemItem);
        String id = getIdFromFolderAndName(problemItem.getFolder(), problemItem.getName());
        String context = SeItemContentParser.parseForContent(YStatementConstants.SEITEM_CONTEXT, problemBody);
        String facing = SeItemContentParser.parseForContent(YStatementConstants.SEITEM_FACING, problemBody);
        String neglectedIds = neglected.stream().collect(Collectors.joining(YStatementConstants.DELIMITER));
        String source = problemItem.getId().toString();

        if (chosenOptionItem != null) {
            Element optionBody = getSeItemContentBody(chosenOptionItem);
            String chosen = getIdFromFolderAndName(chosenOptionItem.getFolder(), chosenOptionItem.getName());
            String achieving = SeItemContentParser.parseForContent(YStatementConstants.SEITEM_ACHIEVING, optionBody);
            String accepting = SeItemContentParser.parseForContent(YStatementConstants.SEITEM_ACCEPTING, optionBody);
            DecisionSourceMapping.putRemoteSource(id, source);
            return new YStatementJustificationWrapperBuilder(id).context(context).facing(facing)
                    .chosen(chosen).neglected(neglectedIds).achieving(achieving).accepting(accepting)
                    .build();
        } else {
            DecisionSourceMapping.putRemoteSource(id, source);
            return new YStatementJustificationWrapperBuilder(id).context(context).facing(facing).
                    neglected(neglectedIds).build();
        }
    }

    private static Element getSeItemContentBody(SeItem item) {
        Document doc;
        Element body = null;
        try {
            doc = Jsoup.connect(item.getId().toString()).get();
            body = doc.body();
        } catch (IOException e) {
            LOG.debug("Failed to parse se-item '{}'", item.getName());
        }
        return body;
    }

    public static String commitYStatement(User user, String message,
                                          List<YStatementJustificationWrapper> yStatementJustificationWrappers,
                                          String url, String project)
            throws UnsupportedEncodingException {
        LOG.debug("Prepare YStatements for commit");
        yStatementJustificationWrappers.stream().map(YStatementJustificationWrapper::toString).forEach(LOG::debug);


        Set<SeItemWithContent> allSeItems = new HashSet<>();

        for (YStatementJustificationWrapper wrapper : yStatementJustificationWrappers) {
            List<SeItemWithContent> addressedByItems = new ArrayList<>();

            // add the chosen option to the set
            SeItemWithContent chosenItem = SeRepoConector.createSeOptionItem(wrapper.getChosen(), wrapper.getAchieving(), wrapper.getAccepting(),
                    MetadataFactory.OptionState.CHOSEN);
            addressedByItems.add(chosenItem);
            allSeItems.add(chosenItem);

            // add neglected options to the set
            String[] neglectedOptions = wrapper.getNeglected().split(YStatementConstants.DELIMITER);
            for (String id : neglectedOptions) {
                SeItemWithContent neglectedItem = SeRepoConector.createSeOptionItem(id, "", "", MetadataFactory.OptionState.NEGLECTED);
                allSeItems.add(neglectedItem);
                addressedByItems.add(neglectedItem);
            }

            // create problem item
            SeItemWithContent problem = SeRepoConector.createSeProblemItem(wrapper.getId(), wrapper
                    .getContext(), wrapper.getFacing(), MetadataFactory.ProblemState.SOLVED);

            // set the relations for the problem item
            for (SeItemWithContent seItem : addressedByItems) {
                String encodedName = UrlEscapers.urlFragmentEscaper().escape(seItem.getName());
                problem.getRelations().add(RelationFactory.addressedBy(encodedName));
            }

            // add the problem to the set
            allSeItems.add(problem);
        }

        return SeRepoConector.commit(message, new ArrayList<>(allSeItems), user, CommitMode.ADD_UPDATE_DELETE, url, project);
    }

    private static String getIdFromFolderAndName(String folder, String name) {
        return folder.trim() + UrlEscapers.urlFragmentEscaper().escape(name.trim());
    }


}
