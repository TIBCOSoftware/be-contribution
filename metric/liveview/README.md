# Metric Stores

All metric store contributions that work with TIBCO BusinessEvents 6.1.0 and above. Below is the list of currently available metric stores,

* [Liveview](https://github.com/tibco/be-contribution/tree/main/metric/liveview)

Reference link of sample example is as follows :

* [FraudDetectionLiveView](https://github.com/tibco/be-samples/tree/main/FraudDetectionLiveView)

## Reference Documents

* Go through the [Custom Metric Store API](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/api/javadoc/com/tibco/cep/runtime/appmetrics/package-summary.html) documentation.

* Go through the developer guide [here](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Configuration/Custom-Application-Metrics-Store.htm) to get more details around the various classes/interfaces involved and how to set it up.

## Pre-requisites

* Since Liveview is dependent on SB classpath and as its jars are already removed, a property for SB home is set in pom.xml of Liveview Metric Store which is used to get the required jars (sbclient.jar and lv-client.jar).

## Getting Started

* If a new metric store jar is needed, follow these [instructions](https://github.com/tibco/be-contribution) to clone/update/build a new jar.

* Follow the [steps](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Configuration/Creating-a-Custom-Application-Metrics-Store.htm?tocpath=Configuration%20Guide%7CCluster%20Configurations%20For%20Your%20Project%7CCustom%20Application%20Metrics%20Store%7C_____1) outlined in the developer guide to setup and make the new store available for use and configuration.

* Start BusinessEvents Studio and open the project CDD, goto Cluster -> Application Metric's -> Metric Store and configure the newly added store.

