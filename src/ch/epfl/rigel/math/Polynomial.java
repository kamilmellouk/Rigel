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
     * @param coefficients the array containing all coefficients
     */
    private Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * returning a polynomial given its coefficients
     *
     * @param coefficientN the highest coefficient
     * @param coefficients the rest of coefficients
     * @return a polynomial or throws an exception if the highest coefficient is 0
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        if (coefficientN == 0) {
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

    /**
     * @param x the given argument
     * @return the value of the polynomial for the given argument
     */
    public double at(double x) {
        // TODO: 19/02/2020 check if the method is correct
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result = result * x + coefficients[i];
        }
        return result;
    }

    @Override
    public String toString() {
        // TODO: 19/02/2020 check if there is a simple way to do this
        if(coefficients.length == 1) return Double.toString(coefficients[0]);
        StringBuilder textualRepresentation = new StringBuilder();
        for (int i = 0 ; i < coefficients.length ; i++) {
            if (i != coefficients.length - 1) {
                if (coefficients[i] != 0) {
                    if (coefficients[i] == 1 || coefficients[i] == -1) {
                        textualRepresentation.append(coefficients[i] < 0 ? "-" : "");
                    } else {
                        textualRepresentation.append(coefficients[i]);
                    }
                    textualRepresentation.append("x");
                    if (i != coefficients.length - 2) {
                        textualRepresentation.append("^" + (coefficients.length - 1 - i));
                    }
                }
                textualRepresentation.append(coefficients[i+1] > 0 ? "+" : "");
            } else {
                textualRepresentation.append(coefficients[i]);
            }
        }
        return textualRepresentation.toString();
    }

    /**
     * @param obj the object
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final boolean equals(Object obj) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @return nothing
     * @throws UnsupportedOperationException to guarantee that no subclass redefines the method
     */
    @Override
    public final int hashCode() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
