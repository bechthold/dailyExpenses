module de.ibechthold.bidailyexpenses {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires org.mariadb.jdbc;
    requires java.sql;

    opens de.ibechthold.bidailyexpenses to javafx.fxml;
    exports de.ibechthold.bidailyexpenses;
    opens de.ibechthold.bidailyexpenses.gui to javafx.fxml;

    opens de.ibechthold.bidailyexpenses.model to javafx.base;
    opens de.ibechthold.bidailyexpenses.gui.tableview to javafx.fxml;
}