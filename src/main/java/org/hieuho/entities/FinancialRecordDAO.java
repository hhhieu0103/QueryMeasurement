package org.hieuho.entities;

import java.sql.SQLException;
import java.util.*;

public class FinancialRecordDAO extends DataAccessObject implements DAOInterface<FinancialRecord> {

    public FinancialRecordDAO(String dbName) {
        super(dbName);
    }

    @Override
    public FinancialRecord parse(String[] columns) {
        int personID = Integer.parseInt(columns[0]);
        boolean stillWorking = Objects.equals(columns[8], "yes");
        String jobID = columns[9];
        int earnings = Integer.parseInt(columns[11]);
        int earningsYear = Integer.parseInt(columns[12]);
        return new FinancialRecord(personID, jobID, stillWorking, earnings, earningsYear);
    }

    @Override
    public void addBatch(Set<FinancialRecord> financialRecords) throws SQLException {
        String query = """
            INSERT INTO FinancialRecord (
                PersonID,
                JobID,
                StillWorking,
                Earnings,
                EarningsYear
            ) VALUES (?, ?, ?, ?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (FinancialRecord fr : financialRecords) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, fr.personID()));
            record.add(new StatementParameter(2, fr.jobID()));
            record.add(new StatementParameter(3, fr.stillWorking()));
            record.add(new StatementParameter(4, fr.earnings()));
            record.add(new StatementParameter(5, fr.earningsYear()));
            records.add(record);
        }

        super.addBatch(query, records);
    }

    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE FinancialRecord (
                PersonID INTEGER NOT NULL,
                JobID TEXT NOT NULL,
                StillWorking INTEGER NOT NULL,
                Earnings INTEGER NOT NULL,
                EarningsYear INTEGER NOT NULL,
                PRIMARY KEY (PersonID, JobID, StillWorking, Earnings, EarningsYear),
                FOREIGN KEY (PersonID) REFERENCES Person,
                FOREIGN KEY (JobID) REFERENCES Job
            );
        """;
        super.createTable(query);
    }

    public void dropTable() throws SQLException {
        super.dropTable("FinancialRecord");
    }
}
