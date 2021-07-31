package ca.uwindsor.acc.searchengine.utility;

import java.util.*;

import ca.uwindsor.acc.searchengine.constants.Constants;

/**
 * 
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth)
 * This class First it will take search query from user and it can be a sentence or word.​
 * After that sentence will be corrected if it have any misspelled words. And, common words will be removed using stop words algorithm.​
 * Next comes the searching part, here for loop will run for each word. Within this loop we have also included the logic for finding longest prefix if string doesn’t find match in TST.​
 * In this hash map will be returned which will include document name and count  and it is mapped to the words.​Then , page ranking and sorting will be performed on the hash map.​FInally web pages will be displayed.  
 */
public class SearchEngine implements Constants {
	public static String output_corrected = "";
	private static TST<Integer> tst;
	private static InvertedIndex invertedIndex;
	public static void webCrawler(String domain) throws Exception {

		// crawler to fetch the pages form the internet
		System.out.println("Wait getting the pages from the domain");

		// domain to fetch the url

		WebCrawler.extractWebPages(domain);
		HashSet<String> urls = WebCrawler.urls;

		System.out.println("Saving pages");
		SavePage.create_text_files(textFileSourcePath3, urls);

		// store the fetched pages in the database
		System.out.println("Saving pages");

	}

	public static void buildEngine() {

		// to store inverted index
		InvertedIndex invertedIndex = new InvertedIndex();
		// to store the trie
		TST<Integer> tst = new TST<Integer>();
		// to remove the stop words
		BuildEngine buildEngine = new BuildEngine(invertedIndex, tst);
		// function to make the trie
		buildEngine.MakeIndexAndTrie();
		// save trie and index table to files

		Boolean store = true;
		if (store) {
			System.out.println("Saving the trie and Index table in files");
			buildEngine.storeToFile(triePath, indexTablePath);
		}

	}
	
	public static void initializeTrie() {
		tst = (TST<Integer>) FileOperations.ReadObjectFromFile(triePath);
		invertedIndex = (InvertedIndex) FileOperations.ReadObjectFromFile(indexTablePath);
		System.out.println("Trie initialization successfully!");
	}

	public static HashMap<String, Integer> search(String input1) {
		HashMap<String, Integer> pages = null;

		try {
			// to remove stop words
			StopWords stopwords = new StopWords();
			// read the trie and index table
			
			// Auto Correction
			AutoCorrection ac = new AutoCorrection();
			// Dictionary for auto correction
			ac.dictionaryOfWords(dictpath);

			Scanner sc = new Scanner(System.in);

			String input = input1.toLowerCase();
			String correct[] = input.split(" ");
			// System.out.println("Input: " + input);

			String s = "";
		    output_corrected = "";
			int c = 0;
			// Auto Correction
			for (String w : correct) {
				if (stopwords.isStopWord(w) || tst.contains(w)) {
					s += w + " ";
					output_corrected += w + " ";
				} else {
					String n = ac.suggestWords(w);
					if (!(n.equals(w))) {
						// System.out.println("Suggested word for "+correct[i]+" is "+n);
						s += n + " ";
						output_corrected += w + " [You mean: " + n + "]";
						c = 1;
					}
				}
			}
			
			if (c == 1) // if any word is corrected
				System.out.println("Showing output For:" + output_corrected + "\n");
			// remove stop words for the search query
		     
			String tokInput = stopwords.removeStopWords(s);
			// System.out.println(tokInput);

			// make the array from the input
			String inputArr[] = tokInput.split(" ");

			// list of hashmap for each word in the search query
			List<HashMap<String, Integer>> maplist = new ArrayList<HashMap<String, Integer>>();

			// for each word get the list of documents as a hashmap and store it to list
			String output = "";
			for (String word : inputArr) {

				// System.out.println(word);
				if (word.length() > 1) {
					if (tst.contains(word)) {

						// get the list of documents from the inverted index
						maplist.add(invertedIndex.get(tst.get(word)));
						output += word + "[found] ";
					}

					// for searching the prefix of the keyword
					else {
						String prefix = tst.longestPrefixOf(word);

						// get the list of documents from the inverted index
						// System.out.println("prefix: " + prefix);
						if (prefix != null && prefix.length() > 1) {
							output += word + "[Prefix found: " + prefix + "] ";
							maplist.add(invertedIndex.get(tst.get(prefix)));
						} else {

						}
					}
				}

			}

			// Rank and sort the hashmap
			if (maplist.size() > 0) {
				pages = Sorting.sortByValue(Rank.rankPages(maplist));
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return pages;
	}

	public static void main(String[] args) {

		try {
			System.out.println("------------Welcome to our Search Engine------------");
			System.out.println("Press \n1 To run web crawler\n2 To build search Engine\n3 To run Search query");
			Scanner sc = new Scanner(System.in);
			int input = sc.nextInt();
			sc.nextLine();
			if (input == 1) {

				System.out.println("Enter Web Domain like (http://www.mit.edu): ");

				String domain = sc.nextLine();
				webCrawler(domain);
			}
			if (input == 2) {
				buildEngine();
			}
			if (input == 3) {
				while (true) {
					System.out.println("Enter Search String or enter exit to stop: ");

					String s = sc.nextLine();

					if (s.equalsIgnoreCase("exit")) {

						break;
					}
					System.out.println("Input : " + s);
					HashMap<String, Integer> pages = search(s);
					// System.out.println(pages);

					if (pages != null) {
						Set<String> keys = pages.keySet();
						Iterator<String> setitr = keys.iterator();
						HashMap<String, String> urlmap = FileOperations.readurlmap();
						// System.out.println(urlmap);
						// print the hashmap
						while (setitr.hasNext()) {
							String key = setitr.next();
							System.out.println(
									"Page name :" + key + " : Rank- " + pages.get(key) + " URL: " + urlmap.get(key));
						}
					}

					else {
						System.out.println("No result found");

					}
				}
			} else {
				System.out.println("Enter valid input");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
