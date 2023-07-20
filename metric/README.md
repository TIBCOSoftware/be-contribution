# Metric Stores

All metric store contributions that work with TIBCO BusinessEvents 6.1.0 and above. Below is the list of currently available metric stores,

* [Elasticsearch](https://github.com/tibco/be-contribution/tree/main/metric/elasticsearch)
* [Liveview](https://github.com/TIBCOSoftware/be-contribution/tree/main/metric/liveview)

## Reference Documents

* Go through the [Custom Metric Store API](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/api/javadoc/com/tibco/cep/runtime/appmetrics/package-summary.html) documentation.

* Go through the developer guide [here](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Configuration/Custom-Application-Metrics-Store.htm) to get more details around the various classes/interfaces involved and how to set it up.

## Getting Started

* If a new metric store jar is needed, follow these [instructions](https://github.com/tibco/be-contribution) to clone/update/build a new jar.

* Follow the [steps](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Configuration/Creating-a-Custom-Application-Metrics-Store.htm?tocpath=Configuration%20Guide%7CCluster%20Configurations%20For%20Your%20Project%7CCustom%20Application%20Metrics%20Store%7C_____1) outlined in the developer guide to setup and make the new store available for use and configuration.

* Start BusinessEvents Studio and open the project CDD, goto Cluster -> Application Metric's -> Metric Store and configure the newly added store.

