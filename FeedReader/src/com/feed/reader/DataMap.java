package com.feed.reader;

import java.util.LinkedHashMap;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DataMap 
{
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	
	@Persistent
	private LinkedHashMap<String, String> matchDetails;
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	
	public LinkedHashMap<String, String> getMatchDetail() {
		return matchDetails;
	}
	public void setMatchDetail(LinkedHashMap<String, String> matchDetail) {
		this.matchDetails = matchDetail;
	}
}
