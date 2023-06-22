# MQTT Channel

MQTT is a machine-to-machine connectivity protocol that enables remote connections for IoT applications. By using the MQTT channel, TIBCO BusinessEvents can receive MQTT messages and transform them into TIBCO BusinessEvents events.

## Getting Started

* Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/channel) are followed to setup the new channel.

* Once 'MQTT' channel is selected via the 'New Channel Wizard', various input fields based on the ones configured in 'drivers.xml' are available on the UI.

* In the Channel editor Channel tab, update the description as desired. The Driver field is set to MQTT(as set in the wizard). The Method of Configuration must be set to Properties.

## MQTT Channel Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
MQTT Broker URLs|Yes|Address of the broker that the clients connect to.
UserName|Yes|Used as the client user name for the connection.
Password|Yes|Used as the client password for the connection.


## MQTT Destination Configuration Properties

| Field | Global Var? | Description |
|---|---|---|
Publish Topic Name|Yes|Topic name to which the publisher delivers the message.
Subscribe Topic Name|Yes|List (as comma-separated values) all topic names to which the client can subscribe.
Client ID|Yes|The client identifier is used by the server to store data related to the client. Hence, use the correct client ID when connecting to the server for reliable messaging and durable subscriptions.
Async Client|Yes|The client used to communicate with the MQTT broker. The Async Client enables clients to initiate non-blocking MQTT actions that run in the background thread.
Clean Session|Yes|Specifies whether the client and the server should remember their states when the client, the server, or the connection restarts.
MaxInflight|Yes|The number of QoS 1 or QoS 2 messages that are in the process of being transmitted simultaneously. Default value: 10 messages
Quality of Service(QoS)|Yes|The agreement between the client and the server regarding the guarantee that a message delivers.
Retain|Yes|Determines if a broker stores the last retained message and the corresponding QoS for that message's topic. A retained message is like a normal MQTT message with the retain flag set to true. When a client subscribes to a topic that has a retained message, it immediately receives the retained message after subscribing. The broker stores only one retained message per topic.
Will and Testament|Yes|Determines whether the Last Will and Testament feature is enabled.	The Last Will and Testament feature is used to notify other clients of an	abruptly disconnected client.	If set to true, each client can specify its last will message while connecting to the broker. If the client then disconnects abruptly, the broker sends the	message to all subscribed clients on the topic that is specified in the message.
Disconnect on Suspend|Yes|Determines whether a destination disconnects on suspension.	If set to false, when a destination disconnects and unsubscribes to the	subscribed topics. No stored messages deliver after the destination	resumes. If set to true, a suspension disconnects the MQTT connection and stored	messages deliver when the destination resumes.
