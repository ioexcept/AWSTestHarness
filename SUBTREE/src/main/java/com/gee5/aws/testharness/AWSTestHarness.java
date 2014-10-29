package com.gee5.aws.testharness;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.*;
import com.amazonaws.services.ec2.model.*;



public class AWSTestHarness {
	 static Logger log = Logger.getLogger("AWSTestHarness");
	 EC2Instance ec2Instance = null;
	 private AmazonEC2 amazonEC2Client = null;
	
	public AWSTestHarness(String[] args) throws IOException{
		try{
			String credentialFile = null;
			if(args.length < 2){
				showHelp();
				System.exit(-1);
			}
			
			for(int index = 0; index <args.length; index++){
				if(args[index].equalsIgnoreCase("-F")){
					index++;
					credentialFile = args[index];
				}
			}
			
			ec2Instance = new EC2Instance(credentialFile);
			
			
//			WINDOWS
//			ec2Instance.setInstanceID("i-1a828811");
			
			//LINUX
//			ec2Instance.setInstanceID("i-1c068913");
			
//			startEC2Instance();

//			stopEC2Instance();
			
//			createEC2Instance();
			
			describeAmazonInstance();
			
			
			
			
			
		}catch(AmazonServiceException ase){
			log.error("FATAL-ASE",ase);
		}catch(Exception ex){
			log.error("FATAL",ex);
		}
		
	}

	private void stopEC2Instance() throws Exception{
		log.info("****STOP EC2 INSTANCE ****");
		amazonEC2Client = ec2Instance.getInstance();			
		if(isInstanceInState(ec2Instance.getInstanceID(), InstanceState.RUNNING)){
			List<String> instancesToStart = new ArrayList<String>();
		    instancesToStart.add(ec2Instance.getInstanceID());
		    StopInstancesRequest stopr = new StopInstancesRequest();
		    stopr.setInstanceIds(instancesToStart);    
		    amazonEC2Client.stopInstances(stopr);   
		    log.info("AWS Instance [" + ec2Instance.getInstanceID() + "] shutting down ... ");
		    new InstanceStartupListener(ec2Instance.getInstanceID()).start();
		}else{
			log.warn("Instance ID [" + ec2Instance.getInstanceID() + "] was slated to be started, however that instance is not in the 'RUNNING' state");;
		}
	}
	
	private void startEC2Instance() throws Exception{
		amazonEC2Client = ec2Instance.getInstance();
		if(isInstanceInState(ec2Instance.getInstanceID(), InstanceState.STOPPED)){
			List<String> instancesToStart = new ArrayList<String>();
		    instancesToStart.add(ec2Instance.getInstanceID());
		    StartInstancesRequest startr = new StartInstancesRequest();
		    startr.setInstanceIds(instancesToStart);    
		    amazonEC2Client.startInstances(startr); 
		    log.info("AWS Instance starting up ... ");
		    new InstanceStartupListener(ec2Instance.getInstanceID()).start();
		}else{
			log.warn("Instance ID [" + ec2Instance.getInstanceID() + "] was slated to be started, however that instance is not in the 'STOPPED' state");;
		}
	}
    

	private void describeAmazonInstance() throws Exception{
		log.info("**** DESCRIBE AMAZON INSTANCE ****");
		amazonEC2Client = ec2Instance.getInstance();
		DescribeInstancesResult describeInstancesRequest = null;
		List<Reservation> reservations = null;
		describeInstancesRequest = amazonEC2Client.describeInstances();
		reservations = describeInstancesRequest.getReservations();
		int currentInstance = 1;
		int instanceCount = reservations.size();
		System.out.println(">> Number of Instances found [" + instanceCount + "]");   
		for (Reservation reservation : reservations) {
			for(Instance instance : reservation.getInstances()){
				System.out.println("Instance " + currentInstance + " of " + instanceCount); 
				System.out.println("InstanceId: " + instance.getInstanceId());
				System.out.println("InstanceType: " + instance.getInstanceType());
				System.out.println("Architecture: " + instance.getArchitecture());
				System.out.println("KeyName: " + instance.getKeyName());
				System.out.println("ImageId: " + instance.getImageId());
				System.out.println("LaunchTime: " + instance.getLaunchTime());
				System.out.println("VpcId: " + instance.getVpcId());
				System.out.println("PublicDnsName: " + instance.getPublicDnsName());
				System.out.println("PublicIpAddress: " + instance.getPublicIpAddress());
				System.out.println("State: " + instance.getState());
				System.out.println("State.Name: " + instance.getState().getName());
				System.out.println("State.Code: " + instance.getState().getCode());
				System.out.println("StateReason: " + instance.getStateReason());
				System.out.println("************************************************************************");
			}//end inner-for
		}//end-for
		        
	}
	
	
	public boolean isInstanceInState(String instanceID, int state) throws Exception{
		log.info("**** IS INSTANCE IN STATE ****");
		boolean inState = false;
		amazonEC2Client = ec2Instance.getInstance();
		DescribeInstancesResult describeInstancesRequest = null;
		List<Reservation> reservations = null;
		
		describeInstancesRequest = amazonEC2Client.describeInstances();
		reservations = describeInstancesRequest.getReservations();
		        
		for (Reservation reservation : reservations) {
			for(Instance instance : reservation.getInstances()){
				if(instance.getInstanceId().equals(ec2Instance.getInstanceID())){
					if(instance.getState().getCode() == state) inState = true;
					return inState;
				}
			}//end inner-for
		}//end-for
		
		throw new Exception("Instance ID [" + instanceID + "] does not exist or cannot be found");

	}

	
	private void createEC2Instance() throws Exception{
		amazonEC2Client = ec2Instance.getInstance();
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

		runInstancesRequest.withImageId(ec2Instance.getAmazonMachineImage())
			                     .withInstanceType("t2.micro")
			                     .withMinCount(1)
			                     .withMaxCount(1)
			                     .withKeyName(ec2Instance.getKeyName())
			                     .withSecurityGroups("HTTP", "Postgres Home Incoming", "SSH", "RewardStyle");
		RunInstancesResult runInstancesResult = amazonEC2Client.runInstances(runInstancesRequest);
		
		ec2Instance.setInstanceID(runInstancesResult.getReservation().getInstances().get(0).getInstanceId());
	    new InstanceStartupListener(ec2Instance.getInstanceID()).start();
		
	}
	
	private void showHelp(){
		System.out.println("Application requires user defined arguments to execute");
		System.out.println("\tThose arguements are: -f <credential-file>");
		System.out.println("\tThe credntial file should contain the Amazon access and secret key's in the following format ");
		System.out.println("\taccessKey = <<your access key>>");
		System.out.println("\tsecretKey = <<your secret key>>");
	}
	
	public static void main(String[] args) throws IOException{
		new AWSTestHarness(args); 	
	}

	
	/*
	
	https://aws.amazon.com/articles/3586
http://www.kpbird.com/2013/09/aws-sdk-for-java-tutorial-2-ec2.html
http://cloud.dzone.com/articles/step-step-aws-ec2-tutorial
http://www.programcreek.com/java-api-examples/index.php?api=com.amazonaws.services.ec2.AmazonEC2Client
http://docs.aws.amazon.com/powershell/latest/userguide/pstools-ec2-get-amis.html#pstools-ec2-get-image
http://qingyeec2.blogspot.com/
	
	
	*/
	
}