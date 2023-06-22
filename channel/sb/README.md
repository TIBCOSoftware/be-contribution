# StreamBase Channel

Using the StreamBase channel, you can send messages back and forth between TIBCO StreamBase and TIBCO BusinessEvents. For example, you can use StreamBase for aggregation and filtering data, and then use BusinessEvents to process context-based rules.

A BusinessEvents application can read from (dequeue) and write to (enqueue) any StreamBase stream using the StreamBase channel. Thus, an existing StreamBase application requires no modification to exchange messages with a BusinessEvents application. The configurations for communicating with the StreamBase application is performed in the BusinessEvents application.

## Pre-requisites

* The StreamBase Channel in BusinessEvents requires a Simple Event definition in the BusinessEvents Project to match the corresponding schema definition in the StreamBase application.

## Getting Started

* Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/channel) are followed to setup the new channel.

* Once 'SB' channel is selected via the 'New Channel Wizard', various input fields based on the ones configured in 'drivers.xml' are available on the UI.

* In the Channel editor Channel tab, update the description as desired. The Driver field is set to SB(as set in the wizard). The Method of Configuration must be set to Properties.
  
## StreamBase Channel Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
StreamBase Server URI|Yes|The URI at which TIBCO BusinessEvents can contact the StreamBase server.	Example: sb://localhost:10000/
UserName|Yes|A valid user name for the StreamBase server. This field is required only when the basic authentication is enabled at the StreamBase server.
Password|Yes|The password assigned to the user name specified in UserName, for accessing the StreamBase server. This field is required only when the basic authentication is enabled	at the StreamBase server.
SSL Trust Store File|Yes|Location of the truststore file.
SSL Trust Store Password|Yes|Password to access the truststore file.	
SSL Key Password|Yes|Password to access the key in the keystore file.

## StreamBase Destination Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Stream Name|No|The name of the input or output stream from the StreamBase application
Client Type|No|Specifies whether the connection is used as an input (dequeue) client or an output (enqueue) client.
Filter Predicate|No|A StreamBase expression that is used to filter the incoming messages. For instance, BidPrice > 100.	This field is active only if Client Type is Dequeuer.
Enable Buffering|No|Specifies whether to activate buffering for an enqueue client. Activating buffering can improve performance when there are a large number of enqueue operations. This field is active only if Client Type is Enqueuer.
Buffer Size|No|The number of tuples to buffer before the enqueue operation. This field is active only if Enable Buffering is selected.
Flush Interval(ms)|No|Time interval (in milliseconds) to wait before flushing the enqueue buffer. This field is active only if Enable Buffering is selected.
