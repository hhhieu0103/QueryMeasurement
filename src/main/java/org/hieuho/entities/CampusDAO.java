package org.hieuho.entities;

import java.sql.SQLException;
import java.util.*;

public class CampusDAO extends DataAccessObject implements DAOInterface<Campus> {

    public CampusDAO(String dbName) {
        super(dbName);
    }

    @Override
    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE Campus (
                SchoolID TEXT NOT NULL,
                SchoolCampus TEXT NOT NULL,
                PRIMARY KEY (SchoolID, SchoolCampus),
                FOREIGN KEY (SchoolID) REFERENCES School(SchoolID)
            );
        """;
        super.createTable(query);
    }

    @Override
    public void dropTable() throws SQLException {
        super.dropTable("Campus");
    }

    @Override
    public Campus parse(String[] columns) {
        String schoolID = columns[2];
        String schoolCampus = columns[4];
        return new Campus(schoolID, schoolCampus);
    }

    @Override
    public int addBatch(Set<Campus> campuses) throws SQLException {
        String query = """
            INSERT OR IGNORE INTO Campus (
                SchoolID,
                SchoolCampus
            ) VALUES (?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (Campus c : campuses) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, c.schoolID()));
            record.add(new StatementParameter(2, c.schoolCampus()));
            records.add(record);
        }

        return super.addBatch(query, records);
    }
}
