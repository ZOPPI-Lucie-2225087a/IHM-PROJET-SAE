package fr.amu.iut.cc3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.util.*;
import java.util.stream.Collectors;


import static fr.amu.iut.cc3.Seisme.seismes;

public class ToileController {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button retour;



    public void initialize() {

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();

        // Compte le nombre de séismes dans chaque tranche de 50 ans
        int startYear = 1600; // Année de départ
        int endYear = 2000; // Année de fin
        int interval = 50; // Intervalle de 50 ans

        for (int i = startYear; i <= endYear; i += interval) {
            int count = countSeismesInInterval(i, i + interval - 1);
            String label = i + "-" + (i + interval - 1);
            dataSeries.getData().add(new XYChart.Data<>(label, count));
        }





        Map<String, Integer> regionCountMap = countSeismesByRegion();

        XYChart.Series<String, Number> DataSeries = new XYChart.Series<>();

        // Ajoute les points de données à la série en utilisant les données de régions et leurs occurrences
        for (Map.Entry<String, Integer> entry : regionCountMap.entrySet()) {
            String region = entry.getKey();
            int count = entry.getValue();
            DataSeries.getData().add(new XYChart.Data<>(region, count));
        }







        // Ajoute la série de données au graphique
        lineChart.getData().add(dataSeries);
        lineChart.setLegendVisible(false);


        barChart.getData().add(DataSeries);
        barChart.setLegendVisible(false);


        ObservableList<PieChart.Data> pieChartData = createPieChartData();
        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(false);

    }

    /**
     * @param startYear
     * @param endYear
     * @return
     */
    private int countSeismesInInterval(int startYear, int endYear) {
        List<Seisme> seismes = Seisme.getSeismes();
        int count = 0;
        for (Seisme seisme : seismes) {
            int year = Integer.parseInt(seisme.getDate().substring(0, 4));
            if (year >= startYear && year <= endYear) {
                count++;
            }
        }
        return count;
    }


    /**
     * @return
     */
    private ObservableList<PieChart.Data> createPieChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Récupère les différentes qualités de séismes

        List<String> qualites = seismes.stream()
                .map(Seisme::getQualite)
                .distinct()
                .collect(Collectors.toList());

        // Compte le nombre de séismes pour chaque qualité

        for (String qualite : qualites) {
            long count = seismes.stream()
                    .filter(s -> s.getQualite().equals(qualite))
                    .count();
            pieChartData.add(new PieChart.Data(qualite, count));
        }

        return pieChartData;
    }


    /**
     * @return
     */
    private Map<String, Integer> countSeismesByRegion() {
        Map<String, Integer> regionCountMap = new HashMap<>();

        for (Seisme seisme : Seisme.getSeismes()) {
            String region = seisme.getEpicentre();

            // Vérifie si la région existe déjà dans la map
            if (regionCountMap.containsKey(region)) {
                // Récupérer le nombre actuel de séismes pour cette région
                int count = regionCountMap.get(region);
                // Incrémente le nombre de séismes pour cette région
                regionCountMap.put(region, count + 1);
            } else {
                // Ajoute la région à la map avec un compteur initialisé à 1
                regionCountMap.put(region, 1);
            }
        }

        return regionCountMap;
    }



        @FXML
        private void RetourCarte() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1280, 720);
                Stage stage = (Stage) retour.getScene().getWindow();
                stage.setTitle("Données sismique");
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
