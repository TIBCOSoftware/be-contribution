# TIBCO BusinessEvents® Contribution

This repository is to add various contributions across different modules within TIBCO BusinessEvents. It will include contributions by TIBCO BusinessEvents team as well external users/customers/fields teams.

Below are some modules where contributions are available and/or can be added,

* [Store](https://github.com/tibco/be-contribution/tree/main/store) - Persistent store implementations.
* [Metric](https://github.com/tibco/be-contribution/tree/main/metric) - Metric store implementations.
* [Channel](https://github.com/tibco/be-contribution/tree/main/channel) - Channels implementations.

 ## Pre-requisites

 Some pre-requisites that generically apply to all contributions existing or new. If there are conflicts/changes/limitations, call them out in individual contribution README's.

 * Download/install Java and setup appropriate JAVA_HOME and PATH environment variables. Existing contributions use Java 11. So use Java 11 or above.

 * All contributions will be maven projects. Download/Install Maven and setup appropriate M2_HOME and PATH environment variables. Existing contribution use Maven 3.5.4. So use maven 3.5.4 or above.

 * Install TIBCO Business Events 6.1.0 or above.

 * Add unit tests for each contribution.

 ## Getting Started

 Clone be-contribution repo,

	git clone https://github.com/tibco/be-contribution.git

 Every contribution has a bundled jar which can be used as-is. It should be under '/module-name/contribution-name/target'. E.g.
 
 	cd /metric/elasticsearch/target

  ### Building

  If you want to make any kind of changes around implementation and/or configuration, you can open the maven project into your choice of editor (Eclipse/IntelliJ/etc) and make appropriate changes.

  * Edit `pom.xml` file,
  	- Set <be.home> path to point to the TIBCO Business Events installation home.
    - Any third-party dependency version changes if needed.

  * Once done, open the command prompt and run the following command to build a new jar,

	mvn clean package

  A new jar should be created under '/modules-name/contribution-name/target'.


## Contributing

 Want to contribute? We've made it easy, all you need to do is fork this repository, make your changes and create a Pull Request! Once the pull request has been created, you'll be prompted to sign the CLA (Contributor License Agreement) online.
 Another great way to contribute would be to checkout the existing contributions. Perhaps there is something missing? Either enhance the existing ones or create a new store or metric or channel or fix a bug in an existing ones.


## License

This repository is licensed under a BSD-type license. See LICENSE for license text.
