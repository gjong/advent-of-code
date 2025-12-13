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

    public static long floorDivPositive(long a, long b) {
        // assumes a >= 0 and b > 0
        return a / b;
    }

    public static long ceilDivPositive(long a, long b) {
        // assumes a >= 0 and b > 0
        return (a + b - 1L) / b;
    }

    /**
     * Möbius function mu(n) for n <= 18-ish (we only need up to 18).
     * Returns:
     *  1  if n is square-free with even number of primes,
     * -1  if n is square-free with odd number of primes,
     *  0  if n has a squared prime factor.
     */
    public static int mobius(int n) {
        int numPrimeFactors = 0;
        int x = n;

        for (int p = 2; p * p <= x; p++) {
            if (x % p != 0) continue;

            int count = 0;
            while (x % p == 0) {
                x /= p;
                count++;
                if (count > 1) return 0; // squared prime factor
            }
            numPrimeFactors++;
        }
        if (x > 1) numPrimeFactors++;

        return (numPrimeFactors % 2 == 0) ? 1 : -1;
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
