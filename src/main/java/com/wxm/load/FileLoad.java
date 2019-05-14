package com.wxm.load;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.wxm.domain.Movie;
import com.wxm.domain.Rating;

public class FileLoad {
	private static final String movieFilePath = "/Users/xiaomanwang/data/ml-20m/movies.csv";
	private static final String ratingFilePath = "/Users/xiaomanwang/data/ml-20m/ratings-p1.csv";
	
	public static List<Movie> loadMovie(int lineNumber) throws Exception{
		List<String> lines = Files.readLines(new File(movieFilePath), Charsets.UTF_8);
		List<Movie> result = new ArrayList<>();
		if(!CollectionUtils.isEmpty(lines)){
			for(String l : lines){
				Movie m = new Movie();
				String[] lineArray = l.split(",");
				m.setId(Integer.valueOf(lineArray[0]));
				m.setName(lineArray[1]);
				m.setType(lineArray[2]);
				result.add(m);
				if(lineNumber-- == 0){
					break;
				}
			};
		}
		return result;
	}
	
	public static Map<Integer,String> loadMovie() throws Exception{
		List<String> lines = Files.readLines(new File(movieFilePath), Charsets.UTF_8);
		Map<Integer,String> result = new HashMap<>();
		if(!CollectionUtils.isEmpty(lines)){
			for(String l : lines){
				String[] lineArray = l.split(",");
				result.put(Integer.valueOf(lineArray[0]),lineArray[1]+","+lineArray[2]);
			};
		}
		return result;
	}
	
	public static List<Rating> loadRating(int lineNumber) throws Exception{
		List<String> lines = Files.readLines(new File(ratingFilePath), Charsets.UTF_8);
		List<Rating> result = new ArrayList<>();
		if(!CollectionUtils.isEmpty(lines)){
			for(String l : lines){
				Rating r = new Rating();
				String[] lineArray = l.split(",");
				r.setUserId(Integer.valueOf(lineArray[0]));
				r.setMovieId(Integer.valueOf(lineArray[1]));
				r.setRating(Float.valueOf(lineArray[2]));
				r.setTimestamp(Long.valueOf(lineArray[3]));
				result.add(r);
				if(lineNumber-- == 0){
					break;
				}
			};
		}
		return result;
	}
	
	public static Map<Integer,Set<Integer>> loadRatingMap() throws Exception{
		List<String> lines = Files.readLines(new File(ratingFilePath), Charsets.UTF_8);
		Map<Integer,Set<Integer>> result = new HashMap<Integer,Set<Integer>>();
		if(!CollectionUtils.isEmpty(lines)){
			for(String l : lines){
				String[] lineArray = l.split(",");
				int userId = Integer.valueOf(lineArray[0]);
				int movieId = Integer.valueOf(lineArray[1]);
				Set<Integer> movies = result.getOrDefault(userId, new HashSet<>());
				movies.add(movieId);
				result.put(userId, movies);
			};
		}
		return result;
	}
}
