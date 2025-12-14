package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.gjong.advent.algo.Algo.*;
import static java.lang.Math.ceilDiv;
import static java.lang.Math.floorDiv;

@Day(year = 2025, day = 2, name = "Gift Shop")
public class Day02 implements DaySolver {

    private record Range(long lower, long upper) {
    }

    private final Validator validator;
    private final InputLoader inputLoader;

    private List<Range> ranges;

    public Day02(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
        ranges = new ArrayList<>();
    }

    @Override
    public void part1() {
        long total = ranges.stream()
                .map(r -> sumInvalidIdsInRange(r.lower, r.upper))
                .reduce(Long::sum)
                .orElseThrow();

        validator.part1(total);
    }

    @Override
    public void part2() {
        long total = ranges.stream()
                .map(r -> sumInvalidInRange(r.lower, r.upper))
                .reduce(BigInteger::add)
                .orElseThrow()
                .longValue();

        validator.part2(total);
    }

    @Override
    public void readInput() {
        ranges.clear();
        for (String range : inputLoader.split(",")) {
            String[] bounds = range.trim().split("-");
            ranges.add(new Range(Long.parseLong(bounds[0]), Long.parseLong(bounds[1])));
        }
    }

    private long sumInvalidIdsInRange(long lower, long upper) {
        BigInteger sum = BigInteger.ZERO;
        for (int k = 1; k <= 9; k++) {
            long pow10k = (long)Math.pow(10, k);
            long multiplier = pow10k + 1L;   // number = x * (10^k + 1)

            long xMinDigits = pow10k / 10;   // smallest k-digit number
            long xMaxDigits = pow10k - 1;    // largest  k-digit number

            // We need x * multiplier in [lower, upper]
            long xFromRange = ceilDivPositive(lower, multiplier);
            long xToRange = floorDivPositive(upper, multiplier);

            long xFrom = Math.max(xMinDigits, xFromRange);
            long xTo = Math.min(xMaxDigits, xToRange);

            if (xFrom > xTo) continue;

            // Sum_{x=xFrom..xTo} (x * multiplier) = multiplier * Sum_{x=xFrom..xTo} x
            BigInteger count = BigInteger.valueOf(xTo - xFrom + 1L);
            BigInteger a = BigInteger.valueOf(xFrom);
            BigInteger b = BigInteger.valueOf(xTo);

            // arithmetic series sum: (a + b) * count / 2
            BigInteger sumX = a.add(b).multiply(count).divide(BigInteger.TWO);
            BigInteger m = BigInteger.valueOf(multiplier);

            sum = sum.add(m.multiply(sumX));
        }
        return sum.longValue();
    }

    private static BigInteger sumInvalidInRange(long lower, long upper) {
        if (upper < lower) return BigInteger.ZERO;

        BigInteger total = BigInteger.ZERO;

        int minLen = digits(lower);
        int maxLen = digits(upper);

        for (int n = minLen; n <= maxLen; n++) {
            long lo = Math.max(lower, (long) Math.pow(10, n - 1));
            long hi = Math.min(upper, (long) Math.pow(10, n) - 1);
            if (lo > hi) continue;

            total = total.add(sumInvalidFixedLength(n, lo, hi));
        }

        return total;
    }

    private static BigInteger sumInvalidFixedLength(int n, long lo, long hi) {
        List<Integer> ps = divisors(n); // sorted
        BigInteger[] periodSum = new BigInteger[n + 1];
        BigInteger[] minSum = new BigInteger[n + 1];
        for (int i = 0; i <= n; i++) {
            periodSum[i] = BigInteger.ZERO;
            minSum[i] = BigInteger.ZERO;
        }

        // Compute sums for "has period p" (not minimal yet)
        for (int p : ps) {
            if (p == n) continue;
            periodSum[p] = sumWithPeriod(n, p, lo, hi);
        }

        // Turn that into "has minimal period p" by subtracting smaller divisor contributions
        for (int p : ps) {
            if (p == n) continue;

            BigInteger s = periodSum[p];
            for (int d : ps) {
                if (d >= p) break;
                if (p % d == 0) {
                    s = s.subtract(minSum[d]);
                }
            }
            minSum[p] = s;
        }

        BigInteger total = BigInteger.ZERO;
        for (int p : ps) {
            if (p == n) continue;
            total = total.add(minSum[p]);
        }
        return total;
    }

    private static BigInteger sumWithPeriod(int n, int p, long lo, long hi) {
        int repeats = n / p;
        long m = repeatMultiplier(p, repeats);

        long xMin = (long) Math.pow(10, p - 1);
        long xMax = (long) Math.pow(10, p) - 1;

        long from = Math.max(xMin, ceilDiv(lo, m));
        long to = Math.min(xMax, floorDiv(hi, m));
        if (from > to) return BigInteger.ZERO;

        BigInteger count = BigInteger.valueOf(to - from + 1L);
        BigInteger a = BigInteger.valueOf(from);
        BigInteger b = BigInteger.valueOf(to);

        BigInteger sumX = a.add(b).multiply(count).divide(BigInteger.TWO);
        return BigInteger.valueOf(m).multiply(sumX);
    }

    private static long repeatMultiplier(int p, int repeats) {
        // m = 1 + 10^p + 10^(2p) + ... + 10^((repeats-1)p)
        long tenPowP = (long) Math.pow(10, p);
        long m = 1L;
        long term = 1L;
        for (int i = 1; i < repeats; i++) {
            term *= tenPowP;
            m += term;
        }
        return m;
    }

    private static List<Integer> divisors(int x) {
        List<Integer> ds = new ArrayList<>();
        for (int d = 1; (long) d * d <= x; d++) {
            if (x % d == 0) {
                ds.add(d);
                int other = x / d;
                if (other != d) ds.add(other);
            }
        }
        Collections.sort(ds);
        return ds;
    }

    private static int digits(long x) {
        int d = 1;
        while (x >= 10) {
            x /= 10;
            d++;
        }
        return d;
    }
}
