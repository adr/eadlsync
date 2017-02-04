package com.eadlsync.serepo.data.restinterface.repository;


import com.eadlsync.serepo.data.restinterface.common.User;

public class CreateRepository {

	private String name;
	
	private String description;
	
	private User user;
	
	public CreateRepository() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
