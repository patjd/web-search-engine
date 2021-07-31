package ca.uwindsor.acc.searchengine.utility;

/**
 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) The class
 *         EditDistance is used to find the edit distance between the words
 */
public class EditDistance {

	/*
	 * This method calculates edit distance between given two words using Dynamic
	 * Programming word1 -> input word from the query word2 -> word from dictionary
	 */
	public int editDistance(String word1, String word2) {
		int w1 = word1.length();
		int w2 = word2.length();
		int editDistance[][] = new int[w1 + 1][w2 + 1];
		for (int i = 0; i <= w1; i++) {
			for (int j = 0; j <= w2; j++) {
				if (i == 0)
					editDistance[i][j] = j;
				else if (j == 0)
					editDistance[i][j] = i;
				else if (word1.charAt(i - 1) == word2.charAt(j - 1))
					editDistance[i][j] = editDistance[i - 1][j - 1];
				else if (i > 1 && j > 1 && word1.charAt(i - 1) == word2.charAt(j - 2)
						&& word1.charAt(i - 2) == word2.charAt(j - 1))
					editDistance[i][j] = 1 + Math.min(Math.min(editDistance[i - 2][j - 2], editDistance[i - 1][j]),
							Math.min(editDistance[i][j - 1], editDistance[i - 1][j - 1]));
				else
					editDistance[i][j] = 1 + Math.min(editDistance[i][j - 1],
							Math.min(editDistance[i - 1][j], editDistance[i - 1][j - 1]));
			}
		}
		return editDistance[w1][w2];
	}

}
