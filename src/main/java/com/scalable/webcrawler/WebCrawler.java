package com.scalable.webcrawler;

import java.util.Scanner;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class WebCrawler {

	private static Logger logger = Logger.getLogger(WebCrawler.class);

	public static void main(String[] args) {
		
		BasicConfigurator.configure();

		String methodLogName = "[main method]";
		System.out.print("Enter a search query : ");
		Scanner scanner = new Scanner(System.in);
		String searchQuery = scanner.nextLine();
		scanner.close();
		logger.info(methodLogName + "searchQuery " + searchQuery );
		// Execute search
		SearchImpl.runSearch(searchQuery);
	}
}
