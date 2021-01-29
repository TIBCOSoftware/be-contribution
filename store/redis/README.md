# RedisStore
This project provides for a reference implementation of TIBCO BusinessEvents Custom Store.
Please refer to Configuration Guide of TIBCO BusinessEvents 6.1.0 for more details on Custom Store.
With this implementation, TIBCO BusinessEvents can be configured with Redis as a direct or backing store.

## Limitations
- BQL Queries not supported
- This implementation only works with new ID implementation of BusinessEvents Application.
- SSL CDD configuration : Only 'Identity File' type is supported 

## Pre-requisites
- TIBCO BusinessEvents 6.1.0 and above
- Redis 6.0.8 with RediSearch 2.0

##How to Build?
Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/store) are followed to setup the new store.

## Getting Started
1. Copy the jar built above to $BE_HOME/lib/ext/tpcl/contrib location.
2. Copy Lettuce and LettuSearch dependencies at $BE_HOME/lib/ext/tpcl
3. Import your BE project in Studio and open the corresponding CDD.
4. Select Redis as a Store Provider
5. Once 'Redis' is selected as the store provider, various input fields based on the ones configured in 'store.xml' are available to accept values.
5. Provide configuration details as appropriate.
6. Due to existing lettuce [issue](https://github.com/RediSearch/lettusearch/issues/33) uber.jar can not be used and the end user needs to handle this manually.
   Update tibco.env.STD_EXT_CP variable in %BE_HOME%/bin/be-engine.tra such a way that lettusearch-2.4.4.jar will be first in order in comparison to lettuce-core-6.0.0.RELEASE.jar.
7. Start the BE engine.
