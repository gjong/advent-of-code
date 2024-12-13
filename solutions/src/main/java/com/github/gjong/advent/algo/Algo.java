package com.github.gjong.advent.algo;

public class Algo {
    private Algo() {
    }

    /**
     * This method computes the least common multiple of two numbers.
     */
    public static long lcm(long a, long b) {
        // compute the LCM of a and b
        return Math.abs(a * b) / gcd(a, b);
    }

    /**
     * This method computes the greatest common divisor of two numbers.
     */
    public static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    /**
     * Calculates the determinant of a 2x2 matrix represented by the values a, b, c, and d.
     *
     * @param a the value in the top-left position of the matrix
     * @param b the value in the top-right position of the matrix
     * @param c the value in the bottom-left position of the matrix
     * @param d the value in the bottom-right position of the matrix
     * @return the determinant of the matrix
     */
    public static long determinant(long a, long b, long c, long d) {
        return (a * b) - (c * d);
    }
}
