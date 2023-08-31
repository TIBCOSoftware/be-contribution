# Analytics Catalog Functions

A set of catalog functions supporting:

* [PMML](#pmml)
* [Statistica](#statistica)
* [TERR](#terr)

Reference links of sample examples are as follows :

* [AuditPMML](https://github.com/tibco/be-samples/tree/main/AuditPMML)
* [FraudDetectionTerr](https://github.com/tibco/be-samples/tree/main/FraudDetectionTerr)
* [LiveScore](https://github.com/tibco/be-samples/tree/main/LiveScore)

## PMML

The PMML catalog functions allow you to manipulate PMML through the following operations:
* Eval Model With Concept
  * [PMML.evalModelWithConcept](#evalmodelwithconcept)
* Eval Model With Event
  * [PMML.evalModelWithEvent](#evalModelWithEvent)
* Eval Model With Parameters
  * [PMML.evalModelWithParams](#evalModelWithParams)
* Get Input Field
  * [PMML.getInputFields](#getInputFields)
* Get Output Field
  * [PMML.getOutputFields](#getOutputFields)
* Get Target Field
  * [PMML.getTargetFields](#getTargetFields)
* Load Model
  * [PMML.loadModel](#loadModel)
* Model Exists
  * [PMML.modelExists](#modelExists)
* Remove Model 
  * [PMML.removeModel](#removeModel)
  
### PMML.evalModelWithConcept

Purpose: Calls the PMML engine with the model name and Concept, and then returns the result.

Function Signature:

```java
Object evalModelWithConcept(String modelName, Concept concept)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |
| concept      | A Concept                                                                |

Returns:

Object

### PMML.evalModelWithEvent

Purpose: Calls the PMML engine with the model name and Event, and then returns the result.

Function Signature:

```java
Object evalModelwithEvent(String modelName, SimpleEvent event)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |
| event        | A SimpleEvent                                                            |

Returns:

Object

### PMML.evalModelWithParams

Purpose: Calls the PMML engine with the model name and parameters, and then returns the result.

Function Signature:

```java
Object evalModelwithParams(String modelName, Object... parameters)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |
| parameters   | Parameters of the model.For example, if a model has an input field Age,  |
|              | then the parameters to this function would be Age,20.                    |

Returns:

Object

### PMML.getInputFields

Purpose: Returns the names of input fields of PMML model as a list

Function Signature:

```java
Object getInputFields(String modelName)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |

Returns:

Object

### PMML.getOutputFields

Purpose: Returns the names of output fields of PMML model as a list

Function Signature:

```java
Object getOutputFields(String modelName)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |

Returns:

Object

### PMML.getTargetFields

Purpose: Returns the names of target fields of PMML model as a list

Function Signature:

```java
Object getTargetFields(String modelName)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |

Returns:

Object

### PMML.loadModel

Purpose: Loads the PMML model from a file (Users have to specify the model name). Throws an exception if input file does not exist or model is not valid

Function Signature:

```java
void loadModel(String modelName, String filePath)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |
| filePath     | Location of Pmml model                                                   |

Returns:

N/A

### PMML.modelExists

Purpose: Tests if the specified model exists in the PMML engine.

Function Signature:

```java
boolean modelExists(String modelName)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |

Returns:

Boolean

### PMML.removeModel

Purpose: Removes the specified model from PMML engine.

Function Signature:

```java
void removeModel(String modelName)
```
Args:

| Arguments    | Purpose                                                                  |
|:-------------|:-------------------------------------------------------------------------|
| modelName    | Name of the model                                                        |

Returns:

N/A


---

## Statistica

The Statistica catalog functions allow you to manipulate Statistica through the following operations:

* Get Score With Concept
  * [Statistica.getScoreWithConcept](#getscorewithconcept)
* Get Score With Event
  * [Statistica.getScoreWithEvent](#getscorewithevent)
* Get Score With Parameters
  * [Statistica.getScoreWithParams](#getscorewithparams)

## Statistica.ConnectionInfo

* Create Connection
  * [Statistica.ConnectionInfo.createConnection](#createconnection)
* Set Basic Authentication
  * [Statistica.ConnectionInfo.setBasicAuth](#setbasicauth)
 
### Statistica.getScoreWithConcept

Purpose: Calls Statistica with the the provided scriptName and Concept through a SOAP request, and then returns the result.

Function Signature:

```java
Object getScorewithConcept(Object connectionInfo, String scriptName, Concept cept)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| connectionInfo| ConnectionInfo object returned using createConnection                    |
| scriptName    | Script Name of Statistica to be called                                   |
| concept       | A Concept                                                                |

Returns:

Object

### Statistica.getScoreWithEvent

Purpose: Calls Statistica with the the provided scriptName and Event through a SOAP request, and then returns the result.

Function Signature:

```java
Object getScoreWithEvent(Object conn, String scriptName, SimpleEvent event)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| conn          | ConnectionInfo object returned using createConnection                    |
| scriptName    | Script Name of Statistica to be called                                   |
| event         | A SimpleEvent                                                            |

Returns:

Object

### Statistica.getScoreWithParams

Purpose: Calls the Statistica with the the provided scriptName through a SOAP request and returns the result.

Function Signature:

```java
Object getScorewithParams(Object connectionInfo, String scriptName, Map<String, String> parameters)
```
Args:

| Arguments      | Purpose                                                                  |
|:-------------- |:-------------------------------------------------------------------------|
| connectionInfo | ConnectionInfo object returned using createConnection                    |
| scriptName     | Script Name of Statistica to be called                                   |
| parameters     | Map of parameters to send in the request                                 |

Returns:

Object

### Statistica.ConnectionInfo.createConnection

Purpose: Sets the connection information for Host URL  to connect to the server.

Function Signature:

```java
Object createConnection(String hostUrl)
```
Args:

| Arguments      | Purpose                                                                  |
|:-------------- |:-------------------------------------------------------------------------|
| hostUrl        | URL of the Statistica Server                                             |

Returns:

Object

### Statistica.ConnectionInfo.setBasicAuth

Purpose: Sets the credential information like User name and Password for Basic Authorization for Statistica for it to connect to the server.

Function Signature:

```java
Object setBasicAuth(Object connection, String userName, String password)
```
Args:

| Arguments      | Purpose                                                                  |
|:-------------- |:-------------------------------------------------------------------------|
| connectionInfo | ConnectionInfo object returned using createConnection                    |
| userName       | User name to connect to the Statistica Server using Basic Auth           |
| password       | Password to connect to the Statistica Server using Basic Auth            |

Returns:

Object

---

## TERR

The TERR catalog functions allow you to manipulate TERR through the following operations:

## TERR.DataFrame

* Create Data Frame
  * [TERR.DataFrame.createFrame](#createframe)
* Print Data Frame
  * [TERR.DataFrame.debugOut](#debugout)
* Get Column
  * [TERR.DataFrame.getColumn](#getcolumn)
* Get Column Count
  * [TERR.DataFrame.getColumnCount](#getcolumncount)
* Get Column Type
  * [TERR.DataFrame.getColumnType](#getcolumntype)
 
## TERR.DataList

* Concept To Data List
  * [TERR.DataList.conceptToDataList](#concepttodatalist)
* Create Data List
  * [TERR.DataList.createList](#createlist)
* Debug Out
  * [TERR.DataList.debugOut](#debugout)
* Get Element
  * [TERR.DataList.getElement](#getelement)
* Get Element Count
  * [TERR.DataList.getElementCount](#getelementcount)
* Get Element Type
  * [TERR.DataList.getElementType](#getelementtype)

## TERR.Engine

* Create Engine
  * [TERR.Engine.createEngine](#enginecreateengine)
* Create Engine Pool
  * [TERR.Engine.createEnginePool](#createenginepool)
* Delete Engine
  * [TERR.Engine.deleteEngine](#deleteengine)
* Delete Variable
  * [TERR.Engine.deleteVariable](#enginedeletevariable)
* Engine Execute
  * [TERR.Engine.engineExecute](#engineexecute)
* Engine Exists
  * [TERR.Engine.engineExists](#engineexists)
* Get Last Error Message
  * [TERR.Engine.getLastErrorMessage](#enginegetlasterrormessage)
* Get Variable
  * [TERR.Engine.getVariable](#enginegetvariable)
* Interrupt
  * [TERR.Engine.interrupt](#engineinterrupt)
* Invoke TERR Function
  * [TERR.Engine.invokeTERRFunction](#engineinvoketerrfunction)
* Is Engine Running
  * [TERR.Engine.isEngineRunning](#isenginerunning)
* Set Engine Parameters
  * [TERR.Engine.setEngineParameters](#setengineparameters)
* Set Java Home
  * [TERR.Engine.setJavaHome](#setjavahome)
* Set Java Options
  * [TERR.Engine.setJavaOptions](#setjavaoptions)
* Set TERR Home
  * [TERR.Engine.setTerrHome](#setterrhome)
* Set Variable
  * [TERR.Engine.setVariable](#enginesetvariable)
* Start Engine
  * [TERR.Engine.startEngine](#startengine)
* Stop Engine
  * [TERR.Engine.stopEngine](#stopengine)
 
### TERR.DataFrame.createFrame

Purpose: Creates and returns a new terr frame object.

Function Signature:

```java
Object createFrame(String [] names, Object [] data)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| names         | Array of column names                                                    |
| data          | Array of column names                                                    |

Returns:

Object

### TERR.DataFrame.debugOut

Purpose: Prints the data frame

Function Signature:

```java
void debugOut(Object frame)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| frame         | Terr Frame Object                                                        |

Returns:

N/A

### TERR.DataFrame.getColumn

Purpose: Returns the column specified by the column number from the data frame object. If the column number specified is incorrect returns null value.

Function Signature:

```java
Object[] getColumn(Object dataFrame, int columnIndex)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| dataFrame     | A TerrDataFrame type of Object                                           |
| columnIndex   | The column number to be retrieved from the data frame                    |

Returns:

Object

### TERR.DataFrame.getColumn

Purpose: Returns the column specified by the column number from the data frame object. If the column number specified is incorrect returns null value.

Function Signature:

```java
Object[] getColumn(Object dataFrame, int columnIndex)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| dataFrame     | A TerrDataFrame type of Object                                           |
| columnIndex   | The column number to be retrieved from the data frame                    |

Returns:

Object

### TERR.DataFrame.getColumnCount

Purpose: Returns the total number of columns present in the passed dataframe object

Function Signature:

```java
int getColumnCount(Object dataFrame)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| dataFrame     | A TerrDataFrame type of Object                                           |

Returns:

| Type   | Description                                                  |
|:-------|:-----------------------                                      |
| int    | Number of columns present in the passed TerrDataFrame Object |

### TERR.DataFrame.getColumnType

Purpose: Returns the column type for the column number specified from the data frame object.<br/>If the column number specified is incorrect returns null value.

Function Signature:

```java
String getColumnType(Object dataFrame, int columnNumber)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| dataFrame     | A TerrDataFrame type of Object                                           |
| columnIndex   | The column number whose type needs to retrieved from the data frame      |

Returns:

| Type   | Description                                                                        |
|:-------|:-----------------------                                                            |
| String | Returns the column type for the column number specified from the data frame object.|
|        |  If the column number specified is incorrect returns null value.                   | 

### TERR.DataList.conceptToDataList

Purpose: Converts the concept to a new terr list object.

Function Signature:

```java
Object conceptToDataList(Concept concept)
```
Args:

| Arguments     | Purpose                                                                  |
|:--------------|:-------------------------------------------------------------------------|
| concept       | Concept                                                                  |

Returns:

Object

### TERR.DataList.createList

Purpose: Creates and returns a new terr list object.

Function Signature:

```java
Object createList(String [] names, Object [] data)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| names         | Array of column names. If an empty array is provided can not create a List| 
|               | and a null value will be returned.                                        |
| data          | Data array                                                                |

Returns:

| Type   | Description                                                  |
|:-------|:-----------------------                                      |
| Object | If the names is empty, then a null Object is returned back.  |

### TERR.DataList.debugOut

Purpose: Prints the data list

Function Signature:

```java
void debugOut(Object list)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| list          | Terr List Object                                                          | 

Returns:

N/A

### TERR.DataList.getElement

Purpose: Returns the Element at the index mentioned from the passed DataList Object.

Function Signature:

```java
Object[] getElement(Object dataList, int elementIndex)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| dataList      | The DataList Object.                                                      | 
| elementIndex  | The index of the element to be fetched from the passed DataList Object.   |

Returns:

| Type     | Description                                                  |
|:-------  |:-----------------------                                      |
| Object[] | Object array of the Element present at the provided index.   |

### TERR.DataList.getElementCount

Purpose: Returns the total number of elements present in the TERR DataList Object passed.

Function Signature:

```java
int getElementCount(Object dataList)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| dataList      | The DataList Object.                                                      | 

Returns:

| Type     | Description                                                  |
|:-------  |:-----------------------                                      |
| int      | Count of the number of elements in DataList                  |

### TERR.DataList.getElementType

Purpose: Returns the datatype of the element present at the index mentioned in the passed DataList Object.

Function Signature:

```java
String getElementType(Object dataList, int elementIndex)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| dataList      | The DataList Object.                                                      | 
| elementIndex  | The index of the element to be fetched from the passed DataList Object.   |

Returns:

| Type     | Description                                                  |
|:-------  |:-----------------------                                      |
| String   | Returns the datatype of the element present at the index     |
|          | mentioned in the passed DataList Object.                     |

### TERR.Engine.createEngine

Purpose: Creates an instance of TERR Engine and returns true if successful.

Function Signature:

```java
boolean createEngine(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine Name                                                               | 

Returns:

Boolean

### TERR.Engine.createEnginePool

Purpose: Create a pool of TERR Engines with given size and name. This pool will be shared among the BE threads

Function Signature:

```java
boolean createEnginePool(int poolSize, String poolName)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| poolSize      | Number of engines to create                                               | 
| poolName      | Name of the engine pool                                                   |

Returns:

Boolean

### TERR.Engine.deleteEngine

Purpose: Deletes instance of TERR Engine or Engine pool. If engine pool name is passed as a parameter all the engines will be deleted in the engine pool

Function Signature:

```java
void deleteEngine(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

N/A

### TERR.Engine.deleteVariable

Purpose: Deletes the variable from TERR Engine; Throws an exception if it fails. Ignores (warning message logged at debug level) if the variable does not exist. If engine pool name is passed as a parameter, it will delete the variable from all engines in the pool

Function Signature:

```java
void deleteVariable(String engine, String var)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| var           | Variable Name                                                             |

Returns:

N/A

### TERR.Engine.engineExecute

Purpose: Parse and evaluate an expression in the given TERR engine.If engine pool name is passed as a parameter, the expression will be evaluated in all the engines in the engine pool

Function Signature:

```java
void engineExecute(String engine, String Rscript, boolean interactive)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| RScript       | R Script to be evaluated                                                  |
| interactive   | If the argument interactive is true, the expression is evaluated as if it |
|               | was in an interactive console                                             |

Returns:

N/A

### TERR.Engine.engineExists

Purpose: Checks if the TERR engine or engine pool exists.

Function Signature:

```java
boolean engineExists(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

Boolean

### TERR.Engine.getLastErrorMessage

Purpose: Gets the last error message of the engine. If engine pool name is passed as a parameter, the error message will be retrieved from Terr engine assigned to the thread invoking this function.

Function Signature:

```java
String getLastErrorMessage(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

String

### TERR.Engine.getVariable

Purpose: Gets the variable from TERR. If engine pool name is passed as a parameter, the variable value will be returned from Terr engine assigned to the thread invoking this function.

Function Signature:

```java
Object[] getVariable(String engine, String var)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| var           | Variable Name                                                             |

Returns:

Object[]

### TERR.Engine.interrupt

Purpose: interrupt the given engine

Function Signature:

```java
void interrupt(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

N/A

### TERR.Engine.invokeTERRFunction

Purpose: Calls the Terr engine and returns the function results.If engine pool name is passed as a parameter, the function will be executed in the Terr engine assigned to the thread invoking this function.

Function Signature:

```java
Object[] invokeTERRFunction(String engine, String functionName, Object... args)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| functionName  | Function Name                                                             |
| args          | Element of Object.. should be String if it is TERR variable name or object| 
|               | if within BE node. It should be null if function doesn't take arguments   |

Returns:

Object[]

### TERR.Engine.isEngineRunning

Purpose: returns true if the engine is ON or false when it is OFF. If engine pool name is passed as a parameter, the status will be returned from Terr engine assigned to the thread invoking this function.

Function Signature:

```java
boolean isEngineRunning(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

Boolean

### TERR.Engine.setEngineParameters

Purpose: Set the engine parameters that control the initialization and execution of the spawned TERR engine.If engine pool name is passed as a parameter, the engine parameters will be set to all the engines in the engine pool

Function Signature:

```java
void setEngineParameters(String engine, String Parameters)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| Parameters    | Engine Parameters                                                         |

Returns:

N/A

### TERR.Engine.setJavaHome

Purpose: Sets the path to Java Home.If engine pool name is passed as a parameter, the java home will be set to all the engines in the engine pool

Function Signature:

```java
void setJavaHome(String engine, String Path)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| Path          | Java Home Path                                                            |

Returns:

N/A

### TERR.Engine.setJavaOptions

Purpose: Set the java options that get used when the TERR engine starts. Note - this API has no effect on an already started TERR engine.If engine pool name is passed as a parameter, the java options will be set to all the engines in the engine pool

Function Signature:

```java
void setJavaOptions(String engine, String Options)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| Options       | Java Options                                                              |

Returns:

N/A

### TERR.Engine.setTerrHome

Purpose: Sets the path to the base of the TERR installation (the directory containing subdirectories bin, library, and so on) that will be used for the spawned TERR engine.If engine pool name is passed as a parameter, the path will be set to all the engines in the engine pool

Function Signature:

```java
void setTerrHome(String engine, String Path)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| Path          | Engine or Engine Pool Name                                                |

Returns:

N/A

### TERR.Engine.setVariable

Purpose: Sets the variable within TERR.If engine pool name is passed as a parameter, the variable will be set in all the engines in the engine pool

Function Signature:

```java
void setVariable(String engine, String var, Object v)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 
| var           | Variable Name                                                             |
| v             | Object that needs to be set to given variable name                        |

Returns:

N/A

### TERR.Engine.startEngine

Purpose: Starts the given engine or engine pool.

Function Signature:

```java
void startEngine(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

N/A

### TERR.Engine.stopEngine

Purpose: Stops the given engine. If engine pool name is passed as a parameter, all the engines will be stopped in the engine pool

Function Signature:

```java
void stopEngine(String engine)
```
Args:

| Arguments     | Purpose                                                                   |
|:--------------|:------------------------------------------------------------------------- |
| engine        | Engine or Engine Pool Name                                                | 

Returns:

N/A
