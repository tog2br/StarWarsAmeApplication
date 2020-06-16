/**
 * 
 */
package com.starwarsame.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Thiago de Luca
 * @date 16/06/2020
 * @version 1.0
 */
public class PlanetStarWars implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String count;
	private String next;
	private String previous;
	private List<PlanetExternal> results;
	private String detail;
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getPrevious() {
		return previous;
	}
	public void setPrevious(String previous) {
		this.previous = previous;
	}
	public List<PlanetExternal> getResults() {
		return results;
	}
	public void setResults(List<PlanetExternal> results) {
		this.results = results;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}