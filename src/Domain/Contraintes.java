package Domain;

import java.util.ArrayList;

public class Contraintes {

    private ArrayList<Double> coefs;
    private ArrayList<Double> coefs_ecart;
    private double valeur;

    public Contraintes() {

        coefs = new ArrayList<>();
        coefs_ecart = new ArrayList<>();

    }

    public Contraintes(ArrayList<Double> coefs, ArrayList<Double> coefs_ecart, double valeur) {
        this.coefs = coefs;
        this.coefs_ecart = coefs_ecart;
        this.valeur = valeur;
    }

    public Contraintes(ArrayList<Double> coeffs) {
        this.coefs = coeffs;
    }

    public ArrayList<Double> getCoefs() {
        return coefs;
    }

    public void setCoefs(ArrayList<Double> coefs) {
        this.coefs = coefs;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public ArrayList<Double> getCoefs_ecart() {
        return coefs_ecart;
    }

    public void setCoefs_ecart(ArrayList<Double> coefs_ecart) {
        this.coefs_ecart = coefs_ecart;
    }
}
