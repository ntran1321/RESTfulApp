package com.mobiledi.rest;
 
import java.io.File;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sun.jersey.core.header.FormDataContentDisposition;
 
@Path("hello/")
public class HelloWorldService {
 
	@GET
	@Path("ping")
	public Response getMsg() {
 
		String output = "Hello world ";
 
		return Response.status(200).entity(output).build();
 
	}
	
	@POST
	@Path("/upload")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(
//			@FormParam("file") InputStream uploadedInputStream,
//			@FormParam("file") FormDataContentDisposition fileDetail
			){
//		uploadFileToS3(uploadedInputStream, fileDetail);
		return "File uploaded successfully";
	}
	
	public void uploadFileToS3(InputStream uploadedInputStream, FormDataContentDisposition fileDetail){
		System.out.println("uploadFileToS3");
    	AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
    	String bucketName = "ntran1321-profilepics";
    	String keyName = "profilepic-01";
		try {
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File(fileDetail.getFileName());
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
    }
 
}