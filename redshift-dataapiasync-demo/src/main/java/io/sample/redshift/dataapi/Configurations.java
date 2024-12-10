package io.sample.redshift.dataapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.redshiftdata.RedshiftDataAsyncClient;
import software.amazon.awssdk.services.redshiftdata.RedshiftDataClient;

@Configuration
public class Configurations {
    @Value("${redshift.accessKey}")
    private String accessKey;
    @Value("${redshift.secretKey}")
    private String secretKey;
    @Value("${redshift.region}")
    private String region;


    @Bean
    public RedshiftDataAsyncClient getDataClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.builder()
                .accessKeyId(accessKey)
                .secretAccessKey(secretKey)
                .build();

        return RedshiftDataAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
