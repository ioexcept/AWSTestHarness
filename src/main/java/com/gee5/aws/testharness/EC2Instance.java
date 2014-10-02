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
	static Logger logger = Logger.getLogger("AWSTestHarness");

	private AmazonEC2 amazonEC2Client = null;
	private EC2Instance singletonInstance = null;

	private Region instanceRegion = null;
	private AWSCredentials credentials;

	private Properties instanceProperties = null;
	private String amazonMachineImage = null;
	private String instanceID = null;
	private String keyName = null;
	private String CREDENTIALS = "AwsCredentials.properties";
	
	public EC2Instance() throws Exception {
		initProperties();
	}
	public EC2Instance(String credentialFile) throws Exception {
		CREDENTIALS = credentialFile;
		initProperties();
	}

	private void initProperties() throws Exception {
		logger.info("Using credential file: " + CREDENTIALS);
		
		instanceProperties = new Properties();
		instanceProperties.loadFromXML(new FileInputStream("conf/instance.xml"));

		logger.info("Instance configuration loaded ...");

		amazonMachineImage = instanceProperties.getProperty("aws_id");
		instanceID = instanceProperties.getProperty("instance_id");
		keyName = instanceProperties.getProperty("keyName");

		logger.info("amazonMachineImage: " + amazonMachineImage);
		logger.info("instanceID: " + instanceID);
		logger.info("keyName: " + keyName);

		logger.info("Initializing AWSTestHarness ... ");
		logger.info("... configuring Region");
		instanceRegion = ConfigRegion.fetchAWSRegion(instanceProperties.getProperty("region"));

		logger.info("... Instantiating credentials");
		credentials = new PropertiesCredentials(new File(CREDENTIALS));

	}

	// public static EC2Instance getInstance(){
	// singletonInstance
	// }

	public AmazonEC2 getInstance() throws Exception {
		logger.info("Attemping to instantiate local EC2Client ...");
		amazonEC2Client = null;
		amazonEC2Client = new AmazonEC2Client(credentials);
		amazonEC2Client.setRegion(instanceRegion);
		logger.info("AmazonEC2Client initialized");
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

	public void setInstanceRegion(String ir) throws Exception {
		this.instanceRegion = ConfigRegion.fetchAWSRegion(ir);
	}

}
