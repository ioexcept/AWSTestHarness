AWSTestHarness
==============

Test Harness for AWS: start, stop, create

You will need to create two files: 
AwsCredentials.properties
contents of this file should be:

# Fill in your AWS Access Key ID and Secret Access Key
# http://aws.amazon.com/security-credentials
accessKey =<<YourKey>>
secretKey =<<YourSecretKey>>



and conf/instance.xml 
contents should be ... 

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<comment>AWS Instance Properties</comment>
<entry key="aws_id">Your AMAZON ID </entry>
<entry key="instance_id">Your_Insgtance_ID</entry>
<entry key="key_name">Name of your security key</entry>
</properties>
