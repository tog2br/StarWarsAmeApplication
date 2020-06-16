package com.starwarsame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StarWarsAmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsAmeApplication.class, args);
		System.out.println("           ,-----.");
		System.out.println("         ,'_//_||_\\_`.");
		System.out.println("        //<<::8[O]::>\\      EndPoints:");
		System.out.println("       _|-----------|_       ");
		System.out.println("   :::|  | ====-=- |  |:::  /starwars/planets/page/{id} (GET) - return Planets by Page from Star Wars API");
		System.out.println("   :::|  | -=-==== |  |:::  /planet (GET) - return all Planets");
		System.out.println("   :::\\  | ::::|()||  /:::  /planet/{id} (GET) - return Planet by Id");
		System.out.println("   ::::| | ....|()|| |::::  /planet/name/{name} (GET) - return Planets like a name");
		System.out.println("       | |_________| |      /planet (POST) - insert a Planet");
		System.out.println("       | |\\_______/| |      /planet/{id} (DELETE) - Delete a planet by id");
		System.out.println("      //   \\ //   \\ //   \\     ");
		System.out.println("      `---' `---' `---'     author - Thiago de Luca (tog2br@gmail.com)");
	}

}