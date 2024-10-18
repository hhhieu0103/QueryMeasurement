package org.hieuho.querymeasurement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SalaryDAO {

    private final String dbName;

    public SalaryDAO(String dbName) {
        this.dbName = dbName;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(String.format("jdbc:sqlite:%s.sqlite", dbName));
    }

    public void addBatch(List<Salary> records) throws SQLException {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        String query = """
                INSERT INTO Salary
                    (PersonID,
                    PersonName,
                    SchoolID,
                    SchoolName,
                    SchoolCampus,
                    DepartmentName,
                    DepartmentID,
                    BirthDate,
                    StillWorking,
                    JobID,
                    JobTitle,
                    Earnings,
                    EarningsYear)
                VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement statement = connection.prepareStatement(query);
        for (Salary salary : records) {
            statement.setInt(1, salary.getPersonID());
            statement.setString(2, salary.getPersonName());
            statement.setString(3, salary.getSchoolID());
            statement.setString(4, salary.getSchoolName());
            statement.setString(5, salary.getSchoolCampus());
            statement.setString(6, salary.getDepartmentName());
            statement.setString(7, salary.getDepartmentID());
            statement.setString(8, salary.getBirthDate().toString());
            statement.setString(9, salary.getStillWorking());
            statement.setString(10, salary.getJobID());
            statement.setString(11, salary.getJobTitle());
            statement.setInt(12, salary.getEarnings());
            statement.setInt(13, salary.getEarningsYear());
            statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
        connection.close();
    }

    public void createTable() throws SQLException {
        Connection connection = getConnection();
        String createTableSalaryQuery = """
            CREATE TABLE Salary
            (
                PersonID       INT NOT NULL,
                PersonName     TEXT NOT NULL,
                SchoolID       TEXT NOT NULL,
                SchoolName     TEXT NOT NULL,
                SchoolCampus   TEXT NOT NULL,
                DepartmentName TEXT NOT NULL,
                DepartmentID   TEXT NOT NULL,
                BirthDate      TEXT NOT NULL,
                StillWorking   TEXT NOT NULL,
                JobID          TEXT NOT NULL,
                JobTitle       TEXT NOT NULL,
                Earnings       INT NOT NULL,
                EarningsYear   INT NOT NULL
            )""";
        PreparedStatement statement = connection.prepareStatement(createTableSalaryQuery);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    public void dropTable() throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS Salary");
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }
}
