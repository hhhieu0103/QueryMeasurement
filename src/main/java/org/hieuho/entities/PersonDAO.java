package org.hieuho.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PersonDAO extends DataAccessObject implements DAOInterface<Person> {

    public PersonDAO(String dbName) {
        super(dbName);
    }

    @Override
    public Person parse(String[] columns) {
        int personID = Integer.parseInt(columns[0]);
        String personName = columns[1];
        String birthDate = columns[7];
        return new Person(personID, personName, birthDate);
    }

    @Override
    public int addBatch(Set<Person> people) throws SQLException {
        String query = """
            INSERT OR IGNORE INTO Person (
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

        return super.addBatch(query, records);
    }

    @Override
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

    @Override
    public void dropTable() throws SQLException {
        super.dropTable("Person");
    }

//    public Person getPersonByName(String name) throws SQLException {
//        String query = """
//            SELECT p.* FROM Person p
//            WHERE p.personName = ?
//            """;
//        List<StatementParameter> parameters = new ArrayList<>();
//        parameters.add(new StatementParameter(1, name));
//        Map<String, Object> personMap = super.executeQuery(query, parameters);
//        int personID = (int) personMap.get("PersonID");
//        String personName = (String) personMap.get("PersonName");
//        String birthDate = (String) personMap.get("BirthDate");
//        return new Person(personID, personName, birthDate);
//    }
}
