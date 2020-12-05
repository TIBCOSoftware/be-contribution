# Elasticsearch Metric Store

`Elasticsearch` is an open source distributed, search and analytics engine built on top of Apache Lucene.
This implementation publishes entity data to Elasticsearch on RTC completion. 

## Getting Started

Assuming you have gone through all the documentation and appropriate [steps]() are followed to setup the new metric store.

* Once 'Elasticsearch' is selected as the metric provider, various input fields based on the ones configured in 'metric-store.xml' are available to accept values. 

* You can use any one of the multiple auth/no auth options to connect to the elasticsearch server,
	- No Auth
	- Auth with User Name and Password
	- Auth with Token Service
	- Auth with API keys

* Start Elasticsearch server. Follow any of the options outlined [here](https://www.elastic.co/guide/en/elasticsearch/reference/current/starting-elasticsearch.html).

* Start the BE engine and validate if the configured entities are pushed into elasticsearch. 

* Once the data is published and available in Elastic, it can be viewed via any Elastic compatible visualization/dashboard tools. Most common visualization tool used with Elastic is [Kibana](https://www.elastic.co/kibana), but you can others as well like [Grafana](https://grafana.com/docs/grafana/latest/datasources/elasticsearch/).




 




