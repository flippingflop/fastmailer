package com.flippingflop.fastmailer.config.aws;

import io.awspring.cloud.sqs.config.SqsBootstrapConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Import(SqsBootstrapConfiguration.class)
@RequiredArgsConstructor
public class SqsConfig {

    final AwsCredentialsProvider awsCredentialsProvider;

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder().credentialsProvider(awsCredentialsProvider).region(Region.AP_NORTHEAST_2).build();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

}
