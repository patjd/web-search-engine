package ca.uwindsor.acc.searchengine.utility;

import java.io.*;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) 
 * Web Crawler extracts the url's from the given domain
 */
public class WebCrawler {

	// arraylist to store the url of the files
	public static HashSet<String> urls = new HashSet<String>();
	static int i = 0;

	/**
	 * This method get all the url from the given url domain
	 * 
	 * @param URL url of the website
	 * @throws IOException
	 */
	public static void extractWebPages(String url) throws IOException {
		// check if the given URL is already in database
		if (!urls.contains(url)) {
			try {
				i++;
				if (i > 1000)
					return;
				// 4. (i) If not add it to the index
				if (urls.add(url)) {
					System.out.println(url);
				}

				// 2. Fetch the HTML code
				Document document = Jsoup.connect(url).get();
				// 3. Parse the HTML to extract links to other URLs
				Elements linksOnPage = document.select("a[href]");

				// 5. For each extracted URL... go back to Step 4.
				for (Element page : linksOnPage) {
					extractWebPages(page.attr("abs:href"));
				}
			} catch (IOException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
			}
		}
		/*
		 * try { Document document = Jsoup.connect(url).get(); Elements links =
		 * document.select("a"); if(urls.isEmpty()) { urls.add(url); }
		 * if(links.isEmpty()) { return; }
		 * 
		 * links.stream().map((link) ->
		 * link.attr("abs:href")).forEachOrdered((currentUrl) -> { boolean addedWebPage
		 * = urls.add(currentUrl); if (addedWebPage &&
		 * currentUrl.contains(urls.stream().findFirst().get())) {
		 * System.out.println(currentUrl); extractWebPages(currentUrl); } });
		 * 
		 * } catch(Exception e) { System.out.println("Failed reading the Url" + url); }
		 */
	}
}
