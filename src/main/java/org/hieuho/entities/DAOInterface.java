package org.hieuho.entities;

import java.sql.SQLException;
import java.util.Set;

public interface DAOInterface<T> {
    void createTable() throws SQLException;
    void dropTable() throws SQLException;
    T parse(String[] columns);
    void addBatch(Set<T> records) throws SQLException;
}
