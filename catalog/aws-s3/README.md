# AWS S3 Catalog Function

## About

The AWS S3 Catalog function provides the ability to interact with an S3 bucket.


## Pre-requisites

None

## Getting Started

* Catalog functions are provided as BE JavaSrc resources that can be imported in to your own projects.

* Start BusinessEvents Studio and open your project.
  
* Use Project->Import->General->File System to import contents from JavaSrc folder.


## Functions

### S3.getS3Object

Get an object from an S3 bucket

```
	String result = S3.getS3Object(<<BUCKET>,<<OBJECT>>, <<REGION>>, <<APIKEY>>, <<APISECRETKEY>>);
```



#### Example

```
/**
 * @description 
 */
void rulefunction RuleFunctions.Startup {
	attribute {
		validity = ACTION;
	}
	scope {
		
	}
	body {
		System.debugOut("INFO >>>> RuleFunctions.Startup");
		String resultGet = S3.getS3Object("test-bucket","Users.yaml", "eu-west-1", "key", "secret");
	
		System.debugOut(resultGet);
	
	}
}
```


### S3.putS3Object

Put an object object to an S3 bucket

```
	String result = S3.putS3Object(<<BUCKET>, <<OBJECT>>, <<REGION>>, <<APIKEY>>, <<APISECRETKEY>>, <<STRING>>);
```	



#### Example

```
/**
 * @description 
 */
void rulefunction RuleFunctions.Startup {
	attribute {
		validity = ACTION;
	}
	scope {
		
	}
	body {
		System.debugOut("INFO >>>> RuleFunctions.Startup");
		String resultGet = S3.getS3Object("test-bucket","Users.yaml", "eu-west-1", "key", "secret");
	
		System.debugOut(resultGet);
		
		String resultPut = S3.putS3Object("test-bucket","Users2.yaml", "eu-west-1", "key", "secret",resultGet);
	
		System.debugOut(resultPut);
	
	}
}
```