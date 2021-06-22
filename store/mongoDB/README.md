# MongoDB Store
This project provides a reference implementation of TIBCO BusinessEvents Custom Store specifically MongoDB which is a document oriented database having rich features as Schema less,Easy to scale,Open source and faster processing and many more.  
Please refer to Configuration Guide of TIBCO BusinessEvents 6.1.0 for more details on Custom Store.
With this implementation, TIBCO BusinessEvents can be configured with MongoDB as a direct or backing store.

## Limitations
- BQL Queries not supported
- This implementation only works with new ID implementation of BusinessEvents Application.
- SSL CDD configuration : Only 'Identity File' type is supported 

## Pre-requisites
- TIBCO BusinessEvents 6.1.0 and above
- MongoDB 4.2.2 (MongoDB Atlas subscription or standalone DB installation)

## How to Build?
Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/store) are followed to setup the new store.

## Getting Started
1. Build and Copy the mongoDB jar to $BE_HOME/lib/ext/tpcl/contrib location.
2. Import your BE project in Studio and open the corresponding CDD.
3. Select MongoDB as a Store Provider
4. Once 'MongoDB' is selected as the store provider, various input fields based on the ones configured in 'store.xml' are available to accept values.
5. Provide configuration details as appropriate.
6. Make sure to select correct protocol for connection URL. For example, if your database is hosted on MongoDB Atlas, check the URL whether it has 'SRV'enabled or not. If SRV is mentioned in your connection URL then select the check box named 'Is SRV Enabled URI' otherwise keep the default settings.
7. Start the BE engine and make sure its connected to database which is mentioned in CDD configuration
