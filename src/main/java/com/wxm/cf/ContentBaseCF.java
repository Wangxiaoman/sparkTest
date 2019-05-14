package com.wxm.cf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Objects;
import com.wxm.domain.ItemSim;
import com.wxm.domain.ItemSimScore;
import com.wxm.load.FileLoad;

public class ContentBaseCF {
	// 计算物料相似度分数
	public static Map<ItemSim, Double> getItemSim(Map<Integer, Set<Integer>> userMovies) throws Exception {
		Map<ItemSim, Double> result = new HashMap<>();
		Map<ItemSim, Integer> itemSimMatrix = new HashMap<>();
		Map<Integer, Integer> movieCount = new HashMap<>();
		for (Entry<Integer, Set<Integer>> e : userMovies.entrySet()) {
			Set<Integer> items = e.getValue();
			for (Integer fromItem : items) {
				movieCount.put(fromItem, items.size());
				for (Integer toItem : items) {
					if (!Objects.equal(fromItem, toItem)) {
						ItemSim is = new ItemSim(fromItem, toItem);
						Integer s = itemSimMatrix.getOrDefault(is, 0);
						itemSimMatrix.put(is, s + 1);
					}
				}
			}
		}

		for (Entry<ItemSim, Integer> entry : itemSimMatrix.entrySet()) {
			ItemSim is = entry.getKey();
			double sim = entry.getValue()
					/ Math.sqrt(movieCount.get(is.getFromItemId()) * movieCount.get(is.getToItemId()));
			result.put(is, sim);
		}
		return result;
	}

	// 将相同物料的相似物料以及打分合并
	public static Map<Integer, List<ItemSimScore>> getItemSimList(Map<ItemSim, Double> itemSim) {
		Map<Integer, List<ItemSimScore>> result = new HashMap<>();

		for (Entry<ItemSim, Double> entry : itemSim.entrySet()) {
			int fromItem = entry.getKey().getFromItemId();
			int toItem = entry.getKey().getToItemId();
			Double sim = entry.getValue();

			List<ItemSimScore> iss = result.getOrDefault(fromItem, new ArrayList<>());
			iss.add(new ItemSimScore(toItem, sim));
			result.put(fromItem, iss);
		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		// Map<Integer,Set<Integer>> userMovies = new HashMap<>();
		// Set<Integer> s1 = new HashSet<>();
		// s1.add(10);
		// s1.add(11);
		// userMovies.put(1, s1);
		//
		// Set<Integer> s2 = new HashSet<>();
		// s2.add(11);
		// s2.add(12);
		// userMovies.put(2, s2);
		//
		// Set<Integer> s3 = new HashSet<>();
		// s3.add(10);
		// s3.add(13);
		// s3.add(11);
		// userMovies.put(3, s3);
		//
		// Set<Integer> s4 = new HashSet<>();
		// s4.add(14);
		// s4.add(15);
		// userMovies.put(4, s4);

		Map<Integer, Set<Integer>> userMovies = FileLoad.loadRatingMap();
		System.out.println("user movie count:" + userMovies.size());

		Map<ItemSim, Double> itemSim = getItemSim(userMovies);
		System.out.println("itemSime count : " + itemSim.size());

		Map<Integer, String> movieMap = FileLoad.loadMovie();
		System.out.println("movieMap count : " + movieMap.size());

		Map<Integer, List<ItemSimScore>> itemSimMap = getItemSimList(itemSim);
		int i = 100;
		for (Entry<Integer, List<ItemSimScore>> entry : itemSimMap.entrySet()) {
			if (i-- == 0) {
				break;
			}
			int item = entry.getKey();
			System.out.println("item name:" + movieMap.get(item));

			List<ItemSimScore> items = entry.getValue();
			Collections.sort(items, new Comparator<ItemSimScore>() {
				@Override
				public int compare(ItemSimScore o1, ItemSimScore o2) {
					if (Objects.equal(o1.getScore(), o2.getScore())) {
						return 0;
					}
					return o1.getScore() - o2.getScore() > 0 ? -1 : 1;
				}
			});

			for (int j = 0; j < Math.min(3, items.size()); j++) {
				ItemSimScore iss = items.get(j);
				System.out.println("sim item name:" + movieMap.get(iss.getId()) + ",sim score:" + iss.getScore());
			}
			
			System.out.println("------------------------------------------");
		}
	}
}
