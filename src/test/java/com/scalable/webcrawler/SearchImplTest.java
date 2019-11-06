package com.scalable.webcrawler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Search;

import junit.framework.Assert;

public class SearchImplTest {
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	SearchImpl searchTest = null;
	Connection newConnection;
	private static final String RESOURCE_URL = "https://google.com";
	private static final String RESOURCE_URL_ANY = "https://de.scalable.capital/";
	public final static String GOOGLE_CX = "001936359397208676040:v1u44zeqomu"; // My custom search engine
	public final static String GOOGLE_API_KEY = "AIzaSyAP6yNYlaeLtw7VBOgV8lGW0xmN359JeW8"; // Search engine api_key
	public final static String WRONG_GOOGLE_API_KEY = "AI67SyAP6yNYlaeLtw7VBOgV8lGW0xmN359JeW8"; // Search engine api_key
		
	@Before
	public void setup() {
		searchTest = new SearchImpl();
		newConnection = new Connection(GOOGLE_API_KEY, GOOGLE_CX, 5);
	}

	@Test
	public void testExecuteGoogleSearchQuery() {
		
		Search searchQuery = null;
		try {
			searchQuery = searchTest.executeGoogleSearchQuery(null, null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Test
	public void testCreateCustomSearch() {
		
		Customsearch searchConect = null;
		try {
			searchConect = searchTest.createCustomSearch(newConnection);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String app = searchConect.getServicePath();
		Assert.assertEquals(app, "customsearch/" );
	}

	@Test
	public void testParseWebSiteForJsLib() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseSiteJSoup() {
		
		Document doc = searchTest.parseSiteJSoup(RESOURCE_URL);
		Assert.assertEquals("Google" , doc.select("title").text());
	}
	
	@Test
	public void testParseSiteJSoupAnyWebsite() {
		
		Document doc = searchTest.parseSiteJSoup(RESOURCE_URL_ANY);
		Assert.assertFalse(doc.select("title").text().isEmpty());
	}
	
	

}
