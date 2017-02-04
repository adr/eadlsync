package com.eadlsync.serepo.data.restinterface.commit;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eadlsync.serepo.data.atom.annotations.AtomEntry;
import com.eadlsync.serepo.data.atom.annotations.AtomEntry.AtomContent;
import com.eadlsync.serepo.data.atom.annotations.AtomFeed;
import com.eadlsync.serepo.data.atom.annotations.AtomId;
import com.eadlsync.serepo.data.atom.annotations.AtomLink;
import com.eadlsync.serepo.data.atom.annotations.AtomPerson;
import com.eadlsync.serepo.data.atom.annotations.AtomTitle;
import com.eadlsync.serepo.data.atom.annotations.AtomUpdated;
import com.eadlsync.serepo.data.restinterface.common.Link;
import com.eadlsync.serepo.data.restinterface.common.User;

@AtomFeed
@AtomEntry
public class Commit {

  @AtomId
  private URI id;

  @AtomUpdated
  private Date when;

  @AtomTitle
  private String shortMessage;

  @AtomEntry.AtomContent
  @AtomContent.Text
  @AtomContent.MediaType(javax.ws.rs.core.MediaType.TEXT_PLAIN)
  private String fullMessage;

  @AtomPerson
  private User author;

  @AtomLink
  private List<Link> links = new ArrayList<>();

  public Commit() {}

  public URI getId() {
    return id;
  }

  public void setId(URI id) {
    this.id = id;
  }

  public Date getWhen() {
    return when;
  }

  public void setWhen(Date when) {
    this.when = when;
  }

  public String getShortMessage() {
    return shortMessage;
  }

  public void setShortMessage(String shortMessage) {
    this.shortMessage = shortMessage;
  }

  public String getFullMessage() {
    return fullMessage;
  }

  public void setFullMessage(String fullMessage) {
    this.fullMessage = fullMessage;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public List<Link> getLinks() {
    return links;
  }


  public void setLinks(List<Link> links) {
    this.links = links;
  }

}
