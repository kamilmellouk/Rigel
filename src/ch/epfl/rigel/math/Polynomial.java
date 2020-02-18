package ch.epfl.rigel.math;

/**
 * @author Mohamed Kamil MELLOUK
 * 18.02.20
 */
public final class Polynomial {

    private static double[] coefficients;

    private Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public static Polynomial of(double coefficientN, double... coeffs) {
        if(coefficientN == 0) throw new IllegalArgumentException();
        else {
            double[] coeffsTab = new double[coeffs.length+1];
            coeffsTab[0] = coefficientN;
            System.arraycopy(coeffs, 0, coeffsTab, 1, coeffs.length);
            return new Polynomial(coeffsTab);
        }
    }
}
