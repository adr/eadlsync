package com.eadlsync.serepo.data.restinterface.repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eadlsync.serepo.data.atom.annotations.AtomEntry;
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
public class Repository {

	@AtomId
	private URI id;

	@AtomTitle
	private String name;

	@AtomUpdated
	private Date updated;

	private User lastUpdateUser;

	@AtomLink
	private List<Link> links = new ArrayList<>();

	@AtomPerson
	private List<User> authors = new ArrayList<>();

	@AtomEntry.AtomContent
	@AtomEntry.AtomContent.Text
	@AtomEntry.AtomContent.MediaType("text/plain")
	private String description;

	public Repository() {
	}

	public URI getId() {
		return id;
	}

	public void setId(URI id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public User getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(User lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<User> getAuthors() {
		return authors;
	}

	public void setAuthors(List<User> authors) {
		this.authors = authors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
