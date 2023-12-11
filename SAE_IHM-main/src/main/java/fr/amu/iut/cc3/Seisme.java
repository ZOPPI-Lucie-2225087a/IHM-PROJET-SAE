package fr.amu.iut.cc3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Seisme {
    private String identifiant;
    private String date;

    private String heure;
    private String nom;
    private String epicentre;
    private String choc;
    private String latitude;
    private String longitude;
    private String intensite;
    private String qualite;

    // Constructeur, getters et setters
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * @param identifiant
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }
    public String getHeure() {
        return heure;
    }

    /**
     * @param heure
     */
    public void setHeure(String heure) {
        this.heure = heure;
    }
    public String getNom() {
        return nom;
    }

    /**
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return
     */
    public String getEpicentre() {
        return epicentre;
    }

    /**
     * @param epicentre
     */
    public void setEpicentre(String epicentre) {
        this.epicentre = epicentre;
    }
    public String getChoc() {
        return choc;
    }

    /**
     * @param choc
     */
    public void setChoc(String choc) {
        this.choc = choc;
    }

    /**
     * @return
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getIntensite() {
        return intensite;
    }

    /**
     * @param intensite
     */
    public void setIntensite(String intensite) {
        this.intensite = intensite;
    }

    /**
     * @return
     */
    public String getQualite() {
        return qualite;
    }

    /**
     * @param qualite
     */
    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    /**
     * @return
     */
    public static List<Seisme> getSeismes() {
        return seismes;
    }


    public static List<Seisme> seismes = new ArrayList<>();

 
    public static void main() {
        try (BufferedReader reader = new BufferedReader(new FileReader( "src/main/resources/fr/amu/iut/cc3/SisFrance_seismes_20230604151458.csv"))) {
            String line;
            boolean firstLine = true; // Pour ignorer la première ligne contenant les en-têtes
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                Seisme seisme = new Seisme();
                seisme.setIdentifiant(data[0]);
                seisme.setDate(data[1]);
                seisme.setHeure(data[2]);
                seisme.setNom(data[3]);
                seisme.setEpicentre(data[4]);
                seisme.setChoc(data[5]);
                seisme.setLatitude(data[8]);
                seisme.setLongitude(data[9]);
                seisme.setIntensite(data[10]);
                seisme.setQualite(data[11]);
                seismes.add(seisme);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
