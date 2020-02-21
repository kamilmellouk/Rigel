package ch.epfl.rigel.math;

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
     * @return the value of the polynomial for the given argument using Horner's formula
     */
    public double at(double x) {
        double result = 0;
        for (double coefficient : coefficients) {
            result = result * x + coefficient;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder textualRepresentation = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            // check if it is the last coefficient
            if (i != coefficients.length - 1) {
                // check if the coefficient is not null
                if (coefficients[i] != 0) {
                    // check if the coefficient is 1 or -1
                    if (Math.abs(coefficients[i]) == 1) {
                        // if it is the case, it just adds the sign
                        // (if the coefficient is 1, the sign + was already added) see line 77
                        textualRepresentation.append(coefficients[i] < 0 ? "-" : "");
                    } else {
                        // it adds the coefficient value
                        textualRepresentation.append(coefficients[i]);
                    }
                    // add the variable 'x'
                    textualRepresentation.append("x");
                    // add the power if it is not 1
                    if (i != coefficients.length - 2) {
                        textualRepresentation.append("^").append(coefficients.length - 1 - i);
                    }
                }
                // add the sign + if the next coefficient is positive
                // the sign - will be automatically written with the negative coefficient
                textualRepresentation.append(coefficients[i + 1] > 0 ? "+" : "");
            } else {
                // add the last coefficient
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
