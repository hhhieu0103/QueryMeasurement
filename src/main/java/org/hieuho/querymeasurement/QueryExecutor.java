package org.hieuho.querymeasurement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QueryExecutor {

    //NUMBER_OF_RUN_TIMES is configurable
    private final int NUMBER_OF_RUN_TIMES = 10;
    private final List<String> csvFileNames;
    private final List<String> queries;
    private final float[][] executionTimes;

    public QueryExecutor(List<String> csvFileNames, List<String> queries) {
        this.csvFileNames = csvFileNames;
        this.queries = queries;
        this.executionTimes = new float[csvFileNames.size()][queries.size()];
    }

    private Connection getConnection(String dbName) throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s.sqlite", dbName));
    }

    public void executeQuery() throws SQLException {
        for (int i = 0; i < csvFileNames.size(); i++) {
            Connection connection = getConnection(csvFileNames.get(i));
            for (int j = 0; j < queries.size(); j++) {
                executionTimes[i][j] = 0;
                for (int n = 0; n < this.NUMBER_OF_RUN_TIMES; n++) {
                    PreparedStatement preparedStatement = connection.prepareStatement(queries.get(j));
                    long start = System.nanoTime();
                    preparedStatement.executeQuery();
                    long end = System.nanoTime();
                    long executionTime = end - start;
                    preparedStatement.close();
                    executionTimes[i][j] += executionTime/1000000f;
                }
                executionTimes[i][j] = executionTimes[i][j] / (float) NUMBER_OF_RUN_TIMES;
            }
        }
    }

    public float[][] getExecutionTimes() {
        return executionTimes;
    }
}
