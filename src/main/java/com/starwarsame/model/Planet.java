/**
 * 
 */
package com.starwarsame.model;

import java.util.UUID;

/**
 * @author Thiago de Luca
 * @date 15/06/2020
 * @version 1.0
 */
public class Planet {
	
	private String id;
	private String name;	
	private String climate;	
	private String terrain;	
	private String numberFilms;	
	
	public Planet() {
		this.id = UUID.randomUUID().toString();
	}
	
	public Planet(String id, String name, String climate, String terrain, String numberFilms) {
		super();
		this.id = id;
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
		this.numberFilms = numberFilms;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClimate() {
		return climate;
	}
	public void setClimate(String climate) {
		this.climate = climate;
	}
	public String getTerrain() {
		return terrain;
	}
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	public String getNumberFilms() {
		return numberFilms;
	}
	public void setNumberFilms(String numberFilms) {
		this.numberFilms = numberFilms;
	}
}