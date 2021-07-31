package ca.uwindsor.acc.searchengine.utility;

/**
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth)
 * Node Data Structure for Trie
 */
public class TrieNode {

	TrieNode[] nodes = new TrieNode[26];
	int count;
	boolean isEnd;

	public int getValue() {
		return count;
	}

	public void incrementValue() {
		count++;
	}
}