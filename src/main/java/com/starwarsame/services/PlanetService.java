/**
 * 
 */
package com.starwarsame.services;

import java.util.List;

import com.starwarsame.model.Planet;

import reactor.core.publisher.Mono;

/**
 * @author Thiago de Luca
 * @date 15/06/2020
 * @version 1.0
 */
public interface PlanetService {
	
	Mono<List<Planet>> findAll();
	
	Mono<Planet> findById(String id);
	
	Mono<Planet> savePlanet(Planet planet);
	
	Mono<List<Planet>> findByName(String name);
	
	Mono<Void> delete(String id);

}