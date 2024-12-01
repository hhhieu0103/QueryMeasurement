package org.hieuho.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DataAccessObject {
    private final String dbName;

    public DataAccessObject(String dbName) {
        this.dbName = dbName;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s.sqlite", dbName));
    }

    protected void addBatch(String query, List<List<StatementParameter>> records) throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(query);
        for (List<StatementParameter> record : records) {
            for (StatementParameter parameter : record) {
                if (parameter.value() instanceof String) {
                    statement.setString(parameter.index(), parameter.value().toString());
                } else if (parameter.value() instanceof Integer) {
                    statement.setInt(parameter.index(), (Integer) parameter.value());
                }
            }
            statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
        connection.close();
    }

    private void executeDDL(String query) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    protected void createTable(String query) throws SQLException {
        executeDDL(query);
    }

    protected void dropTable(String tableName) throws SQLException {
        executeDDL("DROP TABLE IF EXISTS " + tableName);
    }
}
