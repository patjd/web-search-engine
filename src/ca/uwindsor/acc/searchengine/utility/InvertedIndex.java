package ca.uwindsor.acc.searchengine.utility;

import java.io.Serializable;
import java.util.*;

/**
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) This class contains
 *         logic for inverted index table.
 */
public class InvertedIndex implements Serializable {

	// Hashmap to store the word and nested hashmap to store the document name and
	// the word count
	HashMap<Integer, HashMap<String, Integer>> wordToDocument;

	InvertedIndex() {
		wordToDocument = new HashMap<Integer, HashMap<String, Integer>>();
	}

	/**
	 * This method add the word and the document list and the word count for each
	 * document.
	 * 
	 * @param index     index given by trie (for each word we have one index)
	 * @param frequency Frequency of the word.
	 * @param filename  name of the file in which this word appeared
	 */
	public void add(Integer index, Integer frequency, String filename) {

		HashMap<String, Integer> documentToCountMap = wordToDocument.get(index);

		if (documentToCountMap == null) {
			// This word has not been found anywhere before in this document,
			// so create a Map to hold document-map counts.

			documentToCountMap = new HashMap<String, Integer>();
			wordToDocument.put(index, documentToCountMap);
		}

		/*
		 * Integer currentCount = documentToCountMap.get(filename); if(currentCount ==
		 * null) { // This word has not been found in this document before, so // set
		 * the initial count to zero. currentCount = 0; }
		 */
		documentToCountMap.put(filename, frequency);
	}

	/**
	 * This method returns the hashmap containing the list of document containig the
	 * given word
	 * 
	 * @param index Index of the word
	 * @return Hashmap of the documents name and word count in that document
	 */
	public HashMap<String, Integer> get(Integer index) {
		return wordToDocument.get(index);
	}

}
