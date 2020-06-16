/**
 * 
 */
package com.starwarsame.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.starwarsame.model.Planet;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator;
import software.amazon.awssdk.services.dynamodb.model.Condition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

/**
 * @author Thiago de Luca
 * @date 15/06/2020
 * @version 1.0
 */
@Service
@Repository
public class PlanetServiceImpl implements PlanetService {
	
	private static final String tableName = "planet";
	final DynamoDbAsyncClient client;

    @Autowired
    public PlanetServiceImpl(DynamoDbAsyncClient client) {
        this.client = client;
    }

	@PostConstruct
    public void createTableIfNeeded() throws ExecutionException, InterruptedException {
		
		// Start the call to Amazon Dynamo DB, not blocking to wait for it to finish
		CompletableFuture<ListTablesResponse> responseFuture = client.listTables();

		// Map the response to another CompletableFuture containing just the table names
		CompletableFuture<List<String>> tableNamesFuture =
		        responseFuture.thenApply(ListTablesResponse::tableNames);

		// When future is complete (either successfully or in error), handle the response
		@SuppressWarnings("unused")
		CompletableFuture<List<String>> operationCompleteFuture =
		        tableNamesFuture.whenComplete((tableNames, exception) -> {
	        	
		    if (tableNames != null) {
		    	if (!tableNames.contains(tableName)) {
                    createTable();
                }
		    } else {
		        // Handle the error.
		        exception.printStackTrace();
		    }
		});
    }
	
	private CompletableFuture<CreateTableResponse> createTable() {
        KeySchemaElement keySchemaElement = KeySchemaElement
                .builder()
                .attributeName("id")
                .keyType(KeyType.HASH)
                .build();

        AttributeDefinition attributeDefinition = AttributeDefinition
                .builder()
                .attributeName("id")
                .attributeType(ScalarAttributeType.S)
                .build();

        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(keySchemaElement)
                .attributeDefinitions(attributeDefinition)
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        return client.createTable(request);
    }
	
	@CrossOrigin(origins = "*")
	@Override
	public Mono<Planet> savePlanet(Planet planet) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(planet.getId()).build());
        item.put("name", AttributeValue.builder().s(planet.getName()).build());
        item.put("climate", AttributeValue.builder().s(planet.getClimate()).build());
        item.put("terrain", AttributeValue.builder().s(planet.getTerrain()).build());
        item.put("numberFilms", AttributeValue.builder().s(planet.getNumberFilms()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        
        return Mono.fromCompletionStage(client.putItem(putItemRequest))
							                  .map(putItemResponse -> putItemResponse.attributes())
							                  .map(attributeValueMap -> planet);
    }
	
	@Override
	public Mono<Planet> findById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        
        CompletableFuture<GetItemResponse> completableFuture = client.getItem(getRequest);

        CompletableFuture<Planet> tweetCompletableFuture = completableFuture.thenApplyAsync(GetItemResponse::item)
                .thenApplyAsync(map -> {
                	if(map.isEmpty()) {
                		return null;
                	} else {
	                	return new Planet(map.get("id").s(), 
					 					  map.get("name").s(), 
					 					  map.get("climate").s(), 
					 					  map.get("terrain").s(), 
					 					  map.get("numberFilms").s());
                	}
                });

        return Mono.fromFuture(tweetCompletableFuture);
    }
	
	@Override
	public Mono<List<Planet>> findAll() {
        ScanRequest scanRequest = ScanRequest.builder().tableName(tableName).build();

        CompletableFuture<ScanResponse> future = client.scan(scanRequest);

        CompletableFuture<List<Planet>> response =
                future.thenApplyAsync(ScanResponse::items)
                        .thenApplyAsync(list -> list.parallelStream()
                                .map(map -> new Planet(map.get("id").s(), 
                                					   map.get("name").s(), 
                                					   map.get("climate").s(), 
                                					   map.get("terrain").s(), 
                                					   map.get("numberFilms").s()
                                )).collect(Collectors.toList())
                        );

        return Mono.fromFuture(response);
    }
	
	@Override
	public Mono<List<Planet>> findByName(String name) {
		Map<String, Condition> conditionsMap = new HashMap<>();
		Condition condition = Condition.builder()
                .comparisonOperator(ComparisonOperator.CONTAINS)
                .attributeValueList(AttributeValue.builder().s(name).build())
                .build();

		conditionsMap.put("name", condition);

        ScanRequest scanRequest = ScanRequest.builder().tableName(tableName)
                .scanFilter(conditionsMap)
                .build();

        CompletableFuture<ScanResponse> future = client.scan(scanRequest);

        CompletableFuture<List<Planet>> response =
                future.thenApplyAsync(ScanResponse::items)
                        .thenApplyAsync(list -> list.parallelStream()
                                .map(map -> new Planet(map.get("id").s(), 
                                					   map.get("name").s(), 
                                					   map.get("climate").s(), 
                                					   map.get("terrain").s(), 
                                					   map.get("numberFilms").s()                                					   
                                )).collect(Collectors.toList())
                        );

        return Mono.fromFuture(response);
    }
	
	@Override
	public Mono<Void> delete(String id) {
        Map<String, AttributeValue> attributeValueHashMap = new HashMap<>();
        attributeValueHashMap.put("id", AttributeValue.builder().s(id).build());

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(attributeValueHashMap)
                .build();

        client.deleteItem(deleteItemRequest);

        return Mono.empty();
    }

}