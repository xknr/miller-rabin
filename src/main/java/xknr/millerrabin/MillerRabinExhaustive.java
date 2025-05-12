package xknr.millerrabin;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import xknr.euler.primes.SieveEratBitVector;

import static xknr.util.BigInt.*;

import java.util.function.Predicate;

/*
 * TODO use a conditional test for values not in sieve
 * warn if sieve size could be bigger
 * miller3MT can also use values much larger than can be done with a sieve and check against isprobableprime
 */

public class MillerRabinExhaustive {
  private static final long sieveMax = 10_000_000_000L;
  private static SieveEratBitVector sieve;

  private static final int THREAD_COUNT = 12;
  private static final long CHUNK_SIZE = 1000_000;

  private static final MillerRabin32 m32 = new MillerRabin32();
  private static final MillerRabin64Limited m64lim = new MillerRabin64Limited();
  private static final MillerRabin64 m64 = new MillerRabin64();
  private static final MillerRabinBig mbig = new MillerRabinBig();

  private static final Predicate<BigInteger> primalityTest32 = x -> m32.isPrime(x.intValueExact());
  private static final Predicate<BigInteger> primalityTestLim = x -> m64lim.isPrime(x.longValueExact());
  private static final Predicate<BigInteger> primalityTest64 = x -> m64.isPrime(x.longValueExact());
  private static final Predicate<BigInteger> primalityTestBig = x -> mbig.isPrime(x);
  private static final Predicate<BigInteger> primalityVerifySieve = x -> sieve.isPrime(x.longValueExact());

  private static final int CERTAINITY = 50;
  private static final Predicate<BigInteger> primalityVerifyProb = x -> x.isProbablePrime(CERTAINITY);  

  public static void main(String[] args) throws InterruptedException {
    new MillerRabinExhaustive().run();
  }

  public void run() {
    try {
      Predicate<BigInteger> primalityFunc = primalityTest64;
      testMillerMT(primalityFunc);
    } catch(InterruptedException e) {
      e.printStackTrace();
    } finally {
      sieve = null;
    }
  }

  public void testMillerMT(Predicate<BigInteger> primalityFunc) throws InterruptedException {

    Predicate<BigInteger> primalityVerify;

    BigInteger beg = B(9_000_000_000L);
    BigInteger end = B(sieveMax);

    if (primalityFunc == primalityTestBig) {
      primalityVerify = primalityVerifyProb;
    } else {
      prepareSieve();
      primalityVerify = primalityVerifySieve;
    }

    ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    nextChunkStart = beg;
    for (int t = 0; t < THREAD_COUNT; t++) {
      final int tid = t;
      executor.submit(() -> MillerRabinGenericMT(tid, THREAD_COUNT, end, primalityFunc, primalityVerify));
    }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    System.out.println("All tests completed.");
  }

  public void MillerRabinGenericMT(int tid, int threadCount, 
    BigInteger end, 
    Predicate<BigInteger> primalityFunc, 
    Predicate<BigInteger> primalityVerify)
  {
    while(true) {
      BigInteger start = getNextChunk();
      if (start.compareTo(end) >= 0)
        break;
      BigInteger lim = end.min(start.add(B(CHUNK_SIZE)));
      System.out.format("[%d] %d - %d\n", tid, start, lim);

      for (BigInteger i = start; i.compareTo(lim) < 0; i = i.add(B1)) {
        boolean expected = primalityVerify.test(i);
        boolean result = primalityFunc.test(i);

        if (expected != result) {
          String msg = String.format("[%d] Failed for MillerRabin32 i = %d on %b = %b", 
            tid, i, expected, result);
          System.err.println(msg);
          throw new RuntimeException(msg);
        }
      }      
    }
  }
  
  private volatile BigInteger nextChunkStart;

  private synchronized BigInteger getNextChunk() {
    BigInteger current = nextChunkStart;
    nextChunkStart = nextChunkStart.add(B(CHUNK_SIZE));
    return current;
  }
  
  private void prepareSieve() {
    if (sieve == null) {
      System.err.println("sieve start " + sieveMax);
      sieve = new SieveEratBitVector(sieveMax);
      System.err.println("sieve complete");
    }
  }
}
