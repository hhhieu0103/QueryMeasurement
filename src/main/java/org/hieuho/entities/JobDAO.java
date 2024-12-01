package org.hieuho.entities;

import java.sql.SQLException;
import java.util.*;

public class JobDAO extends DataAccessObject implements DAOInterface<Job> {

    public JobDAO(String dbName) {
        super(dbName);
    }

    @Override
    public Job parse(String[] columns) {
        String schoolID = columns[2];
        String schoolCampus = columns[4];
        String departmentID = columns[6];
        String jobID = columns[9];
        String jobTitle = columns[10];

        return new Job(jobID, jobTitle, schoolID, schoolCampus, departmentID);
    }

    @Override
    public void addBatch(Set<Job> jobs) throws SQLException {
        String query = """
            INSERT INTO Job (
                JobID,
                JobTitle,
                SchoolID,
                SchoolCampus,
                DepartmentID
            ) VALUES (?, ?, ?, ?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (Job j : jobs) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, j.jobID()));
            record.add(new StatementParameter(2, j.jobTitle()));
            record.add(new StatementParameter(3, j.schoolID()));
            record.add(new StatementParameter(4, j.schoolCampus()));
            record.add(new StatementParameter(5, j.departmentID()));
            records.add(record);
        }

        super.addBatch(query, records);
    }

    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE Job (
                JobID TEXT NOT NULL,
                JobTitle TEXT NOT NULL,
                SchoolID TEXT NOT NULL,
                SchoolCampus TEXT NOT NULL,
                DepartmentID TEXT NOT NULL,
                PRIMARY KEY (JobID, SchoolID, SchoolCampus, DepartmentID),
                FOREIGN KEY (SchoolID, SchoolCampus) REFERENCES Campus,
                FOREIGN KEY (DepartmentID) REFERENCES Department
            );
        """;
        super.createTable(query);
    }

    public void dropTable() throws SQLException {
        super.dropTable("Job");
    }
}
