# AWS Kinesis Channel

`Amazon Kinesis Data Streams` helps in real-time collection and processing of data records. 

By using the Amazon Kinesis channel, TIBCO BusinessEvents can convert Kinesis data streams to TIBCO BusinessEvents events.

## Pre-requisites

* Install AWS CLI and configure it with access/secret keys.
* Have a AWS Kinesis Data Stream service up and running and necessary access/secret keys available. 


## Getting Started

* Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/channel) are followed to setup the new channel.

* Once 'Kinesis' channel is selected via the 'New Channel Wizard', various input fields based on the ones configured in 'drivers.xml' are available on the UI.

* In the Channel editor <b>Channel</b> tab, update the description as desired. The <b>Driver</b> field is set to Kinesis (as set in the wizard). The Method of Configuration must be set to Properties.

## AWS Kinesis Channel Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Access Key  | Yes | Key used in combination with the Secret Key to make programmatic requests to AWS. For example, AKIAIOSFODNN7EXAMPLE. The access key is similar to a user name used in a username-password pair.
Secret Key  | Yes | Key used in combination with the access key to make programmatic requests to AWS. For example, wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY. The access key is similar to the user name used in a user name-password pair.
Profile Name| Yes | You can store frequently used credentials and configuration settings in files. These files are divided into sections that are referenced by name. These sections are called profiles. Enter the profile name that contains the credentials and configuration settings that you want to use.

## AWS Kinesis Destination Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Name| No |Name of the Destination.
Description| No |Description of the destination that is to be created..
Default Event| No |The default event for the destination. You can browse and select an existing event from the project.
Serializer/Deserializer| No |There are two serializers available. com.tibco.be.custom.channel.kinesis.serializer.KinesisXmlSerializer and com.tibco.be.custom.channel.kinesis.serializer.KinesisJsonSerializer
Stream Name| Yes | 	Name of the stream to which the producer sends data and from which the consumer consumes data. 
Application Name| Yes | 	Name of the Amazon Kinesis Data Stream application.
Note: Each of the applications in the scope of a particular AWS account and Region must have unique names.
Max Records| Yes | The maximum number of records to be returned from the stream when processing the records from the Kinesis consumer.
Region Name| Yes | Name of the region where the Amazon Kinesis data stream application is deployed. For example, us-west-2
Event Property| No | Select an event property that BusinessEvents uses as a partition key for Kinesis.