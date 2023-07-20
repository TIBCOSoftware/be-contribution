# Channels

All channel contributions that work with TIBCO BusinessEvents 5.6.0 and above. Below is the list of currently available channels,

* [aws-sqs](https://github.com/tibco/be-contribution/tree/main/channel/aws-sqs)
* [kafka-streams](https://github.com/TIBCOSoftware/be-contribution/tree/main/channel/kafka-streams)
* [kinesis](https://github.com/tibco/be-contribution/tree/main/channel/kinesis)
* [mqtt](https://github.com/TIBCOSoftware/be-contribution/tree/main/channel/mqtt)
* [sb](https://github.com/TIBCOSoftware/be-contribution/tree/main/channel/sb)

## Pre-requisites

* Go through the [Custom Channel API](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/api/javadoc/com/tibco/be/custom/channel/package-summary.html) documentation.

* Go through the developers guide [here](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Default.htm#Developers/Custom-Channel-Lifecycle.htm?TocPath=Developers%2520Guide%257CCustom%2520Channel%257C_____1) to get more details around the various classes/interfaces/lifecycle involved and how to set it up.

## Getting Started

* If a new channel jar is needed, follow these [instructions](https://github.com/tibco/be-contribution) to clone/update/build a new jar.

* Follow the [steps](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Default.htm#Developers/Creating-a-New-Custom-BusinessEvents-Channel.htm?TocPath=Developers%2520Guide%257CCustom%2520Channel%257C_____4) outlined in the developer guide to setup and make the new channel available for use and configuration.

* Start BusinessEvents Studio and open the project CDD, right click -> New -> Channel, select and configure the newly added store.
