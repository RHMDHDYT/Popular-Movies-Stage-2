package com.rahmad.popularmoviesstage2.models.trailer;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Trailer{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<TrailerResultsItem> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<TrailerResultsItem> results){
		this.results = results;
	}

	public List<TrailerResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"Trailer{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}