package org.hieuho.querymeasurement;

import com.opencsv.CSVReader;
import org.hieuho.entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileReader {
    private static final int BUFFER_SIZE = 10000;
    private static final String DEFAULT_DATA_PATH = "src/main/resources/data";
    private static final String DEFAULT_QUERY_PATH = "src/main/resources/query";
    private List<Path> queryFilePaths = new ArrayList<>();
    private final List<String> queries = new ArrayList<>();
    private List<Path> dataFilePaths = new ArrayList<>();

    public FileReader(String dataFolderPath, String queryFolderPath) {
        this.dataFilePaths = getFilePathsFromFolder(dataFolderPath, ".csv");
        this.queryFilePaths = getFilePathsFromFolder(queryFolderPath, ".txt");
        extractQueries();
    }

    public FileReader() {
        this(DEFAULT_DATA_PATH, DEFAULT_QUERY_PATH);
    }

    public List<String> getQueries() {
        return queries;
    }

    public void setQueryFilePaths(List<Path> queryFilePaths) {
        this.queryFilePaths = queryFilePaths;
        extractQueries();
    }

    public void setDataFilePaths(List<Path> dataFilePaths) {
        this.dataFilePaths = dataFilePaths;
    }

    public void setDefaultDataPath() {
        this.dataFilePaths = getFilePathsFromFolder(DEFAULT_DATA_PATH, ".csv");
    }

    public void setDefaultQueryPath() {
        this.queryFilePaths = getFilePathsFromFolder(DEFAULT_QUERY_PATH, ".txt");
        extractQueries();
    }

    public List<String> getDataFileNamesWithoutExtension() {
        return dataFilePaths.stream().map(this::getFileNameWithoutExtension).toList();
    }

    public List<String> getQueryFileNamesWithoutExtension() {
        return queryFilePaths.stream().map(this::getFileNameWithoutExtension).toList();
    }

    private List<Path> getFilePathsFromFolder(String folderPath, String extension) {
        List<Path> filePaths = new ArrayList<>();
        Path resourceDirectory = Paths.get(folderPath);

        try (Stream<Path> paths = Files.walk(resourceDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(extension))
                    .forEach(filePaths::add);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return filePaths;
    }

    public void importCSVFilesToDB() {
        dataFilePaths.forEach(filePath -> {
            String dbName = getFileNameWithoutExtension(filePath);
            importObjectToDB(filePath, new PersonDAO(dbName));
            importObjectToDB(filePath, new SchoolDAO(dbName));
            importObjectToDB(filePath, new CampusDAO(dbName));
            importObjectToDB(filePath, new DepartmentDAO(dbName));
            importObjectToDB(filePath, new JobDAO(dbName));
            importObjectToDB(filePath, new FinancialRecordDAO(dbName));
        });
    }

    private <T> void importObjectToDB(Path filePath, DAOInterface<T> daoInterface) {
        try {
            daoInterface.dropTable();
            daoInterface.createTable();

            java.io.FileReader filereader = new java.io.FileReader(filePath.toFile());
            CSVReader csvReader = new CSVReader(filereader);
            csvReader.readNext();
            String[] nextRecord;
            Set<T> buffer = new HashSet<>();
            int count = 0;

            while ((nextRecord = csvReader.readNext()) != null) {
                if (buffer.size() == BUFFER_SIZE) {
                    daoInterface.addBatch(buffer);
                    buffer = new HashSet<>();
                    count += BUFFER_SIZE;
                }
                T object = daoInterface.parse(nextRecord);
                buffer.add(object);
            }
            count += buffer.size();
            daoInterface.addBatch(buffer);
            csvReader.close();
            System.out.printf("%s imported %d %s records.%n", filePath.toFile().getName(), count, daoInterface.getClass().getSimpleName());
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private String getFileNameWithoutExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private void extractQueries() {
        for (Path queryFile : queryFilePaths) {
            try {
                String text = new String(Files.readAllBytes(Paths.get(queryFile.toString())));
                queries.clear();
                queries.addAll(Arrays.stream(text.split(";")).map(String::trim).toList());
            } catch (IOException e) {
                System.out.println("Unable to extract queries from " + queryFile);
                System.out.println(e.getMessage());
            }
        }

    }
}
