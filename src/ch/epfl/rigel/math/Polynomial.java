package ch.epfl.rigel.math;

/**
 * @author Mohamed Kamil MELLOUK
 * 18.02.20
 */
public final class Polynomial {

    private double[] coefficients;

    /**
     * constructor of the polynomial
     *
     * @param coefficients  the array containing all coefficients
     */
    private Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * returning a polynomial given its coefficients
     *
     * @param coefficientN  the highest coefficient
     * @param coefficients  the rest of coefficients
     * @return a polynomial or throws an exception if the highest coefficient is 0
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        if(coefficientN == 0) {
            throw new IllegalArgumentException();
        } else {
            // create new array used to contain all coefficients
            double[] coefficientsArray = new double[coefficients.length + 1];
            // place the coefficients in the specific array
            coefficientsArray[0] = coefficientN;
            System.arraycopy(coefficients, 0, coefficientsArray, 1, coefficients.length);
            // return the related polynomial
            return new Polynomial(coefficientsArray);
        }
    }

    public double at(double x) {
        return 0;
    }

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
