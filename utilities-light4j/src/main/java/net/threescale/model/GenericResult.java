package net.threescale.model;

public class GenericResult {
	private String summary, details;

	public GenericResult() {
		super();
	}

	public GenericResult(String summary) {
		super();
		this.summary = summary;
	}

	public GenericResult(String summary, String details) {
		super();
		this.summary = summary;
		this.details = details;
	}

	public String getSummary() {
		return summary;
	}
	
	public String getDetails() {
		return details;
	}

}
