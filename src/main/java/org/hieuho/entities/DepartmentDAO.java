package org.hieuho.entities;

import java.sql.SQLException;
import java.util.*;

public class DepartmentDAO extends DataAccessObject implements DAOInterface<Department> {

    public DepartmentDAO(String dbName) {
        super(dbName);
    }

    @Override
    public Department parse(String[] columns) {
        String departmentName = columns[5];
        String departmentID = columns[6];
        return new Department(departmentID, departmentName);
    }

    @Override
    public int addBatch(Set<Department> departments) throws SQLException {
        String query = """
            INSERT OR IGNORE INTO Department (
                DepartmentID,
                DepartmentName
            ) VALUES (?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (Department d : departments) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, d.departmentID()));
            record.add(new StatementParameter(2, d.departmentName()));
            records.add(record);
        }

        return super.addBatch(query, records);
    }

    @Override
    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE Department (
                DepartmentID TEXT NOT NULL,
                DepartmentName TEXT NOT NULL,
                PRIMARY KEY (DepartmentID)
            );
        """;
        super.createTable(query);
    }

    @Override
    public void dropTable() throws SQLException {
        super.dropTable("Department");
    }
}
