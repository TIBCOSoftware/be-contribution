# Pattern Catalog Functions

A set of catalog functions supporting:

* [IO](#io)
* [Manager](#manager)
* [Service](#service)

Reference link of sample example is as follows :

* [PatternMatcher](https://github.com/tibco/be-samples/tree/main/PatternMatcher)

## IO

The IO catalog functions allow you to manipulate IO through the following operations:

* Default Destination
  * [IO.toDestination](#iotodestination)
* Pattern Service
  * [IO.toPattern](#iotopattern)

### IO.toDestination

Purpose: Sends the event to its default destination.

Function Signature:

```java
void toDestination(SimpleEvent event)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| event        | The event that is to be sent to its default Destination.                 |

Returns:

N/A

### IO.toPattern

Purpose: Sends the event to the Pattern Service which will in turn route it to all interested Pattern instances. Once the instances (if any) have processed this event, the reference\nto it will be discarded by the Pattern Service.

Function Signature:

```java
void toPattern(SimpleEvent event)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| event        | The event that is to be sent to its default Destination.                 |

Returns:

N/A
  
## Manager

The Manager catalog functions allow you to manipulate Manager through the following operations:

* Deploy Pattern Instance
  * [Manager.deploy](#managerdeploy)
* Get Deployed
  * [Manager.getDeployed](#managergetdeployed)
* Get registered
  * [Manager.getRegistered](#managergetregistered)
* Instantiate
  * [Manager.instantiate](#managerinstantiate)
* Register
  * [Manager.register](#managerregister)
* Set Closure
  * [Manager.setClosure](#managersetclosure)
* Set Completion Listener
  * [Manager.setCompletionListener](#managersetcompletionlistener)
* Set Failure Listener
  * [Manager.setFailureListener](#managersetfailurelistener)
* Set Parameter Date Time
  * [Manager.setParameterDateTime](#managersetparameterdatetime)
* Set Parameter Double
  * [Manager.setParameterDouble](#managersetparameterdoube)
* Set Parameter Int
  * [Manager.setParameterInt](#managersetparameterint)
* Set Parameter Long
  * [Manager.setParameterLong](#managersetparameterlong)
* Set Parameter String
  * [Manager.setParameterString](#managersetparameterstring)
* Undeploy
  * [Manager.undeploy](#managerundeploy)
* Unregister
  * [Manager.unregister](#managerunregister)
 
## Manager.Advanced

* Get Event ExtIds
  * [Manager.Advanced.getEventExtIds](#manageradvancedgeteventextids)
* Get Event Ids
  * [Manager.Advanced.getEventIds](#manageradvancedgeteventids)
* Get Recent Event ExtId
  * [Manager.Advanced.getRecentEventExtId](#manageradvancedgetrecenteventextid)
* Get Recent Event Id
  * [Manager.Advanced.getRecentEventId](#manageradvancedgetrecenteventid)
 
### Manager.deploy

Purpose: Deploys the pattern instance that has all the necessary parameters set. The instance will be deployed under the name provided. This name has to be unique.

Function Signature:

```java
void deploy(Object patternInstance, String patternInstanceName)
```
Args:

| Arguments           | Purpose                                                                   |
|:-------------       |:------------------------------------------------------------------------- |
| patternInstance     | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| patternInstanceName | The name for the instance being deployed.                                 |

Returns:

N/A

### Manager.getDeployed

Purpose: Returns the pattern instance names that are deployed under the given URI.

Function Signature:

```java
String[] getDeployed(String patternDefURI)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| patternDefURI | URI of the registered pattern.                                           |

Returns:

| Type    | Description                                                           |
|:------- |:-----------------------                                               |
| String[]| Array of instance names of the patterns deployed under the given URI. |

### Manager.getRegistered

Purpose: Returns the URIs of all the registered patterns.

Function Signature:

```java
String[] getRegistered()
```

Returns:

| Type    | Description                                                  |
|:------- |:-----------------------                                      |
| String[]| Array of registered pattern URIs.                            |

### Manager.instantiate

Purpose: Creates an instance of the pattern registered under the given URI for deployment. This instance should be configured before deployment.

Function Signature:

```java
Object instantiate(String patternDefURI)
```

Returns:

| Type    | Description                                                  |
|:------- |:-----------------------                                      |
| Object  | The pattern instance. This should be treated as an           |
|         | opaque object meant for configuring a pattern\ninstance.     |

### Manager.register

Purpose: Registers the pattern definition text under the URI (define pattern XYZ ...) provided in the text. The URI (XYZ) that was extracted is also returned by the method\nfor reference. The URI has to be unique for the registration to succeed.

Function Signature:

```java
String register(String patternLangString)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------    |:---------------------------------------------------------------------|
| patternLangString | The syntactically valid pattern definition string.                   |

Returns:

| Type    | Description                                                                           |
|:------- |:-----------------------                                                               |
| String  | The URI of the pattern that was parsed. The URI is obtained after parsing the string. |

### Manager.setClosure

Purpose: Sets a $1closure$1 object that will be delivered in the listener function call to help identify this pattern instance. The value is set only for the given pattern instance.Function Signature:

Function Signature:

```java
void setClosure(Object patternInstance, Object closure)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| closure         | The Pattern Service will not interpret this object. It can be of any type.|

Returns:

N/A

### Manager.setCompletionListener

Purpose: Sets the given rule function URI as the listenet that will be invoked by the Pattern Service when/if this pattern instance observes the specified event sequence.\nThe value is set only for the given pattern instance.

Function Signature:

```java
void setCompletionListener(Object patternInstance, String ruleFunctionURI)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| ruleFunctionURI | has obeserved so far.                                                     |

Returns:

N/A

### Manager.setFailureListener

Purpose: Sets the given rule function URI as the listener that will be invoked by the Pattern Service when/if this pattern instance fails due to a wrong event sequence.\nThe value is set only for the given patten instance.

Function Signature:

```java
void setFailureListener(Object patternInstance, String ruleFunctionURI)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| ruleFunctionURI | has obeserved so far.                                                     |

Returns:

N/A

### Manager.setParameterDateTime

Purpose: If the pattern string has a bind parameter that expects a DateTime object (java.util.Calendar), then its value has to be set using this method.\nThe value is set only for the given pattern instance.

Function Signature:

```java
void setParameterDateTime(Object patternInstance, String parameterName, DateTime value)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| parameterName   | Name of the bind parameter whose value is being set.                      |
| value           | Value of the bind parameter                                               |

Returns:

N/A

### Manager.setParameterDouble

Purpose: If the pattern string has bind parameter that expects a double, then its value has to be set using this method.\nThis value if set only for the given pattern instance.

Function Signature:

```java
void setParameterDouble(Object patternInstance, String parameterName, double value)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| parameterName   | Name of the bind parameter whose value is being set.                      |
| value           | Value of the bind parameter                                               |

Returns:

N/A

### Manager.setParameterInt

Purpose: If the pattern string has a bind paramater that expects an integer, then its value has to be set using this method.\nThe value is set only for a given pattern instance.

Function Signature:

```java
void setParameterInt(Object patternInstance, String parameterName, int value)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| parameterName   | Name of the bind parameter whose value is being set.                      |
| value           | Value of the bind parameter                                               |

Returns:

N/A

### Manager.setParameterLong

Purpose: If the pattern string has a bind parameter that expects a long, then its value has to be set using this method.\nThe value is set only for the given pattern instance.

Function Signature:

```java
void setParameterLong(Object patternInstance, String parameterName, long value)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| parameterName   | Name of the bind parameter whose value is being set.                      |
| value           | Value of the bind parameter                                               |

Returns:

N/A

### Manager.setParameterString

Purpose: If the pattern string has a bind parameter that expects a String, then its value has to be set using this method.\nThe value is et only for the given pattern instance.

Function Signature:

```java
void setParameterString(Object patternInstance, String parameterName, String value)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternInstance | The return value (opaque) from the pattern $1instantiate(String)$1 method.|
| parameterName   | Name of the bind parameter whose value is being set.                      |
| value           | Value of the bind parameter                                               |

Returns:

N/A

### Manager.undeploy

Purpose: Undeploys the pattern instance that was deployed under the name provided.

Function Signature:

```java
void undeploy(String patternInstanceName)
```
Args:

| Arguments           | Purpose                                                               |
|:--------------      |:--------------------------------------------------------------------- |
| patternInstanceName | The name of the pattern instance that is currently deployed.          |

Returns:

N/A

### Manager.unregister

Purpose: Unregisters the Pattern that was previously registered under the given URI.

Function Signature:

```java
void unregister(String patternDefURI)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| patternDefURI   | URI of the pattern that is currently registered.                          |

Returns:

N/A

### Manager.Advanced.getEventExtIds

Purpose: Returns an array of Event ExtIds that have been observed by the pattern instance so far - i.e that\nones that did not cause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter\nprovided by the Pattern Service when it invokes the listener rule function. An empty array is returned if there\nwere no such Event ExtIds. If the Event did not have an ExtId, then a <code>null</code> will be stored in its\nplace.

Function Signature:

```java
String[] getEventExtIds(Object opaque)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| advancedOpaque  | Service when it invoked the callback RuleFunction.                        |

Returns:

| Type    | Description                                                  |
|:------- |:-----------------------                                      |
| String[]| An empty array if there was nothing.                         |

### Manager.Advanced.getEventIds

Purpose: Returns an array of Event Ids that have been observed by the pattern instance so far - i.e that ones\nthat did not cause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter\nprovided by the Pattern Service when it invokes the listener rule function. An empty array is returned if there\nwere no such Event Ids.

Function Signature:

```java
Object[] getEventIds(Object opaque)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| advancedOpaque  | Service when it invoked the callback RuleFunction.                        |

Returns:

| Type    | Description                                                  |
|:------- |:-----------------------                                      |
| Object[]| An empty array if there was nothing.                         |

### Manager.Advanced.getRecentEventExtId

Purpose: Returns the most Event ExtId observed by the pattern instance - i.e that most recent one that did not\ncause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter provided by the\nPattern Service when it invokes the listener rule function. $1<code>null</code>$1 is returned if there was no such\nEvent ExtId or if the Event did not have one.

Function Signature:

```java
String getRecentEventExtId(Object opaque)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| advancedOpaque  | Service when it invoked the callback RuleFunction.                        |

Returns:

| Type    | Description                                                  |
|:------- |:-----------------------                                      |
| String  | <code>null</code> if there was nothing.                      |

### Manager.Advanced.getRecentEventId

Purpose: Returns the most Event Id observed by the pattern instance - i.e that most recent one that did not\ncause the instance to fail. The only parameter this function takes is the $1opaque$1 parameter provided by the\nPattern Service when it invokes the listener rule function. $1-1$1 is returned if there was no such Event Id.

Function Signature:

```java
Object getRecentEventId(Object opaque)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| advancedOpaque  | Service when it invoked the callback RuleFunction.                        |

Returns:

| Type    | Description                                                  |
|:------- |:-----------------------                                      |
| Object  | -1 if there was nothing.                                     |

---

## Service

The Service catalog functions allow you to manipulate Service through the following operations:

* Start Service
  * [Service.start](#servicestart)
* Stop Service
  * [Service.stop](#servicestop)
 
## Service.Advanced

* Get Maximum Executor Threads
  * [Service.Advanced.getMaxExecutorThreads](#serviceadvancedgetmaxexecutorthreads)
* Get Maximum Scheduler Threads
  * [Service.Advanced.getMaxSchedulerThreads](#serviceadvancedgetmaxschedulerthreads)
* Set Maximum Executor Threads
  * [Service.Advanced.setMaxExecutorThreads](#serviceadvancedsetmaxexecutorthreads)
* Set Maximum Scheduler Threads
  * [Service.Advanced.setMaxSchedulerThreads](#serviceadvancedsetmaxschedulerthreads)
 
### Service.start

Purpose: Starts the Pattern Matcher service.

Function Signature:

```java
void start()
```

Returns:

N/A

### Service.stop

Purpose: Stops the Pattern Matcher service.

Function Signature:

```java
void stop()
```

Returns:

N/A

### Service.Advanced.getMaxExecutorThreads

Purpose: Returns the maximum number of pooled Threads that the Service can create and use for pattern processing.

Function Signature:

```java
int getMaxExecutorThreads()
```

Returns:

| Type    | Description                                                            |
|:------- |:-----------------------                                                |
| int     | The maximum number of Threads that will be used by the Pattern Service.|

### Service.Advanced.getMaxSchedulerThreads

Purpose: Returns the maximum number of pooled Threads that the Service can create and use for scheduling time based tasks during pattern processing.

Function Signature:

```java
int getMaxSchedulerThreads()
```

Returns:

| Type    | Description                                                            |
|:------- |:-----------------------                                                |
| int     | The maximum number of Threads that will be used by the Pattern Service.|

### Service.Advanced.setMaxExecutorThreads

Purpose: Sets the maximum number of pooled Threads that the Service can create and use for pattern processing.

Function Signature:

```java
void setMaxExecutorThreads(int numThreads)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| numThreads      | The maximum number of Threads to be used by the Pattern Service.          |

Returns:

N/A

### Service.Advanced.setMaxSchedulerThreads

Purpose: Sets the maximum number of pooled Threads that the Service can create and use for scheduling time based tasks during pattern processing.

Function Signature:

```java
void setMaxSchedulerThreads(int numThreads)
```
Args:

| Arguments       | Purpose                                                                   |
|:--------------  |:------------------------------------------------------------------------- |
| numThreads      | temporal/time based patterns.                                             |

Returns:

N/A
