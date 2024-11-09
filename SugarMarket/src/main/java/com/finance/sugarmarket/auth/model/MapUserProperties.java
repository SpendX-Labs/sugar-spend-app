package com.finance.sugarmarket.auth.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class MapUserProperties implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapUserPropertiesId")
	private Long id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userPropertiesId")
	private UserProperties userProperties;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private MFUser user;
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProperties getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(UserProperties userProperties) {
		this.userProperties = userProperties;
	}

	public MFUser getUser() {
		return user;
	}

	public void setUser(MFUser user) {
		this.user = user;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
