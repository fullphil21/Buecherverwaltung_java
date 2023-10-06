package de.phoeppner.phbuecherverwaltung.logic;

import de.phoeppner.phbuecherverwaltung.logic.db.DbManager;
import de.phoeppner.phbuecherverwaltung.model.Book;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * Stellt Bücher als ObservableList zur Verfügung
 * wird als Singleton implementiert, damit immer auf dieselbe Liste zugegriffen wird.
 */
public class BookHolder {
    //region Konstanten
    //endregion

    //region Attribute
    private static BookHolder instance;
    private final ObservableList<Book> bookList;

    //endregion

    //region Konstruktoren
    private BookHolder() {

        bookList = FXCollections.observableArrayList(book ->
                new Observable[]{
                        book.isbnProperty(), book.titleProperty(), book.authorProperty(),
                        book.yearProperty(), book.pagesProperty()
                });

        bookList.addAll(DbManager.getInstance().readAllDataRecords());


        //Listener der Änderungen in der Liste überwacht
        bookList.addListener((ListChangeListener<Book>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (Book removedBook : change.getRemoved()) {
                        DbManager.getInstance().deleteDataRecord(removedBook);
                    }

                } else if (change.wasAdded()) {
                    for (Book addedBook : change.getAddedSubList()) {
                        DbManager.getInstance().insertDataRecord(addedBook);
                    }

                } else if (change.wasUpdated()) {
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        Book updatedBook = change.getList().get(i);
                        DbManager.getInstance().updateDataRecord(updatedBook);
                    }
                }
            }
        });
    }

    //endregion

    //region Methoden
    public static synchronized BookHolder getInstance() {
        if (instance == null) instance = new BookHolder();
        return instance;
    }

    public ObservableList<Book> getBookList() {
        return bookList;

    }

    //endregion
}
