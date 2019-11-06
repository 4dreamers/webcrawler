package com.scalable.webcrawler;

public class Connection {
	
	private String googleApiKey;
	private String customSearchEngineCx;
	private long resultsPageNumber;
	
	protected Connection(String googleApiKey,
					   String customSearchEngineCx,
					   long resultsPageNumber) {
		
		this.googleApiKey = googleApiKey;
		this.customSearchEngineCx = customSearchEngineCx;
		this.resultsPageNumber = resultsPageNumber;
	}
	
	public static Connection buildConnection(String googleApiKey, String customSearchEngineCx, long resultsPageNumber ) {
		return new Connection(googleApiKey, customSearchEngineCx, resultsPageNumber);
	}

	public String getGoogleApiKey() {
		return googleApiKey;
	}

	public String getCustomSearchEngineCx() {
		return customSearchEngineCx;
	}

	public long getResultsPageNumber() {
		return resultsPageNumber;
	}

}
