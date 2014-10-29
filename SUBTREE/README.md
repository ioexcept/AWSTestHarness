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
<!-- These are the pre-defined (amazon, public shared and your private AMI's) -->
<entry key="aws_id">Amazon Machine ID</entry>
<!-- The current instance you are working with -->
<entry key="instance_id">Your_Insgtance_ID</entry>
<!-- same of you key -->
<entry key="key_name">Name of your security key</entry>
<!--
*** MUST MATCH ***
AP_NORTHEAST_1
AP_SOUTHEAST_1
AP_SOUTHEAST_2
CN_NORTH_1
EU_WEST_1
GovCloud
SA_EAST_1
US_EAST_1
US_WEST_1
US_WEST_2
 -->

<entry key="region">US_WEST_2</entry>
</properties>
