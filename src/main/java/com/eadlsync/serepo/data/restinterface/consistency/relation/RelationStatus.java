package com.eadlsync.serepo.data.restinterface.consistency.relation;

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
public class RelationStatus {

	@AtomId
	private URI id;
	
	@AtomTitle(prefix = "Number of inconsistencies: ")
	private int numberOfInconsistencies;
	
	@AtomUpdated
	@JsonIgnore
	private Date updated;

	@AtomLink
	private List<Link> links = new ArrayList<>();
	
	@AtomEntry
	private List<RelationInconsistency> inconsistencies = new ArrayList<>();
	
	public RelationStatus() {
	}

	public URI getId() {
		return id;
	}

	public void setId(URI id) {
		this.id = id;
	}

	public int getNumberOfInconsistencies() {
		return numberOfInconsistencies;
	}

	public void setNumberOfInconsistencies(int numberOfInconsistencies) {
		this.numberOfInconsistencies = numberOfInconsistencies;
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

	public List<RelationInconsistency> getInconsistencies() {
		return inconsistencies;
	}

	public void setInconsistencies(List<RelationInconsistency> inconsistencies) {
		this.inconsistencies = inconsistencies;
	}

}
