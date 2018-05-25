package com.carcomehome.config;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Created by tedonema on 30/03/2016.
 */
@Configuration
//@EnableJpaRepositories(basePackages = "com.devopsbuddy.backend.persistence.repositories")
//@EntityScan(basePackages = "com.devopsbuddy.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/comehomecar/application-common.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Bean
    public AmazonS3Client s3Client() {    	
    	
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials("", "");
    	AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
    			                .withRegion(Regions.US_EAST_2)
    	                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
    	                        .build();
    	
    	return (AmazonS3Client) s3Client;
    	
    }
}
