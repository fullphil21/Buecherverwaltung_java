package de.phoeppner.phbuecherverwaltung.logic.db;

import de.phoeppner.phbuecherverwaltung.model.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Verwaltet die Datenbank, baut Verbindungen zur Datenbank auf.
 * Datenbank-Manager wird als Singleton aufgebaut.
 */
public class DbManager {
    //region Konstanten
    private static final String SERVER_IP = "localhost";
    private static final String DB_NAME = "buecherverwaltung";
    private static final String JDBC_PREFIX = "jdbc:mariadb://";
    private static final String CONNECTION_URL = JDBC_PREFIX + SERVER_IP +"/" +DB_NAME;
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    //endregion

    //region Attribute
    private static DbManager instance;
    private final DaoBook daoBook;

    //endregion

    //region Konstruktoren
    private DbManager(){
        daoBook = new DaoBook();
    }

    //endregion

    //region Methoden
    public static synchronized DbManager getInstance() {
        if(instance == null) instance = new DbManager();
        return instance;
    }

    private Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_URL, DB_USERNAME, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertDataRecord(Book book) {
        daoBook.create(getConnection(), book);
    }
    public List<Book> readAllDataRecords() {
        return daoBook.readAll(getConnection());
    }

    public void updateDataRecord(Book book) {
        daoBook.update(getConnection(), book);
    }

    public void deleteDataRecord(Book book) {
        daoBook.delete(getConnection(), book);
    }







    //endregion
}
