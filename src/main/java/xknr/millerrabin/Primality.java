package xknr.millerrabin;

import java.math.BigInteger;
import static xknr.euler.util.BigInt.*;

public class Primality
{
  private static final MillerRabin32 mr32 = new MillerRabin32();
  private static final MillerRabin64Limited mr64lim = new MillerRabin64Limited();
  private static final MillerRabin64 mr64 = new MillerRabin64();
  private static final MillerRabinBig mrbig = new MillerRabinBig();
  
  private static final BigInteger LONG_MAX = B(Long.MAX_VALUE);
  
  public static boolean isPrime(int n) {
    return mr32.isPrime(n);
  }
  
  public static boolean isPrime(long n) {
    if (n <= MillerRabin32.ARG_LIMIT) {
      return mr32.isPrime((int)n);
    } else if (n < MillerRabin64Limited.ARG_LIMIT) { 
      return mr64lim.isPrime(n);
    } else {  
      return mr64.isPrime(n);
    }
  }
  
  public static boolean isPrime(BigInteger n) {
    if (n.compareTo(LONG_MAX) <= 0) {
      return isPrime(n.longValueExact());
    } else if (n.compareTo(MillerRabinBig.ARG_LIMIT) <= 0) { 
      return mrbig.isPrime(n);
    } else { 
      throw new UnsupportedOperationException(
        String.format("n > %s not supported.", MillerRabinBig.ARG_LIMIT));
    }
  }
}
