# CIS552 - Database Design Project

This is a project for the CIS552 course, built using Maven in IntelliJ IDEA.
The project visualizes the query execution performance on different data sizes and queries.

## Running the Project

### Preparing data

Please make sure that the csv data files are located under ```src > main > resources > data```.

You can run this project via IntelliJ IDEA or directly through Maven.
Personally, I recommend running this project with IntelliJ IDEA.
You can also use any other IDEs that support Java Maven Project.

### [RECOMMENDED] Running with IntelliJ IDEA

**Requirements**:
- **IntelliJ IDEA**: You can download it [here](https://www.jetbrains.com/idea/).

1. **Open the Project**:
   - Open the project folder in IntelliJ IDEA.

2. **Run the Project**:
   - Option 1:
     - Navigate to ```src/main/java/org.hieuho.querymeasurement/QueryMeasurementApplication.java```
     - Click the **Play** button on the top bar to run the main class.
   - Option 2: Open the **Maven** tool window (usually on the right panel), and navigate to:
     ```Plugins > javafx > javafx:run```
     Double-click on ```javafx:run``` to start the project.

### Running with Maven from Command Line

**Requirements**:
- **Java Development Kit (JDK)**: You can download and install JDK (version >= 22) [here](https://www.oracle.com/java/technologies/downloads/).
- **Maven**: You can download the binary version of Maven [here](https://maven.apache.org/download.cgi). After extracting, please add the ```bin``` directory to the ```PATH``` environment variable.

1. **Open a Terminal** in the project directory.
2. **Run the Application**:
   Run the command: ```mvn clean javafx:run```

## Using the Application

### Viewing Different Charts

- The left panel displays a list of available charts. To switch between charts, select an item from this list.
- Under the left panel, there is a file list showing data files (CSV format) and query files (TXT format).

### Selecting Data Files

By default, the application loads data files available on MyCourses each time it starts. You can:

- **Load Custom Data**:
   - Select ```File > Select Data Files``` from the top menu to import alternative data files. Please wait a few seconds if you select large data files (Files are considered large if they are around 100MB or larger).
- **Revert to Default Data**:
   - Select ```File > Select Default Data``` to reload the original data files.

### Selecting Query Files

The application also loads a default query file at startup. You can:

- **Load Custom Query Files**:
   - Go to ```File > Select Query Files``` to load custom query files. Please make sure the queries in your files are ended with an ```;``` and no more than 1 query are on the same line. For example, you can check the default query file at this path ```src/main/resources/query/queries.txt```
- **Revert to Default Queries**:
   - Select ```File > Select Default Queries``` to reload the original query file.

### Saving Charts as Images

To save the generated charts as images, select ```File > Save Charts to Files```. Images will be saved in the specified directory.

## Troubleshooting

- **Maven Build Issues**: If you experience build issues with Maven, try running:
  mvn clean install
  This command clears cached dependencies and rebuilds the project.

