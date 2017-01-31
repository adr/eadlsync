package com.eadlsync.sedata;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeItem {

	private URI id;

	private String name;

	private String folder;

	private Date updated;

	private User author;

	private List<Link> links = new ArrayList<>();

	private Content content;

	public static class Content {

		private String mimetype;

		private String url;
		
		public Content() {
		}

		public String getMimetype() {
			return mimetype;
		}

		public void setMimetype(String mimetype) {
			this.mimetype = mimetype;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
		
	}
	
	public SeItem() {
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

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}
