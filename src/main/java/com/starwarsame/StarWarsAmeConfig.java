/**
 * 
 */
package com.starwarsame;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;

/**
 * @author Thiago de Luca
 * @date 15/06/2020
 * @version 1.0
 */

@Configuration
public class StarWarsAmeConfig {
	
	@Value("${aws.accessKey}")
    String accessKey;

    @Value("${aws.secretKey}")
    String secretKey;

    @Value("${dynamodb.endpoint:}")
    String dynamoEndpoint;

    @Bean
    AwsBasicCredentials awsBasicCredentials(){
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean
    DynamoDbAsyncClient dynamoDbAsyncClient(AwsBasicCredentials awsBasicCredentials){
        DynamoDbAsyncClientBuilder clientBuilder = DynamoDbAsyncClient.builder();
        clientBuilder
                .region(Region.EU_WEST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials));
                if(!dynamoEndpoint.isEmpty()){
                    clientBuilder.endpointOverride(URI.create(dynamoEndpoint));
                }
        return clientBuilder.build();
    }

}