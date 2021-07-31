package ca.uwindsor.acc.searchengine.utility;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

import ca.uwindsor.acc.searchengine.constants.Constants;

/**
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) This class contains
 *         all the file operation like reading, writing object and text files
 */
public class FileOperations implements Constants {
	/**
	 * this method read the given file and return the text
	 * 
	 * @param filename Name of the file to be converted
	 */
	public static String readTextFile(String filePath) {
		// path of the files
		String docText = "";
		try {
			File myfile = new File(filePath);
			// System.out.println(path);
			org.jsoup.nodes.Document doc = Jsoup.parse(myfile, "UTF-8");
			docText = doc.toString();
			return docText;

		} catch (Exception e) {
			System.out.println(e);
		}
		return docText;
	}

	/**
	 * 
	 * @param path path to store the file
	 * @param data data to be stored
	 * @throws IOException
	 */

	public static void createfile(String path, String data) throws IOException {

		PrintWriter wr = new PrintWriter(new FileWriter(path));
		// System.out.println(data);
		wr.println(data);
		wr.close();
	}

	public static HashMap<String, String> readurlmap() throws IOException {
		HashMap<String, String> urlmap = new HashMap<String, String>();
		try {
			FileReader fileReader = new FileReader(textFileSourcePath + "\\names.txt");
			BufferedReader br = new BufferedReader(fileReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				// System.out.println("Line : " + line);
				String[] s = line.split(",");
				urlmap.put(s[0] + ".txt", s[1]);
			}
			fileReader.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		return urlmap;
	}

	/**
	 * This method write the objects to the text file
	 * 
	 * @param filepath path of the file
	 * @param serObj   object to be written
	 */

	public static void WriteObjectToFile(String filepath, Object serObj) {

		try {

			FileOutputStream fileOut = new FileOutputStream(filepath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serObj);
			objectOut.close();
			System.out.println("The Object  was succesfully written to a file");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Read the object for the file
	 * 
	 * @param filepath file path
	 * @return return the object read form the file
	 */
	public static Object ReadObjectFromFile(String filepath) {

		try {

			FileInputStream fileIn = new FileInputStream(filepath);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			Object obj = objectIn.readObject();
			// System.out.println("The Object has been read from the file");
			objectIn.close();
			return obj;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
