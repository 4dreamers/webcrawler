package com.scalable.webcrawler;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class SearchImpl {

	private static Logger logger = Logger.getLogger(SearchImpl.class);
	
	public static void runSearch(String searchQuery) {
		// TODO Auto-generated method stub
		String methodLogName = "[runSearch]";
		Search searchResultList = new Search();
		SearchImpl searchCustom = new SearchImpl();
		Connection newConnection = Connection.buildConnection(
				Constants.GOOGLE_API_KEY,
				Constants.GOOGLE_CX,
				Constants.SEARCH_RESULT_NUMBER);
		logger.info(methodLogName + "API_KEY:" + Constants.GOOGLE_API_KEY + " CX: "
				+ Constants.GOOGLE_CX + " resultNumber:" + Constants.SEARCH_RESULT_NUMBER );
		Customsearch cs = null;
		try {
			cs = searchCustom.createCustomSearch(newConnection);
			searchResultList = searchCustom.executeGoogleSearchQuery(cs, newConnection, searchQuery);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Result> searchResultsLinks = searchResultList.getItems();
		
		//just in case that you need to do something with the java script libraries object
		Map<String, HashSet<String>> jsParsedPagesList = searchCustom.parseWebSiteForJsLib(searchResultsLinks);
		
	}

	public  Search executeGoogleSearchQuery(Customsearch cs, Connection newConnection, String searchQuery) throws IOException {
		Customsearch.Cse.List searchResultQuery = null;
		Search searchResultList = new Search();

		searchResultQuery = cs.cse().list(searchQuery).setCx(newConnection.getCustomSearchEngineCx());
		searchResultQuery.setNum(newConnection.getResultsPageNumber());
		searchResultList = searchResultQuery.execute();

		return searchResultList;
	}

	public Customsearch createCustomSearch(Connection newConnection)
			throws GeneralSecurityException, IOException {

		return new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), null).setApplicationName("ScalableWebCrawler")
						.setGoogleClientRequestInitializer(
								new CustomsearchRequestInitializer(newConnection.getGoogleApiKey()))
						.build();

	}
	
	public  Map<String, HashSet<String>> parseWebSiteForJsLib(List<Result> items) {
		
		Map<String, HashSet<String>> jsScriptList = new ConcurrentHashMap<String, HashSet<String>>();
		items.parallelStream().forEach(url -> {
			
			Document htmlDom  = parseSiteJSoup(url.getLink());
			
			final String webPageTtitle = htmlDom.select("title").text();
			Elements jsScriptTags = htmlDom.getElementsByTag("script");
			//System.out.println(Thread.currentThread().getName() + ": " + title);
			HashSet<String> jsScriptsSources = new HashSet<String>();
			jsScriptTags.forEach(htmlTag -> {
				if(htmlTag.hasAttr("src")) {			
					System.out.println(url.getLink() + " , " + htmlTag.attr("src"));
					jsScriptsSources.add(htmlTag.attr("src"));
				}
			});
			
			jsScriptList.put(webPageTtitle, jsScriptsSources);
		});
		return jsScriptList;
	}

	public  Document parseSiteJSoup(String url) {
		
		Document htmlDom = new Document(url);
		try {
			htmlDom = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return htmlDom;
		
	}
	
}
