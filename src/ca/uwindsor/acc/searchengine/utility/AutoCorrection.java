package ca.uwindsor.acc.searchengine.utility;

import java.io.*;
import java.util.*;

/*
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth)
 * The class AutoCorrection helps in detecting wrong spell words
 * and suggest similar word in dictionary.
 */
public class AutoCorrection {
	Trie trie;
	EditDistance ed;
	Map<String, Integer> dictionary;
	static List<String> list; // to identity single letter word

	AutoCorrection() {
		trie = new Trie();
		ed = new EditDistance();
		dictionary = new HashMap<>(); // Stores dictionary words
		list = Arrays.asList("abcdefghijklmnopqrstuvwxyz");
	}

	/*
	 * This methods reads all the words from the dictionary and adds them in Trie
	 * 
	 * @param dictionaryFileName -> The file path for dictionary file
	 */
	public void dictionaryOfWords(String dictionaryFileName) throws IOException {
		try {
			FileReader fileReader = new FileReader(dictionaryFileName);
			BufferedReader br = new BufferedReader(fileReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				String word = line.toLowerCase();
				if (!line.contains(" ")) {
					dictionary.put(word, dictionary.getOrDefault(word, 0) + 1);
					trie.add(word);
				} else {
					String[] strs = line.split("\\s");
					for (String str : strs) {
						dictionary.put(str, dictionary.getOrDefault(str, 0) + 1);
						trie.add(str);
					}
				}
			}
			fileReader.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/*
	 * This method suggests word for the given input word
	 * 
	 * @param inputWord -> any user input
	 */
	public String suggestWords(String inputWord) {
		TreeMap<Integer, TreeMap<Integer, TreeSet<String>>> map = new TreeMap<>();
		String str = inputWord.toLowerCase();
		String result = null;
		TrieNode node = trie.find(str);

		if (inputWord.length() == 0 || inputWord == null || list.contains(str))
			return result;

		if (node == null) {
			for (String wd : dictionary.keySet()) {
				int dist = ed.editDistance(wd, str);
				TreeMap<Integer, TreeSet<String>> similarWords = map.getOrDefault(dist, new TreeMap<>());
				int freq = dictionary.get(wd);
				TreeSet<String> set = similarWords.getOrDefault(freq, new TreeSet<>());
				set.add(wd);
				similarWords.put(freq, set);
				map.put(dist, similarWords);
			}
			result = map.firstEntry().getValue().lastEntry().getValue().first();
		} else if (node != null) {
			result = str;
		}

		return result;
	}

	public static void main(String[] args) throws IOException {

		AutoCorrection sc = new AutoCorrection();
		EditDistance ed = new EditDistance();
		sc.dictionaryOfWords("./src/realproject/dictionary.txt");

		System.out.println("edit distance:");
		System.out.println("bird -> ird: " + ed.editDistance("bird", "ird"));
		System.out.println("ohuse -> house: " + ed.editDistance("ohuse", "house"));
		System.out.println("zopper -> top: " + ed.editDistance("zopper", "top"));
		System.out.println("ask -> askhim: " + ed.editDistance("ask", "askhim"));

		System.out.println("suggest of aop is " + sc.suggestWords("aop"));
		System.out.println("suggest of bloat is " + sc.suggestWords("bloat"));
		System.out.println("suggest of reah is " + sc.suggestWords("reah"));
	}
}