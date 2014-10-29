package com.gee5.aws.testharness;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;

public class CLI {
	 static Logger logger = Logger.getLogger("CLI");
	 EC2Instance ec2Instance = null;
	 private AmazonEC2 amazonEC2Client = null;

	public CLI()  throws IOException{
		try{
			logger.info("CLI instance initiated ...");
			ec2Instance = new EC2Instance();

			detail();	

		}catch(Exception ex){
			logger.error("FATAL",ex);
		}

	}
	
	
	private void detail() throws Exception{
		amazonEC2Client = ec2Instance.getInstance();	
        DescribeAvailabilityZonesResult availabilityZonesResult = amazonEC2Client.describeAvailabilityZones();
        
        int size = availabilityZonesResult.getAvailabilityZones().size();
        
        System.out.println("You have access to " + size + " Availability Zones.");
        AvailabilityZone zone = null;
        
        for (int index = 0 ; index < size; index++){
        	zone = availabilityZonesResult.getAvailabilityZones().get(index);
        	System.out.println("REGION NAME: " + zone.getRegionName());
        	System.out.println("ZONE_NAME: " + zone.getZoneName());
        }
        
        
        
        
	}
	

	public static void main(String[] args) throws IOException{
		new CLI(); 	
	}

}
