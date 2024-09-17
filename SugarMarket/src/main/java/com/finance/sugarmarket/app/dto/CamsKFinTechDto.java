package com.finance.sugarmarket.app.dto;

import java.util.List;
import java.util.Map;

public class CamsKFinTechDto {
	private String fullName;
	private String emailId;
	private String phoneNumber;
	private List<String> summary;
	private Map<String, List<String>> details;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<String> getSummary() {
		return summary;
	}

	public void setSummary(List<String> summary) {
		this.summary = summary;
	}

	public Map<String, List<String>> getDetails() {
		return details;
	}

	public void setDetails(Map<String, List<String>> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				"CamsKFinTechDto [fullName=" + fullName + ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + "]")
				.append("\n");

		builder.append("Summary: ").append("\n");
		for (String s : summary) {
			builder.append(s).append("\n");
		}

		if (details != null) {
			for (String s : details.keySet()) {
				builder.append(s).append("\n");
				for (String i : details.get(s)) {
					builder.append(i).append("\n");
				}
			}
		}

		return builder.toString();

	}

}
