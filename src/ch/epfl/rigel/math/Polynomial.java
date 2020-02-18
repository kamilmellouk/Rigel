package ch.epfl.rigel.math;

/**
 * @author Mohamed Kamil MELLOUK
 * 18.02.20
 */
public final class Polynomial {

    private static double[] coefficients;

    // PAS FINI
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

    public double at(double x) {
        return 0;
    }
    // PAS FINI

    /**
     *
     * @param obj   the object
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final boolean equals(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
