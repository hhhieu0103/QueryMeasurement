package org.hieuho.entities;

import java.sql.*;
import java.util.*;

public class DataAccessObject {
    protected final String dbName;

    public DataAccessObject(String dbName) {
        this.dbName = dbName;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s.sqlite", dbName));
    }

    protected int addBatch(String query, List<List<StatementParameter>> records) throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(query);
        for (List<StatementParameter> record : records) {
            fillParameters(statement, record);
            statement.addBatch();
        }
        int[] rows = statement.executeBatch();
        int insertedRows = Arrays.stream(rows).filter(i -> i == 1).toArray().length;
        connection.commit();
        statement.close();
        connection.close();
        return insertedRows;
    }

    private void executeUpdate(String query) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    protected void createTable(String query) throws SQLException {
        executeUpdate(query);
    }

    protected void dropTable(String tableName) throws SQLException {
        executeUpdate("DROP TABLE IF EXISTS " + tableName);
    }

    private void fillParameters(PreparedStatement statement, List<StatementParameter> parameters) throws SQLException {
        for (StatementParameter parameter : parameters) {
            if (parameter.value() instanceof String) {
                statement.setString(parameter.index(), parameter.value().toString());
            } else if (parameter.value() instanceof Integer) {
                statement.setInt(parameter.index(), (Integer) parameter.value());
            }
        }
    }
}
