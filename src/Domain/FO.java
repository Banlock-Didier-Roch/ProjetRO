package Domain;

import java.util.ArrayList;

public class FO {

    private ArrayList<Double> coefs;

    public FO() {

        coefs = new ArrayList<>();

    }

    public FO(ArrayList<Double> coeffs) {
        this.coefs = coeffs;
    }

    public ArrayList<Double> getCoefs() {
        return coefs;
    }

    public void setCoefs(ArrayList<Double> coefs) {
        this.coefs = coefs;
    }
}
