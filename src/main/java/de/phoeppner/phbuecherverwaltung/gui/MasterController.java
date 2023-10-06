package de.phoeppner.phbuecherverwaltung.gui;

import de.phoeppner.phbuecherverwaltung.logic.BookHolder;
import de.phoeppner.phbuecherverwaltung.logic.ValidationUtils;
import de.phoeppner.phbuecherverwaltung.model.Book;
import de.phoeppner.phbuecherverwaltung.settings.AppSettings;
import de.phoeppner.phbuecherverwaltung.settings.AppTexts;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Enthält die Steuerlogik für die Master-View
 */
public class MasterController implements Initializable {
    //region Konstanten
    public static final String ATTRIBUTE_ISBN = "isbn";
    public static final String ATTRIBUTE_TITLE = "title";
    public static final String ATTRIBUTE_AUTHOR = "author";
    public static final String ATTRIBUTE_YEAR = "year";
    public static final String ATTRIBUTE_PAGES = "pages";

    //endregion

    //region Attribute
    private ValidationUtils validationUtils;
    private DialogUtils dialogUtils;
    private Dialog<Book> dialog;
    private FilteredList<Book> filteredList;
    private TextField isbnTextField;
    private TextField titleTextField;
    private TextField authorTextField;
    private TextField yearTextField;
    private TextField pagesTextField;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    Button btnUpdate;
    @FXML
    private TableView<Book> tvBooks;
    @FXML
    private TableColumn<Book, Integer> colIsbn;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, Integer> colYear;
    @FXML
    private TableColumn<Book, Integer> colPages;

    //endregion


    //region Konstruktoren
    //endregion

