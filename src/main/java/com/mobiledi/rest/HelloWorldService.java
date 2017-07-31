package com.mobiledi.rest;
 
import java.io.File;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
 
@Path("aws/")
public class HelloWorldService {
 
	@GET
	public Response getMsg() {
		String output = "Hello world ";
 
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail
			){
		uploadFileToS3(uploadedInputStream, fileDetail);
		return "File uploaded successfully";
	}
	
	public void uploadFileToS3(InputStream uploadedInputStream, FormDataContentDisposition fileDetail){
		System.out.println("uploadFileToS3");
    	AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
    	String bucketName = "ntran1321-uploads";
    	String keyName = fileDetail.getFileName();
		try {
//			CreateBucketRequest createBucket = new CreateBucketRequest(bucketName);
//			Bucket newBucket = s3client.createBucket(createBucket);
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File(fileDetail.getFileName());
			System.out.println(file.getAbsolutePath());
			
			ObjectMetadata omd = new ObjectMetadata();
			omd.setContentLength(file.length());
			PutObjectRequest putObjectRequest = 
					new PutObjectRequest(bucketName, keyName, uploadedInputStream, omd);
			putObjectRequest.withMetadata(omd);
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			
			System.out.println(putObjectRequest.getFile());
			s3client.putObject(putObjectRequest);

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException.");
			System.out.println("Error Message: " + ace.getMessage());
		}
    }
 
}