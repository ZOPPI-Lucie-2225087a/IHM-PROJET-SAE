package fr.amu.iut.cc3;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.gluonhq.maps.MapLayer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.*;
import java.util.stream.Collectors;


public class HelloController {
    //importation des objet de FXML que l'on a besoin pour le programme
    @FXML
    private Button btnG;
    @FXML
    private ChoiceBox<String> choix;
    @FXML
    private ChoiceBox<String> choix2;
    @FXML
    private ChoiceBox<String> choix3;
    @FXML
    private TableView<Seisme> Tableau;
    @FXML
    private TableColumn<Seisme, String> nomColumn;
    @FXML
    private TableColumn<Seisme, String> dateColumn;
    @FXML
    private TableColumn<Seisme, String> EpicentreColumn;
    @FXML
    private MapView mapView;


    @FXML
    private void PlusDeGraph() {
        //change d'une fenetre à une autre
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("toile.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnG.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param event
     */
    @FXML
    private void rechercherSeismes(ActionEvent event) {
        String region = choix.getValue(); // Récupère la région sélectionnée
        String dateDep = choix2.getValue(); // Récupère la date de départ
        String dateArr = choix3.getValue(); // Récupère la date d'arrivée

        //Afficher tout les séismes dans le cas où un des trois critère n'est par remplit
        if ((choix.getValue()==null) || (choix2.getValue()==null) || (choix3.getValue()==null)){
            Tableau.setItems(FXCollections.observableArrayList(Seisme.getSeismes()));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            EpicentreColumn.setCellValueFactory(new PropertyValueFactory<>("epicentre"));

        }
        else {
            // Filtrer les séismes par rapport à la region et aux dates
            List<Seisme> seismesFiltres = Seisme.getSeismes().stream()
                    .filter(seisme -> seisme.getEpicentre().equals(region))
                    .filter(seisme -> seisme.getDate().compareTo(dateDep) >= 0 && seisme.getDate().compareTo(dateArr) <= 0)
                    .collect(Collectors.toList());

            // Afficher les séismes filtré
            Tableau.setItems(FXCollections.observableArrayList(seismesFiltres));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            EpicentreColumn.setCellValueFactory(new PropertyValueFactory<>("epicentre"));
        }
    }


    public void initialize() {
        mapView.setCenter(new MapPoint(46.603354, 1.888334)); // Coordonnées de la France

        mapView.setZoom(6); // Niveau de zoom pour afficher la France
        List<Seisme> seismes = Seisme.getSeismes();//Crée une liste avec tout les seismes
        Set<String> regions = new HashSet<>();//Prepare un Set qui contiendra les region
        List<String> annees = new ArrayList<>();//Prepar une list qui contiendra les date
        //ajoute les region et date
        for (Seisme seisme : seismes) {
            regions.add(seisme.getEpicentre());
            //Empeche les doublons de date, le Set de region le fait tout seul
            if (annees.contains(seisme.getDate().substring(0, 4))){
                continue;
            }
            else {
                annees.add(seisme.getDate().substring(0, 4));
            }
        }
        //donne les different parametre au choix, region, date de depart et date de fin
        choix.getItems().addAll(regions);
        choix2.getItems().addAll(annees);
        choix3.getItems().addAll(annees);


        //recupère pour chaque seisme ses coordonnées
        for (Seisme seisme : seismes) {
            String latitudeStr = seisme.getLatitude();
            String longitudeStr = seisme.getLongitude();
            //crée un point rouge sur la carte pour chaque seismes avec des coordonnées corect
            if (!latitudeStr.isEmpty() && !longitudeStr.isEmpty()) {
                double latitude = Double.parseDouble(latitudeStr);
                double longitude = Double.parseDouble(longitudeStr);
                MapPoint point = new MapPoint(latitude, longitude);
                MapLayer France = new customCircleMarkerLayer(point);
                mapView.addLayer(France);
            }
        }

    }

    /**
     * @param mouseEvent
     */
    //Empèche le deplacement de la carte mais laisse le zoom disponible
    public void recentrerLaCarte(MouseEvent mouseEvent) {
        mapView.setZoom(mapView.getZoom());
    }
}

