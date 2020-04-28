package Domain;

import java.util.ArrayList;

public class Contraintes {

    private ArrayList<Integer> coefs;
    private int valeur;

    public Contraintes() {

        coefs = new ArrayList<>();

    }

    public Contraintes(ArrayList<Integer> coeffs) {
        this.coefs = coeffs;
    }

    public ArrayList<Integer> getCoefs() {
        return coefs;
    }

    public void setCoefs(ArrayList<Integer> coefs) {
        this.coefs = coefs;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
}
