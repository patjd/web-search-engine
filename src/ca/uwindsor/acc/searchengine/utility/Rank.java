package ca.uwindsor.acc.searchengine.utility;

import java.util.*;

public class Rank {

	/**
	 * @author Team ACC (Ankit, Gurjit, Jaydeep, Mehul, Parth) 
	 * This method rank the pages
	 * 
	 * @param maplist list of all the pages
	 * @return Single hashmap by combining all the hashmap in the lists
	 */
	public static HashMap<String, Integer> rankPages(List<HashMap<String, Integer>> maplist) {
		Set<String> commonkeys = hashMapIntersection(maplist);
		// System.out.println("common keys :" +commonkeys);
		HashMap<String, Integer> unionmap = hashMapUnion(maplist);
		// System.out.println("union: "+ unionmap);

		// System.out.println("size: "+ commonkeys.size());
		for (String key : commonkeys) {

			// System.out.println("Key" + key);
			int count = unionmap.get(key);
			unionmap.put(key, count + 100000);
		}

		return unionmap;
	}

	/**
	 * 
	 * @param maplist List of hashmap
	 * @return intersection of all the hashmaps
	 */
	public static Set<String> hashMapIntersection(List<HashMap<String, Integer>> maplist) {

		Iterator<HashMap<String, Integer>> i = maplist.iterator();
		Map<String, Integer> firstmap = i.next();
		Set<String> result = firstmap.keySet();

		while (i.hasNext()) {
			// System.out.println("result size" + result.size());
			result.retainAll(i.next().keySet());
		}
		Set<String> finalset = new HashSet<String>();
		for (String key : result) {
			finalset.add(key);
		}

		return finalset;
	}

	/**
	 * This method do the union of all the hashmaps in the list
	 * 
	 * @param maplist List of hashmaps
	 * @return union of hashmaps
	 */
	public static HashMap<String, Integer> hashMapUnion(List<HashMap<String, Integer>> maplist) {
		Iterator<HashMap<String, Integer>> i = maplist.iterator();
		HashMap<String, Integer> unionMap = i.next();
		while (i.hasNext()) {
			Map<String, Integer> map = i.next();
			Set<String> keys = map.keySet();
			Iterator<String> setitr = keys.iterator();

			while (setitr.hasNext()) {
				String key = setitr.next();
				if (unionMap.containsKey(key)) {
					int count = map.get(key) + unionMap.get(key);
					unionMap.put(key, count);
				} else {
					unionMap.put(key, map.get(key));
				}
			}
		}
		return unionMap;
	}

	public static void main(String[] args) {

		HashMap<String, Integer> h1 = new HashMap<String, Integer>();
		HashMap<String, Integer> h2 = new HashMap<String, Integer>();
		HashMap<String, Integer> h3 = new HashMap<String, Integer>();

		List<HashMap<String, Integer>> maplist = new ArrayList<HashMap<String, Integer>>();
		maplist.add(h1);
		maplist.add(h2);
		maplist.add(h3);
		h1.put("singh", 1);
		h1.put("a", 1);
		h1.put("b", 1);
		h1.put("c", 1);

		h2.put("c", 1);
		h2.put("b", 1);
		h2.put("d", 1);
		h2.put("r", 1);

		h3.put("c", 1);
		// hashMapIntersection(maplist);
		System.out.println(rankPages(maplist));

	}
}
