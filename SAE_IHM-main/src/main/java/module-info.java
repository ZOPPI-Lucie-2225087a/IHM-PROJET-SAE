

module fr.amu.iut.cc3 {

    requires javafx.controls;

    requires javafx.fxml;
    requires com.gluonhq.maps;


    opens fr.amu.iut.cc3 to javafx.fxml;

    exports fr.amu.iut.cc3;
}

