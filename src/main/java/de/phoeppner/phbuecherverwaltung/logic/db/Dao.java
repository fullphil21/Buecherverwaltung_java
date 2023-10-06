package de.phoeppner.phbuecherverwaltung.logic.db;

import java.sql.Connection;
import java.util.List;

/**
 * Interface, dass Methoden für die CRUD-Operationen zur Verfügung stellt
 * @param <T> : Instanz einer beliebigen Klasse
 */
public interface Dao<T> {
    void create(Connection dbConnection, T object);
    List<T> readAll(Connection dbConnection);
    void update(Connection dbConnection, T object);
    void delete(Connection dbConnection, T object);
}
