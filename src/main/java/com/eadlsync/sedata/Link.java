package com.eadlsync.sedata;

public class Link {

	private String title;
	private String rel;
	private String href;

	public Link() {
	}

	public Link(String rel, String href) {
		this.rel = rel;
		this.href = href;
	}

	public Link(String title, String rel, String href) {
		this.title = title;
		this.rel = rel;
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
