package xknr.millerrabin;

import static xknr.util.BigInt.*;

import java.math.BigInteger;
import java.util.function.Predicate;

/**
 * MillerRabin32 is best when n < 2^31
 * MillerRabin64Limited is best when n < 3_037_001_057L
 * MillerRabin64 is best for n < 2^32
 * Above that, MillerRabinBig is faster. 
 */
public class Benchmark 
{
  public long dummyResult = 0;

  private MillerRabin32 mr32 = new MillerRabin32();     
  private MillerRabin64Limited mr64lim = new MillerRabin64Limited();
  private MillerRabin64 mr64 = new MillerRabin64();
  private MillerRabinBig mrbig = new MillerRabinBig();

  public static void main(String[] args) {
      BigInteger beg1 = B0;
      BigInteger lim1 = B(100_000); 

      BigInteger lim2 =  B(MillerRabin64Limited.ARG_LIMIT);
      BigInteger beg2 = lim2.subtract(B(100_000));

      BigInteger beg3a = B(4_200_000_000L);
      BigInteger lim3a = beg3a.add(B(100_000)); 

      BigInteger lim3b = B(1000_000_000L * 1000_000_000L);
      BigInteger beg3b = lim3b.subtract(B(100_000));

      BigInteger lim4 = B(Long.MAX_VALUE).multiply(B(15));
      BigInteger beg4 = lim4.subtract(B(100_000));

      BigInteger lim5 = B(Long.MAX_VALUE).multiply(B(100_000));
      BigInteger beg5 = lim5.subtract(B(100_000));

      Benchmark bench = new Benchmark();
      
      bench.benchmarkGeneric("Benchmark 1", beg1, lim1, true);
      bench.benchmarkGeneric("Benchmark 2", beg2, lim2, true);
      bench.benchmarkGeneric("Benchmark 3 a", beg3a, lim3a, true);
      bench.benchmarkGeneric("Benchmark 3 b", beg3b, lim3b, true);
      bench.benchmarkGeneric("Benchmark 4", beg4, lim4, true);
      bench.benchmarkGeneric("Benchmark 5", beg5, lim5, true);
  }
  
  public void benchmarkGeneric(String name, BigInteger beg, BigInteger limit, boolean doProb) {
    int CERT = 20;
    System.out.format("---\n%s [%d, %d]\n", name, beg, limit);

    if (beg.compareTo(B(Long.MAX_VALUE)) <= 0) {
      BigInteger l = limit.min(B(Long.MAX_VALUE));      
      //System.out.format("verify long (%d %d)\n", beg, l);
      verifyRange(beg, l, i -> mr64.isPrime(i.longValueExact()), i -> mrbig.isPrime(i));
      verifyRange(beg, l, i -> mr64.isPrime(i.longValueExact()), i -> i.isProbablePrime(CERT));
    }

    if (limit.compareTo(B(Long.MAX_VALUE)) > 0) {
      BigInteger b = beg.max(B(Long.MAX_VALUE).add(B1));      
      //System.out.format("verify big  (%d %d)\n", b, limit);
      verifyRange(b, limit, i -> mrbig.isPrime(i), i -> i.isProbablePrime(CERT));
    }

    if (limit.compareTo(B(MillerRabin32.ARG_LIMIT)) <= 0) {
      System.out.format("int version     : %.4f ns/check\n", measure(beg, limit, i -> mr32.isPrime(i.intValueExact())));
    }

    if (limit.compareTo(B(MillerRabin64Limited.ARG_LIMIT)) < 0) {
      System.out.format("long version    : %.4f ns/check\n", measure(beg, limit, i -> mr64lim.isPrime(i.longValueExact())));
    }
    
    if (limit.compareTo(B(MillerRabin64.ARG_LIMIT)) <= 0) {
      System.out.format("long with BigInt: %.4f ns/check\n", measure(beg, limit, i -> mr64.isPrime(i.longValueExact())));
    }

    if (limit.compareTo(MillerRabinBig.ARG_LIMIT) <= 0) {
      System.out.format("BigInt version  : %.4f ns/check\n", measure(beg, limit, i -> mrbig.isPrime(i)));
    }
    
    if (doProb) {
      System.out.format("isProbablePrime : %.4f ns/check\n", measure(beg, limit, i -> i.isProbablePrime(CERT)));
    }
  }
  
  /**
   * Verify that the given two primality check functions give the same results for all numbers in the given range.
   * @param beg the start of the range
   * @param end the end of the range
   * @param isPrime the first primality check to verify
   * @param isPrime2 the second primality check to verify
   */
  public void verifyRange(BigInteger beg, BigInteger end, Predicate<BigInteger> isPrime1, Predicate<BigInteger> isPrime2) {
    for(BigInteger n = beg; n.compareTo(end) < 0; n = n.add(B1)) {
      verify(isPrime1.test(n), isPrime2.test(n));
    }
  }

  /**
   * Throws a RuntimeException if a != b. This is a simple way to verify
   * that two boolean values are equal, since the JUnit assert methods are
   * not directly accessible from this class.
   * @param a first boolean value
   * @param b second boolean value
   */
 
  public static void verify(boolean a, boolean b) {
    if (a != b) {
      throw new RuntimeException();
    }
  }
  
  
  /**
   * Measure the time taken by the given primality checker function over the given range,
   * and return the average time per primality check in nanoseconds.
   * 
   * @param beg the start of the range
   * @param end the end of the range
   * @param isPrime the primality check to measure
   * @return the average time per primality check in nanoseconds
   */
  private double measure(BigInteger beg, BigInteger end, Predicate<BigInteger> isPrime) {
    if (beg.compareTo(end) > 0)
      throw new IllegalArgumentException("beg > limit");

    double result = 0;
    long start = System.nanoTime();

    long MILLISECOND_IN_NANOS = 1000_000L;

    int it;
    for(it = 1; ; ++it) {
      for(BigInteger n = beg; n.compareTo(end) <= 0; n = n.add(B1)) {
        boolean testResult = isPrime.test(n);
        dummyResult += testResult ? 1 : 0; // Consume result.
      }

      result = System.nanoTime() - start;
      if (it > 1 && result > 250 * MILLISECOND_IN_NANOS) {
        break;
      }        
    }

    result /= it;
    result /= end.subtract(beg).longValueExact();

    return result;
  }

}

