# AWS SQS Channel

`AWS SQS` is a fully managed message queuing service. One can send, store, and receive messages between software components at any volume, without losing messages or requiring other services to be available.

The implementation provides support for injesting, processing incoming SQS messages into events consumed by BusinessEvents and converts events to outgoing SQS messages.

## Pre-requisites

* Have a AWS SQS service up and running and necessary access/secret keys available.

## Getting Started

* Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/channel) are followed to setup the new channel.


## Using the SQS Channel

### Adding a SQS Channel

Add a channel to your project and configure it as follows:

1. At the New Channel Channel Wizard, provide a name and description, and in the <b>Driver</b> field select AWS-SQS. Click <b>Finish</b>.

2. In the Channel editor <b>Channel</b> tab, update the description as desired. The <b>Driver</b> field is set to AWS-SQS (as set in the wizard). The Method of Configuration must be set to Properties.

3. In the [Properties](AWS-SQS Channel Configuration Properties) field, set values for AWS Region, AWS SQS Access Key, AQS SQS Secret Key, and AWS SQS Role ARN


### Adding a Destination

Add a destination to the channel in the usual way.

The [Properties](AWS-SQS Destination Configuration Properties) for destinations, set values for Default Event, Queue URL, Consumer Threads, Poll Interval, and Maximum number message per poll interval.



## AWS-SQS Channel Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
AWS Region|No|AWS Region where SQS resides. For example <b>eu-west-1</b>
AWS SQS Access Key|Yes|Key used in combination with the AWS SQS Secret Key to make programmatic request to AWS. For example, <b>AKIAIOSFODNN7EXAMPLE</b>.<p/><p/>The access key is similar to a user name used in a username-password pair.
AWS SQS Secret Key|Yes|Key used in combination with the access key to make programmatic requests to AWS. For example, <b>wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY</b><p/><p/>The access key is similar to the user name used in a user name-password pair.
AWS SQS Role ARN|Yes|The AWS Role ARN used to access SQS. For example, <b>arn:aws:iam::396113037621:role/TIBCO/BE</b>

## AWS-SQS Destination Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Name|No|Name of the Destination.
Description|No|Description of the destination that is to be created..
Default Event|No|The default event for the destination. You can browse and select an existing event from the project.
Serializer/Deserializer|No|The only serializer available is com.tibco.be.custom.channel.aws.sqs.serializer.SqsTextSerializer
Queue URL|Yes|The AWS SQS Queue URL. For example, https://sqs.eu-west-1.amazonaws.com/396113037621/sqs-test-queue
Consumer Threads|Yes|Number of consumer threads that BusinessEvents creates for the destination. 
Poll Interval|Yes|The polling interval (in secs) to wait for messages on the SQS queue.
Maximum number message per poll interval|Yes|The number of messages to receive per poll interval.