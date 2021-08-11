# Stores

Covers all persistent store contributions that work with TIBCO BusinessEvents 6.1.0 and above. Below is the list of currently available persistent stores,

* [Redis](https://github.com/TIBCOSoftware/be-contribution/tree/main/store/redis)
* [MongoDB](https://github.com/TIBCOSoftware/be-contribution/tree/main/store/mongoDB)

## Pre-requisites

* Go through the [Custom Store API](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/api/javadoc/com/tibco/cep/store/custom/package-summary.html) documentation. 

* Go through the guide [here](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Default.htm#Configuration/Custom-Backing-Store.htm) to get more details around the various classes/interfaces involved and how to set it up.

## Getting Started

* Follow the above documents to create add a new store implementation.

* If a new store jar is needed, follow these [instructions](https://github.com/tibco/be-contribution) to clone/update/build a new jar.

* Follow the [steps](https://docs.tibco.com/emp/businessevents-enterprise/6.1.0/doc/html/Default.htm#Configuration/Creating-a-Custom-Backing-Store.htm) outlined in the configuration guide to setup and make the new store available for use and configuration.

* Start BusinessEvents Studio and open the project CDD, depending on whether you have configured your store as a direct store or a cache based backing store there are couple of options to configure the newly added store.
    - Direct Store -  Goto Cluster -> Object Management -> Store

    - Cache Based Backing store(Assuming its configured as Cluster and Object Management as Cache) - Goto Cluster -> Object Management -> Persistence 
