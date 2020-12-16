# Stores

Covers all persistent store contributions that work with TIBCO BusinessEvents 6.1.0 and above. Below is the list of currently available persistent stores,

* [Redis]()

## Pre-requisites

* Go through the [Custom Store API](https://docs.tibco.com/pub/businessevents-enterprise/6.0.0/doc/html/api/javadoc/index.html)] documentation. [TODO - Link to be updated]

* Go through the developer guide [here]() to get more details around the various classes/interfaces involved and how to set it up. [TODO - Link to be added]

## Getting Started

* Follow the above documents to create add a new store implementation.

* If a new store jar is needed, follow these [instructions](https://github.com/tibco/be-contribution) to clone/update/build a new jar.

* Follow the [steps]() outlined in the developer guide to setup and make the new store available for use and configuration. [TODO - Add appropriate link]

* Start BusinessEvents Studio and open the project CDD, depending on whether you have configured your store as a direct store or a cache based backing store there are couple of options to configure the newly added store.
    - Direct Store -  Goto Cluster -> Object Management -> Store

    - Cache Based Backing store(Assuming its configured as Cluster and Object Management as Cache) - Goto Cluster -> Object Management -> Persistence 
