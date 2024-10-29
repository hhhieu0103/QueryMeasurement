package org.hieuho.querymeasurement;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            try {
                SalaryDAO salaryDAO = new SalaryDAO(getFileNameWithoutExtension(filePath));
                salaryDAO.dropTable();
                salaryDAO.createTable();

                java.io.FileReader filereader = new java.io.FileReader(filePath.toFile());
                CSVReader csvReader = new CSVReader(filereader);
                csvReader.readNext();
                String[] nextRecord;
                ArrayList<Salary> buffer = new ArrayList<>();
                int count = 0;

                while ((nextRecord = csvReader.readNext()) != null) {
                    if (buffer.size() == BUFFER_SIZE) {
                        salaryDAO.addBatch(buffer);
                        buffer = new ArrayList<>();
                    }
                    Salary salary = new Salary(nextRecord);
                    buffer.add(salary);
                    count++;
                }
                salaryDAO.addBatch(buffer);
                csvReader.close();
                System.out.println(filePath.toFile().getName() + " imported " + count + " records");
            }
            catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            }
        });
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
                System.out.println("Unable to extract queries from " + queryFile.toString());
                System.out.println(e.getMessage());
            }
        }

    }
}
