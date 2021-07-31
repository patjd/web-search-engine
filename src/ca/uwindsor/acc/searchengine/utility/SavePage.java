package ca.uwindsor.acc.searchengine.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ca.uwindsor.acc.searchengine.constants.Constants;

/**
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) 
 * This class contains html to text code and save html to text files
 */
public class SavePage implements Constants {

	/**
	 * 
	 * @param path path to store the text files
	 * @param urls hashset of fetched urls using crawler
	 * @throws IOException
	 * @throws SQLException
	 * 
	 */
	public static void create_text_files(String path, HashSet<String> urls) throws IOException, SQLException {

		int j = 1;
		PrintWriter writer1 = null;
		writer1 = new PrintWriter(new File(path + "names" + ".txt"));
		for (String url : urls) {
			PrintWriter wr = new PrintWriter(new FileWriter(path + j + ".txt"));
			String data = html_to_text(url);
			System.out.println(data);
			wr.println(data);// print in file
			wr.close();
			writer1.write(j + "," + url + "\n");
			j++;
		}
		writer1.close();
	}

	/**
	 * connects to url, fetches html and converts html to text
	 * 
	 * @param takes URL of input page
	 * @return text type of string
	 */
	public static String html_to_text(String Url) {
		String text = null;
		try {
			Document document = Jsoup.connect(Url).get();
			text = document.body().text();
		} catch (Exception e) {
			System.out.println("Exception" + e.getStackTrace());
		}
		return text;
	}

}
