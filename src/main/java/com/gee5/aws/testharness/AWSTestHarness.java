package com.gee5.aws.testharness;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;

public class AWSTestHarness {
	 static Logger log = Logger.getLogger("AWSTestHarness");
	 private Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	 private AWSCredentials credentials = null;
	 private AmazonEC2 amazonEC2Client = null;
	 Properties instanceProperties = null;
	 private String AMAZON_ID = null;
	 private String IMAGE_ID = null;
	 private String KEY_NAME = null;
	
	public AWSTestHarness() throws IOException{
		try{
			instanceProperties = new Properties();
			instanceProperties.loadFromXML(new FileInputStream("conf/instance.xml"));
			AMAZON_ID = instanceProperties.getProperty("aws_id");
			IMAGE_ID = instanceProperties.getProperty("instance_id");
			KEY_NAME = instanceProperties.getProperty("key_name");

			log.info("Initializing AWSTestHarness ... ");
			log.info("... configuring Region");
			usWest2 = Region.getRegion(Regions.US_WEST_2);
			log.info("... Instantiating credentials");
			credentials = new PropertiesCredentials(new File("AwsCredentials.properties"));
			log.info("... starting instance");
			stopEC2Instance();
//			startEC2Instance();
//			createEC2Instance();
		}catch(Exception ex){
			log.error("FATAL",ex);
		}
		
		log.info("exiting ... ");
	}

	
	private void initAWSInstance() throws Exception{
		amazonEC2Client = null;
		amazonEC2Client = new AmazonEC2Client(credentials);	
	    amazonEC2Client.setRegion(usWest2);
	    log.info("AmazonEC2Client initialized");
	}
	
	
	private void stopEC2Instance() throws Exception{
		initAWSInstance();		
		List<String> instancesToStart = new ArrayList<String>();
	    instancesToStart.add(IMAGE_ID);
	    StopInstancesRequest stopr = new StopInstancesRequest();
	    stopr.setInstanceIds(instancesToStart);    
	    amazonEC2Client.stopInstances(stopr);    
	    log.info("AWS Instance shutting down ... ");

	}
//	 StopInstancesRequest stopIR = new StopInstancesRequest(instanceIds);
	
	private void startEC2Instance() throws Exception{
		initAWSInstance();		
		List<String> instancesToStart = new ArrayList<String>();
	    instancesToStart.add(IMAGE_ID);
	    StartInstancesRequest startr = new StartInstancesRequest();
	    startr.setInstanceIds(instancesToStart);    
	    amazonEC2Client.startInstances(startr); 
	    log.info("AWS Instance starting up ... ");
	}
    
	private void createEC2Instance() throws Exception{
		initAWSInstance();
	    
	    /*
	    DescribeImagesResult res = amazonEC2Client.describeImages(new DescribeImagesRequest().withImageIds(imageID));
	    if (res.getImages().size() > 0 && res.getImages().get(0).getImageId().equals(imageID)) {
	        Image image = res.getImages().get(0);
	        log.info(image.getImageLocation() + " :: " + image.getDescription());
	      } else {
	    	  log.info("No such AMI: i-bad8d6b1");
	      }
	    */
//	    /*
	    
	    
		RunInstancesRequest runInstancesRequest = 
				  new RunInstancesRequest();

			  runInstancesRequest.withImageId(AMAZON_ID)
			                     .withInstanceType("t2.micro")
			                     .withMinCount(1)
			                     .withMaxCount(1)
			                     .withKeyName(KEY_NAME)
			                     .withSecurityGroups("HTTP", "Postgres Home Incoming", "SSH", "RewardStyle");
			  RunInstancesResult runInstancesResult = 
					  amazonEC2Client.runInstances(runInstancesRequest);
//		*/
	}
	
	public static void main(String[] args) throws IOException{
		new AWSTestHarness(); 	
	}

}