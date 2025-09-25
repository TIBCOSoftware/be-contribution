# TIBCO BusinessEvents® Contribution

This repository is to add various contributions across different modules within TIBCO BusinessEvents. It will include contributions by TIBCO BusinessEvents team as well external users/customers/fields teams.

Below are some modules where contributions are available and/or can be added,

* [Store](https://github.com/tibco/be-contribution/tree/main/store) - Persistent store implementations.
* [Metric](https://github.com/tibco/be-contribution/tree/main/metric) - Metric store implementations.
* [Channel](https://github.com/tibco/be-contribution/tree/main/channel) - Channels implementations.
* [Catalog](https://github.com/tibco/be-contribution/tree/main/catalog) - Catalog functions implementations.

 ## Pre-requisites

 Some pre-requisites that generically apply to all contributions existing or new. If there are conflicts/changes/limitations, call them out in individual contribution README's.

 * Download/install Java and setup appropriate JAVA_HOME and PATH environment variables. Existing contributions use `Java 17`. So use Java 17 or above.

 * All contributions will be maven projects. Download/Install Maven and setup appropriate M2_HOME and PATH environment variables. Existing contribution use `Maven 3.9.6`. So use maven 3.9.6 or above.

 * Install docker, since most integration test will need to create a light throw away container.

 * Install `TIBCO BusinessEvents 6.3.x` and above.

 * Add necessary unit and/or integration tests for each contribution.

 ## Getting Started

 * Clone be-contribution repo,
 ```
 git clone https://github.com/tibco/be-contribution.git
 ```

 * Existing Contribution,

   - If you want to make any kind of changes around existing implementation and/or configuration, you can open the maven project into your choice of editor (Eclipse/IntelliJ/etc) and make appropriate changes. 

   - Follow the steps outlined in 'Building' section below to create a contribution jar.

 * New Contribution, 
 
   - First step will be to go through existing module specific pre-requite documents and contributions to get a sense of how they are implemented/structured/built.

   - Creat a new maven project. Add it as a child module to the parent module. E.g. `elasticsearch` metric contribution is marked as a sub module within '/metric/pom.xml'.

   - Finally follow the steps outlined in 'Building' to build the new contribution jar.
   

  ### Building

  Follow the below steps to build a contribution jar,

  * Once all changes for existing ones or for any new contributions are done. Edit root `pom.xml` file located under 'be-contribution',
    - Set `<be.home>` path to point to the TIBCO BusinessEvents installation home or pass it at command line while building the jar, ref below.
    - Any common third-party dependency version changes if needed.
    - Exclude any conflicting/duplicate jars off BusinessEvents and/or thirdparty dependencies from being packaged into the 'uber jar'. Some obvious ones are already excluded. Examples of conflicting/duplicate jars are BusinessEvents jars or common 3rd party ones like, jackson, httpclient, log4j, etc, these are already available under 'BE_HOME/lib/ext/tpcl', so it would be best to avoid packaging them again to prevent any class loading as well as jar size related issues.

  * Any contribution specific dependencies go in contribution specific `pom.xml` file.

  * Once done, open the command prompt, goto the the specific contribution folder('/modules-name/contribution-name') and run the maven command to build a new jar. E.g. passing the be.home path and skipping tests.
```
    cd /metric/elasticsearch
    mvn clean install -Dbe.home=/path/to/be/home -DskipTests
```
  
  * A new jar should be created under '/modules-name/contribution-name/target'. E.g. /metric/elasticsearch/target. You can then copy this jar to 'BE_HOME/lib/ext/tpcl/contrib' to make it available for use in BusinessEvents.


## Contributing

 Want to contribute? We've made it easy, all you need to do is fork this repository, make your changes and create a Pull Request! Once the pull request has been created, you'll be prompted to sign the CLA (Contributor License Agreement) online.
 Another great way to contribute would be to checkout the existing contributions. Perhaps there is something missing? Either enhance the existing ones or create a new store or metric or channel or fix a bug in an existing ones.
 
For additional details, refer to the [Contribution Guidelines](https://github.com/tibco/be-contribution/blob/main/CONTRIBUTING.md).



## License

This repository is licensed under a BSD-type license. See LICENSE for license text.
