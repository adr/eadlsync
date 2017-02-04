package com.eadlsync.serepo.data.restinterface.seitem;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eadlsync.serepo.data.atom.annotations.AtomEntry;
import com.eadlsync.serepo.data.atom.annotations.AtomFeed;
import com.eadlsync.serepo.data.atom.annotations.AtomId;
import com.eadlsync.serepo.data.atom.annotations.AtomLink;
import com.eadlsync.serepo.data.atom.annotations.AtomTitle;
import com.eadlsync.serepo.data.atom.annotations.AtomUpdated;
import com.eadlsync.serepo.data.restinterface.common.Link;
import com.fasterxml.jackson.annotation.JsonIgnore;

@AtomFeed
public class RelationContainer {

  @AtomId
  private URI id;
  
  @AtomTitle
  @JsonIgnore
  private final static String TITLE = "Relations";
  
  @AtomUpdated
  private Date updated;
  
  @AtomLink
  private List<Link> links = new ArrayList<>();

  @AtomEntry
  private RelationEntry entry;

  
  public RelationContainer() {
  }


  public URI getId() {
    return id;
  }


  public void setId(URI id) {
    this.id = id;
  }


  public Date getUpdated() {
    return updated;
  }


  public void setUpdated(Date updated) {
    this.updated = updated;
  }


  public List<Link> getLinks() {
    return links;
  }


  public void setLinks(List<Link> links) {
    this.links = links;
  }


  public RelationEntry getEntry() {
    return entry;
  }


  public void setEntry(RelationEntry entry) {
    this.entry = entry;
  }

}
