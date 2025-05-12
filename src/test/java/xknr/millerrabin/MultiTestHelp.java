package xknr.millerrabin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.stream.Stream;

import java.util.function.Predicate;

public class MultiTestHelp 
{
  // Instances of the MillerRabin classes
  private static final MillerRabin32 millerRabin32 = new MillerRabin32();
  private static final MillerRabin64Limited millerRabin64Limited = new MillerRabin64Limited();
  private static final MillerRabin64 millerRabin64 = new MillerRabin64();
  private static final MillerRabinBig millerRabinBig = new MillerRabinBig();

  public final static Stream<MethodProvider> primeMethodsGeneric(boolean has1, boolean has2, boolean has3, boolean has4) {
    List<MethodProvider> li = new ArrayList<MethodProvider>();
    if (has1)li.add(MethodProvider.of("MillerRabin1", (IntPredicate)millerRabin32::isPrime));
    if (has2)li.add(MethodProvider.of("MillerRabin2", (LongPredicate)millerRabin64Limited::isPrime));
    if (has3)li.add(MethodProvider.of("MillerRabin3", (LongPredicate)millerRabin64::isPrime));
    if (has4)li.add(MethodProvider.of("MillerRabinBig", (Predicate<BigInteger>)millerRabinBig::isPrime));
    return li.stream();
  }
  
  // MethodProvider class to hold method names and lambda expressions
  public static class MethodProvider {
      private final String methodName;
      private final Object method;  // Accepting either IntPredicate, LongPredicate, or BigIntegerPredicate

      private MethodProvider(String methodName, Object method) {
          this.methodName = methodName;
          this.method = method;
      }

      public static MethodProvider of(String methodName, Object method) {
          return new MethodProvider(methodName, method);
      }

      public boolean isPrime(long n) {
          if (method instanceof IntPredicate) {
              return ((IntPredicate) method).test((int)n);  // Coercing `long` to `int`
          } else if (method instanceof LongPredicate) {
              return ((LongPredicate) method).test(n); 
          } else if (method instanceof Predicate) {
              return ((Predicate<BigInteger>) method).test(BigInteger.valueOf(n)); // Converting long to BigInteger
          } else {
              throw new UnsupportedOperationException("Unsupported method type");
          }
      }

      public String getMethodName() {
          return methodName;
      }
  }


}
