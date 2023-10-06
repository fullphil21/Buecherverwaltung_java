package de.phoeppner.phbuecherverwaltung.gui;

import de.phoeppner.phbuecherverwaltung.Main;
import de.phoeppner.phbuecherverwaltung.model.Book;
import de.phoeppner.phbuecherverwaltung.settings.AppSettings;
import de.phoeppner.phbuecherverwaltung.settings.AppTexts;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Hilfsklasse, die Methoden für die Erzeugung der Dialogbox im MasterController bereitstellt
 */
public class DialogUtils {
    //region Konstanten
    public static final String CSS_BUTTON_STYLE_FILE_NAME = "buttonStyle.css";
    public static final String CSS_CONFIRM_BUTTON_CLASS = "confirm-button";
    public static final String CSS_CANCEL_BUTTON_CLASS = "cancel-button";
    public static final String CSS_DIALOG_BACKGROUND_COLOR = "-fx-background-color: #d3cc03;";

    //endregion

    //region Attribute
    private final MasterController masterController;
    private TextField isbnTextField;
    private TextField titleTextField;
    private TextField authorTextField;
    private TextField yearTextField;
    private TextField pagesTextField;

    //endregion

    //region Konstruktoren
    public DialogUtils(MasterController masterController) {
        this.masterController = masterController;
    }

    //endregion

    //region Methoden
    private void initializeTextFields() {
       isbnTextField = masterController.getIsbnTextField();
       titleTextField = masterController.getTitleTextField();
       authorTextField = masterController.getAuthorTextField();
       yearTextField = masterController.getYearTextField();
       pagesTextField = masterController.getPagesTextField();
    }

    public GridPane createGridPane() {
        initializeTextFields();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(AppSettings.GRIDPANE_HGAP);
        gridPane.setVgap(AppSettings.GRIDPANE_VGAP);
        gridPane.setPadding(AppSettings.GRIDPANE_INSETS);

        gridPane.add(new Label(AppTexts.DIALOG_ISBN), 0, 0);
        gridPane.add(isbnTextField, 1, 0);
        gridPane.add(new Label(AppTexts.DIALOG_TITLE), 0, 1);
        gridPane.add(titleTextField, 1, 1);
        gridPane.add(new Label(AppTexts.DIALOG_AUTHOR), 0, 2);
        gridPane.add(authorTextField, 1, 2);
        gridPane.add(new Label(AppTexts.DIALOG_YEAR), 0, 3);
        gridPane.add(yearTextField, 1, 3);
        gridPane.add(new Label(AppTexts.DIALOG_PAGES), 0, 4);
        gridPane.add(pagesTextField, 1, 4);

        setToolTipsForTextFields();

        return gridPane;
    }

    public void createPromptTextsForTextFields() {
        initializeTextFields();
        isbnTextField.setPromptText(AppTexts.PROMPT_TEXT_ISBN);
        titleTextField.setPromptText(AppTexts.PROMPT_TEXT_TITLE);
        authorTextField.setPromptText(AppTexts.PROMPT_TEXT_AUTHOR);
        yearTextField.setPromptText(AppTexts.PROMPT_TEXT_YEAR);
        pagesTextField.setPromptText(AppTexts.PROMPT_TEXT_PAGES);
    }
    public void setDialogBoxStyle(ButtonType buttonType) {
        String cssPath = String.valueOf(Main.class.getResource(CSS_BUTTON_STYLE_FILE_NAME));

        Dialog<Book> dialog = masterController.getDialog();

        dialog.getDialogPane().setStyle(CSS_DIALOG_BACKGROUND_COLOR);

        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        Button confirmButton = (Button) dialog.getDialogPane().lookupButton(buttonType);

        confirmButton.getStylesheets().add(cssPath);
        confirmButton.getStyleClass().add(CSS_CONFIRM_BUTTON_CLASS);

        cancelButton.getStylesheets().add(cssPath);
        cancelButton.getStyleClass().add((CSS_CANCEL_BUTTON_CLASS));
    }

    private void setToolTipsForTextFields() {
        Tooltip isbnToolTip = new Tooltip(AppTexts.TOOLTIP_ISBN);
        isbnTextField.setTooltip(isbnToolTip);
        isbnToolTip.setShowDelay(Duration.millis(AppSettings.TOOLTIP_SHOW_DELAY));
        Tooltip titleToolTip = new Tooltip(AppTexts.TOOLTIP_TITLE);
        titleTextField.setTooltip(titleToolTip);
        titleToolTip.setShowDelay(Duration.millis(AppSettings.TOOLTIP_SHOW_DELAY));
        Tooltip authorToolTip = new Tooltip(AppTexts.TOOLTIP_AUTHOR);
        authorTextField.setTooltip(authorToolTip);
        authorToolTip.setShowDelay(Duration.millis(AppSettings.TOOLTIP_SHOW_DELAY));
        Tooltip yearToolTip = new Tooltip(AppTexts.TOOLTIP_YEAR);
        yearTextField.setTooltip(yearToolTip);
        yearToolTip.setShowDelay(Duration.millis(AppSettings.TOOLTIP_SHOW_DELAY));
        Tooltip pagesToolTip = new Tooltip(AppTexts.TOOLTIP_PAGES);
        pagesTextField.setTooltip(pagesToolTip);
        pagesToolTip.setShowDelay(Duration.millis(AppSettings.TOOLTIP_SHOW_DELAY));
    }

    //endregion
}

