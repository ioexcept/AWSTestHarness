package com.gee5.aws.testharness;

import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

public class InstanceStartupListener extends Thread {
	 static Logger logger = Logger.getLogger("AWSTestHarness");

	private EC2Instance ec2Instance = null;

	public InstanceStartupListener(String targetInstance) throws Exception {
		ec2Instance = new EC2Instance();
		ec2Instance.setInstanceID(targetInstance);
	}

	public void run() {
		try {
    		System.out.println("************************************************************************");
			AmazonEC2 amazonEC2Client = ec2Instance.getInstance();
			boolean monitor = true;
			int instanceStateID = -1;
			DescribeInstancesResult describeInstancesRequest = null;
			List<Reservation> reservations = null;
			Instance instRef = null;
			while(monitor){
			
		        describeInstancesRequest = amazonEC2Client.describeInstances();
		        reservations = describeInstancesRequest.getReservations();
		        
		        for (Reservation reservation : reservations) {
		         	for(Instance instance : reservation.getInstances()){
		         		if(instance.getInstanceId().equals(ec2Instance.getInstanceID())){
			        		instanceStateID = instance.getState().getCode();
			        		System.out.println("State.Name: " + instance.getState().getName());
			        		System.out.println("State.Code: " + instanceStateID);
			        		System.out.println("StateReason: " + instance.getStateReason());
			        		instRef = instance;
			        		System.out.println("************************************************************************");
		         		}
		        	}
		        }

		        if(instanceStateID == InstanceState.STOPPED){
		        	System.out.println("Instance is now in the 'STOPPED' State ...");
		        	monitor = false;
		        }else if(instanceStateID == InstanceState.RUNNING){
		        	System.out.println("Instance is now in the 'RUNNING' State ...");
					System.out.println("InstanceId: " + instRef.getInstanceId());
					System.out.println("InstanceType: " + instRef.getInstanceType());
					System.out.println("Architecture: " + instRef.getArchitecture());
					System.out.println("KeyName: " + instRef.getKeyName());
					System.out.println("ImageId: " + instRef.getImageId());
					System.out.println("LaunchTime: " + instRef.getLaunchTime());
					System.out.println("VpcId: " + instRef.getVpcId());
					System.out.println("PublicDnsName: " + instRef.getPublicDnsName());
					System.out.println("PublicIpAddress: " + instRef.getPublicIpAddress());
		        	
		        	
		        	monitor = false;
		        }else{
		        	Thread.sleep(10000);
		        }
			}


		}catch(AmazonServiceException ase){
			logger.error("FATAL-ASE",ase);
		} catch (Exception ex) {
			logger.error("FATAL",ex);
		}
	}

}
