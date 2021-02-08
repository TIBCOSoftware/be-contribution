# AWS SQS Channel

`AWS SQS` is a fully managed message queuing service. One can send, store, and receive messages between software components at any volume, without losing messages or requiring other services to be available.

The implementation provides support for injesting, processing incoming SQS messages into events consumed by BusinessEvents and converts events to outgoing SQS messages.

## Pre-requisites

* Install docker, since the bundled integration test's creates a light weight throw away container for all the tests. Existing tests were run again Docker v19.03.13.
* Have a AWS SQS service up and running and necessary access/secret keys available. 

Note - For integration tests, we use an embedded localstack instance that mocks/simulates the AWS SQS and other services.

## Getting Started

* Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/channel) are followed to setup the new channel.

* Once 'AWS-SQS' channel is selected via the 'New Channel Wizard', various input fields based on the ones configured in 'drivers.xml' are available on the UI.

* In the Channel editor <b>Channel</b> tab, update the description as desired. The <b>Driver</b> field is set to AWS-SQS (as set in the wizard). The Method of Configuration must be set to Properties.

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