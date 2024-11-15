module ETI {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.base;

    opens DAMeti to javafx.fxml;
    opens Modelo to javafx.base; // Permitir acceso a JavaFX
    
    

    exports DAMeti;
}