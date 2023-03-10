module seals.c195task {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens seals.c195task.Models to javafx.graphics, javafx.fxml, javafx.base;
    exports seals.c195task;
    exports seals.c195task.Controllers;
    opens seals.c195task.Controllers to javafx.fxml;
}