# CAB 401 – High Performance and Parallel Computing

The original sequential version can be found in the Sequential class in the qut package. All the different parallel version (and only code change i made) can be found in the qut.parallel_versions package.

## Running the application
**Step 1**

Navigate to projects directory in your terminal (the folder with the pom.xml file).

**Step 2**

Compile the project by running the following command:
 `mvn compile`
 
 **Step 3**
 
Start the Sequential version using the command:
`mvn exec:java -Dexec.mainClass="qut.Sequential"`

Start the BasicSteamingVersion using the command:
`mvn exec:java -Dexec.mainClass="qut.parallel_versions.BasicSteamingVersion"`

Start the ExecutorServiceCollectorVersion using the command:
`mvn exec:java -Dexec.mainClass="qut.parallel_versions.ExecutorServiceCollectorVersion"`

Start the ExecutorServiceSynconzidedVersion using the command:
`mvn exec:java -Dexec.mainClass="qut.parallel_versions.ExecutorServiceSynconzidedVersion"`

Start the SteamingVersion using the command:
`mvn exec:java -Dexec.mainClass="qut.parallel_versions.SteamingVersion"`
