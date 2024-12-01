package org.hieuho.entities;

import java.sql.SQLException;
import java.util.*;

public class SchoolDAO extends DataAccessObject implements DAOInterface<School> {

    public SchoolDAO(String dbName) {
        super(dbName);
    }

    @Override
    public School parse(String[] columns) {
        String schoolID = columns[2];
        String schoolName = columns[3];
        return new School(schoolID, schoolName);
    }

    @Override
    public void addBatch(Set<School> schools) throws SQLException {
        String query = """
            INSERT INTO School (
                SchoolID,
                SchoolName,
            ) VALUES (?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (School s : schools) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, s.schoolID()));
            record.add(new StatementParameter(2, s.schoolName()));
            records.add(record);
        }

        super.addBatch(query, records);
    }

    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE School (
                SchoolID TEXT NOT NULL,
                SchoolName TEXT NOT NULL,
                PRIMARY KEY (SchoolID)
            );
        """;
        super.createTable(query);
    }

    public void dropTable() throws SQLException {
        super.dropTable("School");
    }
}
