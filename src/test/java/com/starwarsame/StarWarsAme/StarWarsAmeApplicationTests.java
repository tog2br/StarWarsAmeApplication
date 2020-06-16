package com.starwarsame.StarWarsAme;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.starwarsame.model.Planet;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StarWarsAmeApplicationTests {
	
	@Autowired
    public WebTestClient webTestClient;
	
	@Test
    @Order(1)
    public void savePlanet() {
		Planet planet = new Planet("1", "Tatooine", "arid", "desert", "0");
		
		webTestClient.post()
				     .uri("/planet")
				     .body(Mono.just(planet), Planet.class)
				     .exchange()
				     .expectStatus()
		             .isOk()
				     .returnResult(Planet.class);
    }

    @Test
    @Order(2)
    public void getPlanetById() {
        webTestClient.get()
        		.uri("/planet/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Planet.class);
    }

    @Test
    @Order(3)
    public void getAllPlanets() {
        webTestClient.get()
        		.uri("/planet")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Planet.class);
    }

    @Test
    @Order(4)
    public void getPlanetByName() {
        webTestClient.get()
        		.uri("/planet/name/Tatoo")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Planet.class);
    }

    @Test
    @Order(5)
    public void getStarWarsPlanets() {
        webTestClient.get()
        		.uri("/starwars/planets/page/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Planet.class);
    }
    
    @Test
    @Order(6)
    public void deletePlanet() {
		webTestClient.delete()
				     .uri("/planet/1")
				     .exchange()
				     .expectStatus()
		             .isOk()
				     .returnResult(Void.class);
    }

}