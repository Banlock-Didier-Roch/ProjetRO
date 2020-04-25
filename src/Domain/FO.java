package Domain;

import java.util.ArrayList;

public class FO {

    private ArrayList<Integer> coefs;

    public FO() {

        coefs = new ArrayList<>();

    }

    public FO(ArrayList<Integer> coeffs) {
        this.coefs = coeffs;
    }

    public ArrayList<Integer> getCoefs() {
        return coefs;
    }

    public void setCoefs(ArrayList<Integer> coefs) {
        this.coefs = coefs;
    }
}
