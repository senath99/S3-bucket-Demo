package com.example.demo.Config;


import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class StorageConfig {

	//Single instance
	private static S3Client s3Client = null;
	
	public static S3Client getS3Client() {
		
		if(s3Client == null) {
			
			s3Client = S3Client.builder().region(Region.US_EAST_2).build();
		}
		return s3Client;
		
		
	}
}
