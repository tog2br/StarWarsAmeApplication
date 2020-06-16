/**
 * 
 */
package com.starwarsame.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.starwarsame.model.Planet;
import com.starwarsame.model.PlanetExternal;
import com.starwarsame.model.PlanetStarWars;
import com.starwarsame.services.PlanetService;

import reactor.core.publisher.Mono;

/**
 * @author Thiago de Luca
 * @date 15/06/2020
 * @version 1.0
 */

@RestController
public class PlanetController {
	
	private static final String starWarsUrl = "https://swapi.dev/api/";
	
	@Autowired
	PlanetService service;
	
	@GetMapping(value="/planet")
	public Mono<List<Planet>> findAll() {
		return service.findAll();
	}
	
	@GetMapping(value="/planet/{id}")
	public Mono<Planet> findById(@PathVariable String id) {
		return service.findById(id);
	}
	
	@GetMapping(value="/planet/name/{name}")
	public Mono<List<Planet>> findByName(@PathVariable String name) {
		return service.findByName(name);
	}
	
	@PostMapping(value="/planet")
	@CrossOrigin(origins = "*")
	public Mono<Planet> savePlanet(@RequestBody Planet planet) {
		RestTemplate template = new RestTemplate();
		ResponseEntity<PlanetExternal> respStr = template.exchange(starWarsUrl + "planets/" + planet.getId() + "/", HttpMethod.GET, null,  PlanetExternal.class);
		if(respStr.getStatusCodeValue() == 200) {
			planet.setNumberFilms(String.valueOf(respStr.getBody().getFilms().size()));
			return service.savePlanet(planet);
		} else {
			return null;
		}
	}
	
	@DeleteMapping("/planet/{id}")
	public Mono<Void> delete(@PathVariable String id) {
		return service.delete(id);
	}
	
	@GetMapping(value="/starwars/planets/page/{id}")
	@CrossOrigin(origins = "*")
	public List<PlanetExternal> getStarWarsPlanets(@PathVariable String id) {
		RestTemplate template = new RestTemplate();
		try {
			ResponseEntity<PlanetStarWars> respStr = template.exchange(starWarsUrl + "planets/?page=" + id, HttpMethod.GET, null,  PlanetStarWars.class);
			return respStr.getBody().getResults();
		} catch (Exception e) {
			return new ArrayList<PlanetExternal>();
		}
	}

}