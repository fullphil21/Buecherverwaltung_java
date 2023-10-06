module de.phoeppner.phbuecherverwaltung {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mariadb.jdbc;
    requires java.sql;

    opens de.phoeppner.phbuecherverwaltung to javafx.fxml;
    exports de.phoeppner.phbuecherverwaltung;
    exports de.phoeppner.phbuecherverwaltung.gui;
    exports de.phoeppner.phbuecherverwaltung.model;
    opens de.phoeppner.phbuecherverwaltung.gui to javafx.fxml;
    opens de.phoeppner.phbuecherverwaltung.model to javafx.base;
}