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
        String schoolID = columns[2];
        String schoolCampus = columns[4];
        String departmentID = columns[6];
        int stillWorking = Objects.equals(columns[8], "yes") ? 1 : 0;
        String jobID = columns[9];
        int earnings = Integer.parseInt(columns[11]);
        int earningsYear = Integer.parseInt(columns[12]);

        return new FinancialRecord(personID, jobID, schoolID, schoolCampus, departmentID, stillWorking, earnings, earningsYear);
    }

    @Override
    public int addBatch(Set<FinancialRecord> financialRecords) throws SQLException {
        String query = """
            INSERT OR IGNORE INTO FinancialRecord (
                PersonID,
                JobID,
                SchoolID,
                SchoolCampus,
                DepartmentID,
                StillWorking,
                Earnings,
                EarningsYear
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (FinancialRecord fr : financialRecords) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, fr.personID()));
            record.add(new StatementParameter(2, fr.jobID()));
            record.add(new StatementParameter(3, fr.schoolID()));
            record.add(new StatementParameter(4, fr.schoolCampus()));
            record.add(new StatementParameter(5, fr.departmentID()));
            record.add(new StatementParameter(6, fr.stillWorking()));
            record.add(new StatementParameter(7, fr.earnings()));
            record.add(new StatementParameter(8, fr.earningsYear()));
            records.add(record);
        }

        return super.addBatch(query, records);
    }

    @Override
    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE FinancialRecord (
                RecordID INTEGER PRIMARY KEY AUTOINCREMENT,
                PersonID INTEGER NOT NULL,
                JobID TEXT NOT NULL,
                SchoolID TEXT NOT NULL,
                SchoolCampus TEXT NOT NULL,
                DepartmentID TEXT NOT NULL,
                StillWorking INTEGER NOT NULL,
                Earnings INTEGER NOT NULL,
                EarningsYear INTEGER NOT NULL,
                FOREIGN KEY (PersonID) REFERENCES Person,
                FOREIGN KEY (JobID) REFERENCES Job,
                FOREIGN KEY (SchoolID) REFERENCES School,
                FOREIGN KEY (SchoolCampus) REFERENCES SchoolCampus,
                FOREIGN KEY (DepartmentID) REFERENCES Department
            );
        """;
        super.createTable(query);
    }

    @Override
    public void dropTable() throws SQLException {
        super.dropTable("FinancialRecord");
    }
}
