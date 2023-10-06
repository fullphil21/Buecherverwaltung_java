package de.phoeppner.phbuecherverwaltung.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Modell-Klasse für Instanziierung von Büchern
 */
public class Book {
    //region Konstanten
    public static final int DEFAULT_INT_VALUE = -1;
    public static final String DEFAULT_STRING_VALUE = "noValueSetYet";

    //endregion

    //region Attribute
    private int id;
    private final StringProperty isbn;
    private final StringProperty title;
    private final StringProperty author;
    private final IntegerProperty year;
    private final IntegerProperty pages;

    //endregion


    //region Konstruktoren
    public Book() {
        isbn = new SimpleStringProperty(DEFAULT_STRING_VALUE);
        title = new SimpleStringProperty(DEFAULT_STRING_VALUE);
        author = new SimpleStringProperty(DEFAULT_STRING_VALUE);
        year = new SimpleIntegerProperty(DEFAULT_INT_VALUE);
        pages = new SimpleIntegerProperty(DEFAULT_INT_VALUE);
    }

    public Book(String isbn, String title, String author, int year, int pages) {
        this.isbn = new SimpleStringProperty(isbn);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.year = new SimpleIntegerProperty(year);
        this.pages = new SimpleIntegerProperty(pages);
    }

    //endregion

    //region Methoden
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn.get();
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public int getPages() {
        return pages.get();
    }

    public IntegerProperty pagesProperty() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages.set(pages);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn=" + isbn.get() + '\'' +
                ", title=" + title.get() + '\'' +
                ", author=" + author.get() + '\'' +
                ", year=" + year.get() +
                ", pages=" + pages.get() +
                '}';
    }

    //endregion
}
