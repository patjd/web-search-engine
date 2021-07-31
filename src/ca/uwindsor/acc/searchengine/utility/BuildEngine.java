
package ca.uwindsor.acc.searchengine.utility;

import java.io.File;
import java.util.*;

import ca.uwindsor.acc.searchengine.constants.Constants;

/**
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) This class makes the
 *         TST and inverted index table from the text files.
 */
public class BuildEngine implements Constants {

	InvertedIndex invertedIndex;
	TST<Integer> tst;

	public BuildEngine() {
		invertedIndex = new InvertedIndex();
		tst = new TST<Integer>();
	}

	public BuildEngine(InvertedIndex invertedIndex, TST<Integer> tst) {
		this.invertedIndex = invertedIndex;
		this.tst = tst;
	}

	/**
	 * This is the flow chart for our build Engine. Here we are making our ternary
	 * search tree and inverted index table. ​ For each file downloaded using
	 * crawler, ​ firstly we have read the file content and then the content is
	 * tokenized. ​ While reading the tokens generated we got the word count and the
	 * word set,​ From the word set all the stop words are removed. Then each word
	 * is inserted in the Ternary search tree, and for each word Inverted index is
	 * updated. Before inserting in the ternary search tree, we have checked, if the
	 * word is already present in the tree, then we got its index form the ternary
	 * search tree and only inverted index is updated. All the above steps are
	 * repeated for each file.​ At last we have stored our TST and index table in
	 * the files. so that we need not to make index table and TST each time we run
	 * search query. ​
	 */
	public void MakeIndexAndTrie() {

		String fileContent = null;
		String word = null;
		String filename = null;
		int trieIndex = 0;
		int tableIndex = 0;
		int frequency = 0;
		HashSet<String> wordSet = new HashSet<String>();
		HashMap<String, Integer> wordcount = new HashMap<String, Integer>();
		StopWords stopwords = new StopWords();

		File files = new File(textFileSourcePath);
		File[] fileList = files.listFiles();
		// for each file call the converter function
		for (File f : fileList) {
			if (f.isFile()) {
				filename = f.getName();
				wordSet.clear();
				wordcount.clear();

				// get file content
				fileContent = FileOperations.readTextFile(f.getPath());

				// tokenizing the string
				StringTokenizer stoken = new StringTokenizer(fileContent, " -=,.#%&*<>\"\t\n\r\f,.:;?![](){}'\\/+-_?");

				// for each token add it to hashset
				while (stoken.hasMoreTokens()) {
					word = stoken.nextToken().trim().toLowerCase();
					wordSet.add(word);

					if (wordcount.get(word) == null) {
						wordcount.put(word, 1);
					} else {
						int count = wordcount.get(word);
						wordcount.put(word, count + 1);
					}

				}

				Iterator<String> i = wordSet.iterator();
				while (i.hasNext()) {

					word = i.next();

					// remove the stop words
					if (!stopwords.isStopWord(word)) {
						// System.out.println(word);

						if (!tst.contains(word)) {

							// add the word to trie
							tst.put(word, trieIndex++);
							tableIndex = tst.get(word);

							// call boyer moore to get frequency
							// frequency =getFrequency(fileContent, word);
							frequency = wordcount.get(word);
							invertedIndex.add(tableIndex, frequency, filename);

						} else {
							tableIndex = tst.get(word);

							// call boyer moore
							// frequency =getFrequency(fileContent, word);
							frequency = wordcount.get(word);
							invertedIndex.add(tableIndex, frequency, filename);
						}
					}
				}
			}
		}

	}

	public void storeToFile(String triePath, String indexTablePath) {

		FileOperations.WriteObjectToFile(triePath, tst);
		FileOperations.WriteObjectToFile(indexTablePath, invertedIndex);
	}

	public static int getFrequency(String text, String word) {
		int count = 0;
		int offset = 0;
		do {
			/*
			 * BoyerMoore boyermoore = new BoyerMoore(word); offset =
			 * boyermoore.search(text.substring(offset)); count++;
			 */
		} while (offset < text.length());
		return count - 1;
	}
}
