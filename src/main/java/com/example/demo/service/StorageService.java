package com.example.demo.service;

import static com.example.demo.Config.StorageConfig.getS3Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
public class StorageService {
	
	public String uploadFile(MultipartFile multipartFile) {
		File file = convertMultiPartFiletoFile(multipartFile);
		String fileName = System.currentTimeMillis()+"_"+multipartFile.getOriginalFilename();
		getS3Client().putObject(PutObjectRequest.builder().bucket("senath").key(fileName).build(), RequestBody.fromFile(file));
		file.delete();
		return "File Uploaded "+ fileName;
		
		
	}

	//Convert multipartfile to file
	
	
	
	
	private File convertMultiPartFiletoFile(MultipartFile file) {
		
		File convertedFile = new File(file.getOriginalFilename());
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
			fileOutputStream.write(file.getBytes());
			
		}catch(IOException e) {
			
			log.error("Error converting multipart to File "+e);
		}
		return convertedFile;
	}
}
