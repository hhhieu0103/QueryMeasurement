package org.hieuho.entities;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PersonDAO extends DataAccessObject implements DAOInterface<Person> {

    public PersonDAO(String dbName) {
        super(dbName);
    }

    @Override
    public Person parse(String[] columns) {
        int personID = Integer.parseInt(columns[0]);
        String personName = columns[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
        LocalDate birthDate = LocalDate.parse(columns[7], formatter);
        return new Person(personID, personName, birthDate);
    }

    @Override
    public void addBatch(Set<Person> people) throws SQLException {
        String query = """
            INSERT INTO Person (
                PersonID,
                PersonName,
                BirthDate
            ) VALUES (?, ?, ?)
        """;

        List<List<StatementParameter>> records = new ArrayList<>();
        for (Person p : people) {
            List<StatementParameter> record = new ArrayList<>();
            record.add(new StatementParameter(1, p.personID()));
            record.add(new StatementParameter(2, p.personName()));
            record.add(new StatementParameter(3, p.birthDate()));
            records.add(record);
        }

        super.addBatch(query, records);
    }

    public void createTable() throws SQLException {
        String query = """
            CREATE TABLE Person (
                PersonID       INTEGER NOT NULL,
                PersonName     TEXT NOT NULL,
                BirthDate      TEXT NOT NULL,
                PRIMARY KEY (PersonID)
            )""";
        super.createTable(query);
    }

    public void dropTable() throws SQLException {
        super.dropTable("Person");
    }
}