    //region Methoden

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colIsbn.setCellValueFactory(new PropertyValueFactory<>(ATTRIBUTE_ISBN));
        colTitle.setCellValueFactory(new PropertyValueFactory<>(ATTRIBUTE_TITLE));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>(ATTRIBUTE_AUTHOR));
        colYear.setCellValueFactory(new PropertyValueFactory<>(ATTRIBUTE_YEAR));
        colPages.setCellValueFactory(new PropertyValueFactory<>(ATTRIBUTE_PAGES));

        tvBooks.setItems(BookHolder.getInstance().getBookList());

        //Eventlistener für MouseClicks
        tvBooks.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY &&
                event.getClickCount() == AppSettings.OPEN_UPDATE_DIALOG_CLICK_COUNT) updateBook();
        });

        tvBooks.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) updateBook();
        });

        //Suche für Tableview implementieren
        filteredList = new FilteredList<>(BookHolder.getInstance().getBookList(), p -> true);

        searchTextField.setOnKeyReleased(keyEvent -> {
            filteredList.setPredicate(book -> {
                if (searchTextField.getText() == null || searchTextField.getText().isEmpty()) return true;

                String lowerCaseFilter = searchTextField.getText().toLowerCase();

                //Suche für Titel, Autor, ISBN und Jahr
                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(book.getYear()).contains(searchTextField.getText())) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Book> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tvBooks.comparatorProperty());
        tvBooks.setItems(sortedList);

        //Hilfsklassen initialisieren
        validationUtils = new ValidationUtils(this);
        dialogUtils = new DialogUtils(this);
    }

    @FXML
    private void handleButtonAction(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnDelete) deleteBook();
        if (actionEvent.getSource() == btnAdd) addBook();
        if (actionEvent.getSource() == btnUpdate) updateBook();
    }

    public void deleteBook() {

        Book selectedBook = tvBooks.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(AppTexts.ALERT_ERROR, null, AppTexts.ALERT_CHOOSE_BOOK);
            return;
        }
        BookHolder.getInstance().getBookList().remove(selectedBook);
    }


    public void addBook() {
        dialog = new Dialog<>();
        dialog.setTitle(AppTexts.DIALOG_ADD_TITLE);
        dialog.setHeaderText(null);
        dialog.setResizable(false);

        ButtonType buttonType = new ButtonType(AppTexts.DIALOG_ADD_BUTTON, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);


        isbnTextField = new TextField();
        titleTextField = new TextField();
        authorTextField = new TextField();
        yearTextField = new TextField();
        pagesTextField = new TextField();

        dialogUtils.createPromptTextsForTextFields();
        dialogUtils.setDialogBoxStyle(buttonType);

        dialog.getDialogPane().setContent(dialogUtils.createGridPane());

        Platform.runLater(isbnTextField::requestFocus);

        //Methode, die prüft, ob alle Textfelder befüllt sind
        validationUtils.checkFieldsAndEnableButton(buttonType, true);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {

                try {
                    return new Book(isbnTextField.getText(), titleTextField.getText(),
                            authorTextField.getText(), Integer.parseInt(yearTextField.getText()),
                            Integer.parseInt(pagesTextField.getText()));

                } catch (NumberFormatException e) {
                    showAlert(AppTexts.ALERT_ERROR, AppTexts.ALERT_INVALID_INPUT,
                            AppTexts.ALERT_INSERT_VALID_NUMBER);
                    return null;
                }
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();

        result.ifPresent(book -> {
            BookHolder.getInstance().getBookList().add(book);
        });
    }

    public void updateBook() {

        Book selectedBook = tvBooks.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert(AppTexts.ALERT_ERROR, null, AppTexts.ALERT_CHOOSE_BOOK);
            return;
        }

        dialog = new Dialog<>();
        dialog.setTitle(AppTexts.DIALOG_UPDATE_TITLE);
        dialog.setHeaderText(null);
        dialog.setResizable(false);

        ButtonType buttonType = new ButtonType(AppTexts.DIALOG_UPDATE_BUTTON, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);

        isbnTextField = new TextField(selectedBook.getIsbn());
        titleTextField = new TextField(selectedBook.getTitle());
        authorTextField = new TextField(selectedBook.getAuthor());
        yearTextField = new TextField(Integer.toString(selectedBook.getYear()));
        pagesTextField = new TextField(Integer.toString(selectedBook.getPages()));

        dialogUtils.createPromptTextsForTextFields();

        dialogUtils.setDialogBoxStyle(buttonType);

        dialog.getDialogPane().setContent(dialogUtils.createGridPane());

        Platform.runLater(isbnTextField::requestFocus);

        //Methode, die prüft, ob alle Textfelder befüllt sind
        validationUtils.checkFieldsAndEnableButton(buttonType, false);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {
                try {
                    return new Book(isbnTextField.getText(), titleTextField.getText(),
                            authorTextField.getText(), Integer.parseInt(yearTextField.getText()),
                            Integer.parseInt(pagesTextField.getText()));
                } catch (NumberFormatException e) {
                    showAlert(AppTexts.ALERT_ERROR, AppTexts.ALERT_INVALID_INPUT,
                            AppTexts.ALERT_INSERT_VALID_NUMBER);
                    return null;
                }
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();

        result.ifPresent(book -> {
            selectedBook.setIsbn(isbnTextField.getText());
            selectedBook.setTitle(titleTextField.getText());
            selectedBook.setAuthor(authorTextField.getText());
            selectedBook.setYear(Integer.parseInt(yearTextField.getText()));
            selectedBook.setPages(Integer.parseInt(pagesTextField.getText()));
        });

    }

    public void showAlert(String alertTitle, String alertHeader, String alertContent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContent);
        alert.showAndWait();
    }

    public Dialog<Book> getDialog() {
        return dialog;
    }

    public TextField getIsbnTextField() {
        return isbnTextField;
    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public TextField getAuthorTextField() {
        return authorTextField;
    }

    public TextField getYearTextField() {
        return yearTextField;
    }

    public TextField getPagesTextField() {
        return pagesTextField;
    }

    //endregion
}


