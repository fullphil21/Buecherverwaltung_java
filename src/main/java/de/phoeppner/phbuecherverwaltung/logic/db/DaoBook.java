package de.phoeppner.phbuecherverwaltung.logic.db;

import de.phoeppner.phbuecherverwaltung.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementiert Methoden, um Bücher in der Datenbank anzulegen, zu aktualisieren, zu löschen oder auszulesen.
 */
public class DaoBook implements Dao<Book>{
    //region Konstanten
    public static final String COL_ISBN = "isbn";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_YEAR = "year";
    public static final String COL_PAGES = "pages";
    public static final String COL_ID = "id";
    public static final String INSERT_BOOK_STATEMENT = "INSERT INTO books (isbn, title, author, year, pages) VALUES (?,?,?,?,?);";
    public static final String SELECT_BOOKS_STATEMENT = "SELECT * FROM books;";
    public static final String DELETE_BOOK_STATEMENT = "DELETE FROM books WHERE id=?;";
    public static final String UPDATE_BOOK_STATEMENT = "UPDATE books SET isbn=?, title=?, author=?, year=?, pages=? WHERE id=?;";
    public static final int PARAMETER_INDEX_FIRST = 1;
    public static final int PARAMETER_INDEX_SECOND = 2;
    public static final int PARAMETER_INDEX_THIRD = 3;
    public static final int PARAMETER_INDEX_FOURTH = 4;
    public static final int PARAMETER_INDEX_FIFTH = 5;
    public static final int PARAMETER_INDEX_SIXTH = 6;
    public static final String DB_COL_LABEL = "insert_id";

    //endregion

    //region Attribute
    //endregion

    //region Konstruktoren
    //endregion

    //region Methoden
    @Override
    public void create(Connection dbConnection, Book book) {
        PreparedStatement statement = null;
        try {
            statement = dbConnection.prepareStatement(INSERT_BOOK_STATEMENT, Statement.RETURN_GENERATED_KEYS);

            statement.setString(PARAMETER_INDEX_FIRST, book.getIsbn());
            statement.setString(PARAMETER_INDEX_SECOND, book.getTitle());
            statement.setString(PARAMETER_INDEX_THIRD, book.getAuthor());
            statement.setInt(PARAMETER_INDEX_FOURTH, book.getYear());
            statement.setInt(PARAMETER_INDEX_FIFTH, book.getPages());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int insertId = resultSet.getInt(DB_COL_LABEL);
                book.setId(insertId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Book> readAll(Connection dbConnection) {
        List<Book> books = new ArrayList<>();

        Statement statement = null;

        try {
            statement = dbConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_BOOKS_STATEMENT);

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getString(COL_ISBN),
                        resultSet.getString(COL_TITLE),
                        resultSet.getString(COL_AUTHOR),
                        resultSet.getInt(COL_YEAR),
                        resultSet.getInt(COL_PAGES)
                );
                book.setId(resultSet.getInt(COL_ID));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return books;
    }


    @Override
    public void update(Connection dbConnection, Book book) {
        PreparedStatement statement = null;
        try {
            statement = dbConnection.prepareStatement(UPDATE_BOOK_STATEMENT);

            statement.setString(PARAMETER_INDEX_FIRST, book.getIsbn());
            statement.setString(PARAMETER_INDEX_SECOND, book.getTitle());
            statement.setString(PARAMETER_INDEX_THIRD, book.getAuthor());
            statement.setInt(PARAMETER_INDEX_FOURTH, book.getYear());
            statement.setInt(PARAMETER_INDEX_FIFTH, book.getPages());
            statement.setInt(PARAMETER_INDEX_SIXTH, book.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Connection dbConnection, Book book) {
        PreparedStatement statement = null;

        try {
            statement = dbConnection.prepareStatement(DELETE_BOOK_STATEMENT);
            statement.setInt(PARAMETER_INDEX_FIRST, book.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                dbConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //endregion
}
