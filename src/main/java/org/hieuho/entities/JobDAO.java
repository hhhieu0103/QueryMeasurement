package org.hieuho.entities;

import java.sql.SQLException;
import java.util.*;

public class JobDAO extends DataAccessObject implements DAOInterface<Job> {

    public JobDAO(String dbName) {
        super(dbName);
    }

    @Override
    public Job parse(String[] columns) {

        String jobID = columns[9];
        String jobTitle = columns[10];

        return new Job(jobID, jobTitle);
    }

    @Override
    public void addBatch(Set<Job> jobs) throws SQLException {
        String query = """
            INSERT OR IGNORE INTO Job (
                JobID,
                JobTitle
            ) VALUES (?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (Job j : jobs) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, j.jobID()));
            record.add(new StatementParameter(2, j.jobTitle()));
            records.add(record);
        }

        super.addBatch(query, records);
    }

    @Override
    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE Job (
                JobID TEXT NOT NULL,
                JobTitle TEXT NOT NULL,
                PRIMARY KEY (JobID)
            );
        """;
        super.createTable(query);
    }

    @Override
    public void dropTable() throws SQLException {
        super.dropTable("Job");
    }
}
