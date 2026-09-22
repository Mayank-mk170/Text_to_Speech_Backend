package com.text_to_speech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

import software.amazon.awssdk.regions.Region;

import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.translate.TranslateClient;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.access-key-id}")
    private String awsAccessKeyId;

    @Value("${aws.secret-access-key}")
    private String awsSecretAccessKey;

    @Bean
    public StaticCredentialsProvider awsCredentialsProvider() {

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(
                        awsAccessKeyId,
                        awsSecretAccessKey
                );

        return StaticCredentialsProvider.create(
                credentials
        );
    }

    @Bean
    public TranslateClient translateClient(
            StaticCredentialsProvider credentialsProvider
    ) {

        return TranslateClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public PollyClient pollyClient(
            StaticCredentialsProvider credentialsProvider
    ) {

        return PollyClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(credentialsProvider)
                .build();
    }
}