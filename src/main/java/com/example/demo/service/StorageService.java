package com.example.demo.service;

import static com.example.demo.Config.StorageConfig.getS3Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.utils.IoUtils;

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
	
	public byte[] downloadFile(String fileName){
		
		GetObjectRequest getObjectRequest = null;
		//getObject
		getObjectRequest =  GetObjectRequest.builder().bucket("senath").key(fileName).build();
		//converting to inputstream
		ResponseInputStream inputStream = getS3Client().getObject(getObjectRequest);
		
		try {
			byte[] content = IoUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	
	}	


	
	public String deleteFile(String fileName) {
		
		 DeleteObjectRequest deleteObjectRequest = null;
		 deleteObjectRequest = DeleteObjectRequest.builder().bucket("senath").key(fileName).build();
		 DeleteObjectResponse deleteObjectResponse = getS3Client().deleteObject(deleteObjectRequest);
		 return fileName + " Deleted !!";
		
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
