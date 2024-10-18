package org.hieuho.querymeasurement;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {
    private static final int BUFFER_SIZE = 10000;
    private static final String DEFAULT_CSV = "src/main/resources/csv";
    private static final String DEFAULT_QUERIES = "src/main/resources/queries.txt";
    private List<String> queries = new ArrayList<>();
    private List<Path> csvFilePaths = new ArrayList<>();

    public FileReader(String csvFolderPath, String queriesFilePath) {
        this.csvFilePaths = getAllCSV(csvFolderPath);
        this.queries = getQueriesFromFile(queriesFilePath);
    }

    public FileReader() {
        this(DEFAULT_CSV, DEFAULT_QUERIES);
    }

    public FileReader(List<Path> csvFilePaths) {
        this.csvFilePaths = csvFilePaths;
        this.queries = getQueriesFromFile(DEFAULT_QUERIES);
    }

    public FileReader(String queriesFilePath) {
        this.csvFilePaths = getAllCSV(DEFAULT_CSV);
        this.queries = getQueriesFromFile(queriesFilePath);
    }

    public List<String> getQueries() {
        return queries;
    }

    public List<String> getCsvFileNamesWithoutExtension() {
        return csvFilePaths.stream().map(this::getFileNameWithoutExtension).toList();
    }

    private List<Path> getAllCSV(String csvFolderPath) {
        List<Path> csvFilePaths = new ArrayList<>();

        Path resourceDirectory = Paths.get(csvFolderPath);

        try (Stream<Path> paths = Files.walk(resourceDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".csv"))
                    .forEach(csvFilePaths::add);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return csvFilePaths;
    }

    public void importCSVFilesToDB() {
        csvFilePaths.forEach(filePath -> {
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

    private List<String> getQueriesFromFile(String filePath) {
        List<String> queries = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            StringBuilder query = new StringBuilder();
            for (String line : lines) {
                String trimmedLine = line.trim();
                if (trimmedLine.endsWith(";")) {
                    query.append(" ").append(trimmedLine);
                    queries.add(query.toString());
                    query = new StringBuilder();
                } else {
                    query.append(" ").append(trimmedLine);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return queries;
    }
}
