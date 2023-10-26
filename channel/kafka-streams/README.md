# Kafka Streams Channel

The Kafka Streams channel destination can be configured to use stream processors to create stream processing topologies. After the stream topology processes the incoming stream, a stream record is converted to a
SimpleEvent that triggers the relevant rules.

Reference link of sample example is as follows :

* [KafkaStreamsChannel](https://github.com/tibco/be-samples/tree/main/KafkaStreamsChannel)

## Pre-requisites

* To process incoming Kafka Streams records in your TIBCO BusinessEvents application, set up a Kafka Streams channel in your application.

## Getting Started

* Assuming you have gone through all the documentation and appropriate steps are followed to setup the new channel.

* Once 'Kafka Streams' channel is selected via the 'New Channel Wizard', various input fields based on the ones configured in 'drivers.xml' are available on the UI.

* In the Channel editor Channel tab, update the description as desired. The Driver field is set to Kafka Streams(as set in the wizard). The Method of Configuration must be set to Properties.
  
## Kafka Streams Channel Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Kafka Broker URLs|Yes|List of URLs (host and port pairs) that are used for establishing the initial connection to the Kafka cluster. The format of the URL is host1:port1,host:port2,....
Security Protocol|Yes|The security protocol is implemented in the Kafka broker. This protocol must match with security protocol configured in the Kafka broker.
SASL Mechanism|Yes|Type of SASL mechanism is implemented on Kafka broker. This field is active only for SASL_PLAINTEXT and SASL_SSL security protocol.
Configure SSl|N/A|Click the Configure SSL button to open the SSL configuration window and configure SSL details. This button is active only for SSL and SASL_SSL security protocol.

## Kafka Streams Destination Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Topic Name|Yes|Required. Name of the Kafka topic.
Is Regex Pattern|No|A toggle to denote if the Topic Name is a regex pattern
Application ID|Yes|Required. A unique ID for the stream processing application. The same ID must be given to all instances of the application. Each destination must have a unique ID which is used by Kafka Streams to create internal topics.
Key Serde|Yes|Specify the SerDes (Serializer/Deserializer) for the data types of record keys.
Value Serde|Yes|Specify the SerDes (Serializer/Deserializer) for the data types of record	values.
Auto Offset Reset|Yes|Specify the strategy for resetting the offset of the topic in Kafka in case the	current offset does not exist anymore on the server.
Processor Topology|No|Define the processor topology for the stream coming to the destination.	Add the transformations (stream processors) in the same sequence as you	want to process the stream. So, the output of the transformation of a row is input to the row below it. The output of the last row transformation is sent	to a TIBCO BusinessEvents Foreach processor which creates an event by using the provided serializer. The top row transformation is the source processor and the Foreach processor is the sink processor of Kafka Streams. Click Add to view the list of transformations that you can add to your processor topology. Select the transformation and provide input to it as required and click OK. You can provide rule functions as input to the transformations where the rule functions contains the stream processor code for the transformation. The Add Transformation window lists only eligible transformations that can be added in the processor topology based on the existing transformations. For example, if you have added a GroupByKey transformation, the Add Transformation window lists only Aggregate, Count, and Reduce transformations.

The transformation input rulefunction must have a specific signature depending on the input type. The
following table gives a sample signature for each transformation input. The rulefunction browser on
the Add/Edit Transformation window filters the rule function that are applicable to a given input. For
more information on the following transformations, see the [Kafka Streams DSL documentation](https://kafka.apache.org/23/documentation/streams/developer-guide/dsl-api.html).
| Transformation | Required Input | TIBCO BusinessEvents Resource |
|---|---|---|
| Filter,FilterNot | Predicate | boolean rulefunction(Object, Object) |
| MapValues | ValueMapper | Object rulefunction(Object, Object) |
| FlatMapValues | ValueMapperWithKey | Object rulefunction(Object) |
| GroupByKey | None | |
| SelectKey | KeyValueMapper | Object rulefunction(Object, Object) |
| Aggregate | Initializer | Object rulefunction() |
| Aggregate | Aggregator | Object rulefunction(Object, Object, Object) |
| Count | None | |
| Reduce | Reducer | Object rulefunction(Object, Object) |
| ToStream | None | |
| Join | Topic | String Literal or Global Variable(String) |
| LeftJoin | ValueJoiner | Object rulefunction(Object, Object) |
| OuterJoin | JoinWindows(Time Difference, After, Before, Grace) Durations | Positive Number or Global Variable(Integer) |
