package com.gee5.aws.testharness;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;

public class EC2Instance {
	 static Logger log = Logger.getLogger("AWSTestHarness");
	 
	 private AmazonEC2 amazonEC2Client = null;
	 private EC2Instance singletonInstance = null;
	 
	 private Region instanceRegion = null;
	 private final AWSCredentials credentials;
	 
	 private Properties instanceProperties = null;
	 private String amazonMachineImage = null;
	 private String instanceID = null;
	 private String keyName = null; 
	 
	 public EC2Instance() throws Exception{
		instanceProperties = new Properties();
		instanceProperties.loadFromXML(new FileInputStream("conf/instance.xml"));
		
		log.info("Instance configuration loaded ...");
		
		amazonMachineImage = instanceProperties.getProperty("aws_id");
		instanceID = instanceProperties.getProperty("instance_id");
		keyName = instanceProperties.getProperty("keyName");
		
		log.info("amazonMachineImage: " + amazonMachineImage);
		log.info("instanceID: " + instanceID);
		log.info("keyName: " + keyName);
		
		log.info("Initializing AWSTestHarness ... ");
		log.info("... configuring Region");
		instanceRegion = ConfigRegion.fetchAWSRegion(instanceProperties.getProperty("region"));
		
		log.info("... Instantiating credentials");
		credentials = new PropertiesCredentials(new File("AwsCredentials.properties"));
	}
	
	 
//	 public static EC2Instance getInstance(){
//		 singletonInstance
//	 }
	 
	 
	public AmazonEC2 getInstance() throws Exception{
		log.info("Attemping to instantiate local EC2Client ...");
		amazonEC2Client = null;
		amazonEC2Client = new AmazonEC2Client(credentials);	
	    amazonEC2Client.setRegion(instanceRegion);
	    log.info("AmazonEC2Client initialized");
	    return amazonEC2Client;
	}


	public Properties getInstanceProperties() {
		return instanceProperties;
	}


	public void setInstanceProperties(Properties instanceProperties) {
		this.instanceProperties = instanceProperties;
	}


	public String getAmazonMachineImage() {
		return amazonMachineImage;
	}


	public void setAmazonMachineImage(String amazonMachineImage) {
		this.amazonMachineImage = amazonMachineImage;
	}


	public String getInstanceID() {
		return instanceID;
	}


	public void setInstanceID(String instanceID) {
		this.instanceID = instanceID;
	}


	public String getKeyName() {
		return keyName;
	}


	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}


	public Region getInstanceRegion() {
		return instanceRegion;
	}


	public void setInstanceRegion(Region instanceRegion) {
		this.instanceRegion = instanceRegion;
	}
	
	public void setInstanceRegion(String ir) throws Exception{
		this.instanceRegion = ConfigRegion.fetchAWSRegion(ir);
	}

}
