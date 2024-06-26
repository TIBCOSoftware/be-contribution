# Cassandra Store Catalog Functions

A set of catalog functions supporting:

* [ConnectionInfo](#ConnectionInfo)
* [QueryOptions](#QueryOptions)

Reference link of sample example is as follows :

* [Cassandra Catalog](https://github.com/tibco/be-samples/tree/main/CassandraCatalog)

## Note

This document only list the functions that are specific to Cassandra and the newly introduced one, existing functions that are part of the original Store API are not mentioned here.

## ConnectionInfo

The ConnectionInfo catalog functions allow you to manipulate S3 objects and buckets through the following operations:
* Set user credentials
  * [Store.ConnectionInfo.Cassandra.setUserCredentials](#setUserCredentials)
* Set trust store
  * [Store.ConnectionInfo.Cassandra.setTrustStore](#setTrustStore)
* Set key store
  * [Store.ConnectionInfo.Cassandra.setKeyStore](#setKeyStore)
* Set KeySpace
  * [Store.ConnectionInfo.Cassandra.setKeySpace](#setKeySpace)

# QueryOptions

* Set fetch size
  * [Store.QueryOptions.Cassandra.fetchSize](#fetchSize)
* Set if query is idempotent
  * [Store.QueryOptions.Cassandra.setIdempotent](#setIdempotent)

# Store.ConnectionInfo.Cassandra.setUserCredentials
Purpose: Sets the user credentials if using an authenticated Cassandra setup.

Function Signature:

```java
void setUserCredentials (Object storeConnectionInfo, String userName, String password)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| storeConnectionInfo     | Store Connection Info object                                  |
| userName   | user name to authenticate with                                             |
| password   | password to authenticate with                                              |

Returns:

N/A

# Store.ConnectionInfo.Cassandra.setTrustStore

Purpose: Sets the trust store-related information. The client trusts the secure Cassandra server based on this trust store information.

Function Signature:

```java
void setTrustStore (Object storeConnectionInfo, String trustFilePath, String trustStorePwd, String storeType)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| storeConnectionInfo     | Store Connection Info object  |
| trustFilePath   | Trust file path                                                 |
| trustStorePwd   | Trust file password                                             |
| storeType | store type ( currently we are supporting JKS)                                                         |

Returns:

N/A

# Store.ConnectionInfo.Cassandra.setKeyStore

Purpose: Sets the Keystore store-related information.

Function Signature:

```java
void setKeyStore (Object storeConnectionInfo, String keystorePath, String keystorePwd, String keyStoreType)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| storeConnectionInfo     | Store Connection Info object  |
| keystorePath   | Keystore file path                                                 |
| keystorePwd   | Keystore Pwd                                           |
| keyStoreType | key tore type ( currently we are supporting JKS)                       |

Returns:

N/A

### Store.ConnectionInfo.Cassandra.setKeySpace

Purpose: Sets the keyspace for connection

```java
void setKeySpace (Object storeConnectionInfo, String keySpaceName)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| storeConnectionInfo     | Store Connection Info object  |
| keySpaceName  |keyspace name to connect to                                                                        

Returns:

N/A

### Store.QueryOptions.Cassandra.fetchSize

Purpose: Sets the query fetch size.

```java
void fetchSize (Object queryOptions, int fetchSize)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| queryOptions     | Query option object   |
| fetchSize  |fetchSize|  

Returns:

N/A

### Store.QueryOptions.Cassandra.setIdempotent

Purpose: Sets whether this query is idempotent.

```java
void setIdempotent (Object queryOptions, boolean idempotent)
```
Args:

| Arguments   | Purpose                                                                                             |
|:------------|:----------------------------------------------------------------------------------------------------|
| queryOptions     | Query option object   |
| idempotent  | sets whether this query is idempotent or not|  

Returns:

N/A
