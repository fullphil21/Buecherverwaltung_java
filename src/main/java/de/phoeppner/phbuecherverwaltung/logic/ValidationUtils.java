package de.phoeppner.phbuecherverwaltung.logic;

import de.phoeppner.phbuecherverwaltung.gui.MasterController;
import de.phoeppner.phbuecherverwaltung.model.Book;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.time.LocalDate;

/**
 * Hilfsklasse, die Methoden für die Validierung der Eingaben zur Verfügung stellt.
 */

public class ValidationUtils {
    public static final String CSS_MARK_TEXTFIELD = "-fx-background-color: #ffbc6d; -fx-border-color: red;";
    public static final String CSS_RESET_TEXTFIELD = "";
    public static final String EMPTY_STRING = "";
    public static final String STARTS_WITH_YEAR_VALUE = "0";
    public static final String STARTS_WITH_PAGES_VALUE = "0";
    public static final int VALID_PAGES_LENGTH = 4;
    public static final int VALID_ISBN_LENGTH = 13;
    public static final int VALID_YEAR_LENGTH = 4;
    public static final int POSITIVE_VALUE = 0;
    public static final String REGEX_ONLY_DIGITS = "\\d*";
    public static final String REGEX_FIND_DASHES = "[\\s-]";
    public static final String REGEX_NUMBERS_AND_DASHES = "[0-9-]*";
    public static final String REGEX_VALID_ISBN_INPUT = ".*\\d{13,}.*";
    public static final String REGEX_VALID_DIGIT_AMOUNT = "\\d{13}";
    public static final String REGEX_VALID_PAGES_INPUT = "[1-9]\\d{0,3}";
    //region Konstanten
    //endregion

    //region Attribute
    private final MasterController masterController;
    //endregion

    //region Konstruktoren
    public ValidationUtils(MasterController masterController) {
        this.masterController = masterController;
    }
    //endregion

    //region Methoden
    public void checkFieldsAndEnableButton(ButtonType buttonType, Boolean isDisabled) {

        Dialog<Book> dialog = masterController.getDialog();
        TextField isbnTextField = masterController.getIsbnTextField();
        TextField titleTextField = masterController.getTitleTextField();
        TextField authorTextField = masterController.getAuthorTextField();
        TextField yearTextField = masterController.getYearTextField();
        TextField pagesTextField = masterController.getPagesTextField();


        Node updateButton = dialog.getDialogPane().lookupButton(buttonType);
        updateButton.setDisable(isDisabled);

        isbnTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValidIsbn = isValidISBN(newValue.trim());
            updateButton.setDisable(!isValidIsbn || newValue.trim().isEmpty()
                    || titleTextField.getText().trim().isEmpty() || authorTextField.getText().trim().isEmpty()
                    || yearTextField.getText().trim().isEmpty() || pagesTextField.getText().trim().isEmpty());


            if (!isValidIsbn) {
                String formattedValue = newValue.replaceAll(REGEX_FIND_DASHES, EMPTY_STRING);
                if (formattedValue.length() >= VALID_ISBN_LENGTH || !newValue.matches(REGEX_NUMBERS_AND_DASHES)) {
                    isbnTextField.setStyle(CSS_MARK_TEXTFIELD);
                } else {
                    isbnTextField.setStyle(CSS_RESET_TEXTFIELD);
                }
            } else {
                isbnTextField.setStyle(CSS_RESET_TEXTFIELD);
            }
        });

        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || isbnTextField.getText().trim().isEmpty()
                    || authorTextField.getText().trim().isEmpty() || yearTextField.getText().trim().isEmpty()
                    || pagesTextField.getText().trim().isEmpty());
        });

        authorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || isbnTextField.getText().trim().isEmpty()
                    || titleTextField.getText().trim().isEmpty() || yearTextField.getText().trim().isEmpty()
                    || pagesTextField.getText().trim().isEmpty());
        });

        yearTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValidIsbn = isValidISBN(isbnTextField.getText());
            boolean isValidYear = isValidYear(newValue.trim());

            updateButton.setDisable(!isValidYear || !isValidIsbn || newValue.trim().isEmpty()
                    || isbnTextField.getText().trim().isEmpty() || titleTextField.getText().trim().isEmpty()
                    || authorTextField.getText().trim().isEmpty() || pagesTextField.getText().trim().isEmpty());

            if (!isValidYear && newValue.length() >= 4 || !newValue.matches(REGEX_ONLY_DIGITS)
                || newValue.startsWith(STARTS_WITH_YEAR_VALUE)) {
                yearTextField.setStyle(CSS_MARK_TEXTFIELD);
            } else {
                yearTextField.setStyle(CSS_RESET_TEXTFIELD);
            }
        });

        pagesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValidIsbn = isValidISBN(isbnTextField.getText());
            boolean isValidYear = isValidYear(yearTextField.getText());
            boolean isValidPages = isValidPages(newValue.trim());
            updateButton.setDisable(!isValidPages || !isValidIsbn || !isValidYear || newValue.trim().isEmpty()
                    || isbnTextField.getText().trim().isEmpty() || titleTextField.getText().trim().isEmpty()
                    || authorTextField.getText().trim().isEmpty() || yearTextField.getText().trim().isEmpty());

            if (!isValidPages && newValue.length() > VALID_PAGES_LENGTH || !newValue.matches(REGEX_ONLY_DIGITS)
                    || newValue.startsWith(STARTS_WITH_PAGES_VALUE)) {
                pagesTextField.setStyle(CSS_MARK_TEXTFIELD);

            } else {
                pagesTextField.setStyle(CSS_RESET_TEXTFIELD);
            }
        });
    }

    private boolean isValidISBN(String isbn) {
        String formattedString = isbn.replaceAll(REGEX_FIND_DASHES, EMPTY_STRING);
        if (!formattedString.matches(REGEX_VALID_ISBN_INPUT)) {
            return false;
        }
        if (isbn.length() >= VALID_ISBN_LENGTH) {
            String formattedISBN = isbn.replaceAll(REGEX_FIND_DASHES, EMPTY_STRING);
            return formattedISBN.matches(REGEX_VALID_DIGIT_AMOUNT);
        }
        return false;
    }

    private boolean isValidYear(String year) {
        String trimmedYear = year.trim();
        if (trimmedYear.length() != VALID_YEAR_LENGTH) {
            return false;
        }

        if (trimmedYear.startsWith(STARTS_WITH_YEAR_VALUE)) {
            return false;
        }

        int yearValue;
        try {
            yearValue = Integer.parseInt(trimmedYear);
        } catch (NumberFormatException e) {
            return false;
        }

        int currentYear = LocalDate.now().getYear();
        return yearValue <= currentYear;
    }

    private boolean isValidPages(String pages) {
        String trimmedPages = pages.trim();
        if (!trimmedPages.matches(REGEX_VALID_PAGES_INPUT)) {
            return false;
        }

        try {
            int pagesValue = Integer.parseInt(pages);
            return pagesValue >= POSITIVE_VALUE && pages.length() <= VALID_PAGES_LENGTH;
        } catch (NumberFormatException e) {
            return false;
        }
    //endregion
    }
}
