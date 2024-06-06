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
}
