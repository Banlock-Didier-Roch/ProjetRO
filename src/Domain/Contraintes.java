package Domain;

import java.util.ArrayList;

public class Contraintes {

    private ArrayList<Integer> coefs;

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
}
