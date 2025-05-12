package xknr.millerrabin;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import xknr.millerrabin.MultiTestHelp.MethodProvider;

import xknr.euler.primes.FirstPrimes;

public class MillerRabinTest
{
  public final static Stream<MethodProvider> primeMethods1234() {
    return MultiTestHelp.primeMethodsGeneric(true, true, true, true);
  }
  public final static Stream<MethodProvider> primeMethods234() {
    return MultiTestHelp.primeMethodsGeneric(false, true, true, true);
  }
  public final static Stream<MethodProvider> primeMethods34() {
    return MultiTestHelp.primeMethodsGeneric(false, false, true, true);
  }
  
  public static final BigInteger b(String n) {
    return new BigInteger(n);
  }
  
  
  public static final long[] nums = {
      174440041L, 3657500101L, 88362852307L, 414507281407L, 2428095424619L, 
      4952019383323L, 12055296811267L, 17461204521323L, 28871271685163L, 
      53982894593057L, 75063692618249L, 119543903707171L, 156740126985437L, 
      180252380737439L, 222334565193649L,
      3657500101L, 88362852307L, 2428095424619L, 12055296811267L, 
      75063692618249L, 156740126985437L,
      3, 7, 11, 31, 37, 127, 131, 257, 521, 1031, 2053, 8191, 8209, 16411, 32771, 
      131071, 131101, 524287, 524309, 1048583, 2097169, 4194319, 8388617, 
      16777259, 33554467, 67108879, 134217757, 268435459, 536870923, 2147483647, 
      2147483659L, 4294967311L, 8589934609L, 17179869209L, 34359738421L, 68719476767L, 
      137438953481L, 274877906951L, 549755813911L, 1099511627791L, 2199023255579L, 
      4398046511119L, 8796093022237L, 17592186044423L, 35184372088891L, 70368744177679L, 
      140737488355333L, 281474976710677L, 562949953421381L, 1125899906842679L, 
      2251799813685269L, 4503599627370517L, 9007199254740997L, 18014398509482143L, 
      36028797018963971L, 72057594037928017L, 144115188075855881L, 288230376151711813L,
      576460752303423619L, 2305843009213693951L, 2305843009213693967L, 4611686018427388039L,      };
  
  public static final List<Long> nums2 = List.of(
      4611826755915743237L, 
      4611826755915743251L, 
      4611826755915743389L, 
      4611826755915743449L, 
      4611826755915743471L, 
      4611826755915743513L, 
      4611826755915743543L, 
      4611826755915743557L, 
      4611826755915743593L, 
      4611826755915743641L, 
      4611826755915743711L, 
      4611826755915743779L, 
      4611826755915743789L, 
      4611826755915743909L, 
      4611826755915744007L, 
      4611826755915744017L, 
      4611826755915744163L, 
      4611826755915744181L, 
      4611826755915744271L, 
      4611826755915744301L
  );
  public static final List<Long> nums3 = List.of(

      5764607523034235009L, 
      5764607523034235033L, 
      5764607523034235159L, 
      5764607523034235269L, 
      5764607523034235297L, 
      5764607523034235299L, 
      5764607523034235321L, 
      5764607523034235353L, 
      5764607523034235357L, 
      5764607523034235401L, 
      5764607523034235419L, 
      5764607523034235443L, 
      5764607523034235489L, 
      5764607523034235549L, 
      5764607523034235563L, 
      5764607523034235573L, 
      5764607523034235581L, 
      5764607523034235669L, 
      5764607523034235671L, 
      5764607523034235741L);
      
  public static final List<Long> nums4 = List.of(

      6917529027641081903L, 
      6917529027641081951L, 
      6917529027641081959L, 
      6917529027641082031L, 
      6917529027641082139L, 
      6917529027641082161L, 
      6917529027641082359L, 
      6917529027641082497L, 
      6917529027641082523L, 
      6917529027641082541L, 
      6917529027641082569L, 
      6917529027641082589L, 
      6917529027641082623L, 
      6917529027641082667L, 
      6917529027641082689L, 
      6917529027641082691L, 
      6917529027641082709L, 
      6917529027641082761L, 
      6917529027641082787L, 
      6917529027641082817L);  
  
  public static final BigInteger nums8[] = {  
      b("9223372036854775837"), b("18446744073709551629"), b("36893488147419103363"), 
      b("73786976294838206473"), b("147573952589676412931"), b("295147905179352825889"), 
      b("590295810358705651741"), b("1180591620717411303449"), b("2361183241434822606859"),
      b("4722366482869645213711"), b("9444732965739290427421"), b("18889465931478580854821"), 
      b("37778931862957161709601"), b("75557863725914323419151"), b("151115727451828646838283"),
      b("302231454903657293676551"), b("604462909807314587353111"),
      b("1208925819614629174706189"), b("2417851639229258349412369"),
      
      // Not confirmed
      /*
      b("4835703278458516698824713"), 
      b("9671406556917033397649483"),
      b("19342813113834066795298819"), b("38685626227668133590597803"),
      b("77371252455336267181195291"), b("154742504910672534362390567"),
      b("618970019642690137449562111"), b("618970019642690137449562141"),
      b("1237940039285380274899124357"), b("2475880078570760549798248507"),
      b("4951760157141521099596496921"), b("9903520314283042199192993897"),
      b("19807040628566084398385987713"), b("39614081257132168796771975177"), 
      b("79228162514264337593543950397"), b("158456325028528675187087900777"),
      b("316912650057057350374175801351"), b("633825300114114700748351602943") 
      */
  };

  @ParameterizedTest
  @MethodSource("primeMethods1234")
  public void miller(MethodProvider obj) {
      for (int i = 1; i < FirstPrimes.last(); i++) {
          boolean expected = FirstPrimes.PRIMES_SET.contains(i);
          boolean result = obj.isPrime(i);
          assertEquals(expected, result, 
            String.format("Failed for %s on %d", obj.getMethodName(), i));
      }
  }
  
  @ParameterizedTest
  @MethodSource("primeMethods234")
  public void someNumbers2(MethodProvider obj) {
    String msg = "Failed for " + obj.getMethodName();
    assertEquals(true, obj.isPrime(3036008557L),()->msg);
    assertEquals(true, obj.isPrime(3036008561L),()->msg);
    assertEquals(false, obj.isPrime(3036008563L),()->msg);
    assertEquals(false, obj.isPrime(3036008569L),()->msg);
    assertEquals(false, obj.isPrime(3036008571L),()->msg);
    assertEquals(false, obj.isPrime(3036008577L),()->msg);
    assertEquals(false, obj.isPrime(3036008579L),()->msg);
    assertEquals(false, obj.isPrime(3036008583L),()->msg);
    assertEquals(false, obj.isPrime(3036008589L),()->msg);
    assertEquals(false, obj.isPrime(3036008591L),()->msg);
    assertEquals(false, obj.isPrime(3036008597L),()->msg);
    assertEquals(false, obj.isPrime(3036008601L),()->msg);
    assertEquals(false, obj.isPrime(3036008607L),()->msg);
    assertEquals( true, obj.isPrime(3036008609L),()->msg);
  }

  @ParameterizedTest
  @MethodSource("primeMethods34")
  public void someNumbers3(MethodProvider obj) {
    String msg = "Failed for " + obj.getMethodName();
    assertEquals(true, obj.isPrime(854654140073L),()->msg);
    assertEquals(false, obj.isPrime(854654140079L),()->msg);
    assertEquals(false, obj.isPrime(854654140081L),()->msg);
    assertEquals(false, obj.isPrime(854654140087L),()->msg);
    assertEquals(false, obj.isPrime(854654140091L),()->msg);
    assertEquals(false, obj.isPrime(854654140093L),()->msg);
    assertEquals(false, obj.isPrime(854654140097L),()->msg);
    assertEquals(false, obj.isPrime(854654140099L),()->msg);
    assertEquals(false, obj.isPrime(854654140103L),()->msg);
    assertEquals(false, obj.isPrime(854654140109L),()->msg);
    assertEquals(false, obj.isPrime(854654140111L),()->msg);
    assertEquals(false, obj.isPrime(854654140117L),()->msg);
    assertEquals(false, obj.isPrime(854654140121L),()->msg);
    assertEquals(false, obj.isPrime(854654140123L),()->msg);
    assertEquals(false, obj.isPrime(854654140127L),()->msg);
    assertEquals(true, obj.isPrime(854654140129L),()->msg);
    
    assertEquals(false, obj.isPrime(3215031751L),()->msg);
    assertEquals(false, obj.isPrime(3215031753L),()->msg);

    // uses 5 witnesses:
    assertEquals(true, obj.isPrime(1000000016347L),()->msg);
    assertEquals(true, obj.isPrime(1000000016507L),()->msg);
    for(long i = 1000000016507L+1; i < 1000000016531L; i++) {
      assertEquals(false, obj.isPrime(i),()->msg);
      assertEquals(false, BigInteger.valueOf(i).isProbablePrime(50),()->msg);
    }
    assertEquals(true, obj.isPrime(1000000016531L),()->msg);
    
    // https://oeis.org/A058332
    assertEquals(true, obj.isPrime(9737333L),()->msg);
    assertEquals(true, obj.isPrime(174440041L),()->msg);
    assertEquals(true, obj.isPrime(3657500101L),()->msg);
    assertEquals(true, obj.isPrime(16123689073L),()->msg);
    assertEquals(true, obj.isPrime(88362852307L),()->msg);
    assertEquals(true, obj.isPrime(175650481151L),()->msg);
    assertEquals(true, obj.isPrime(414507281407L),()->msg);
    assertEquals(true, obj.isPrime(592821132889L),()->msg);
    assertEquals(true, obj.isPrime(963726515729L),()->msg);
    assertEquals(true, obj.isPrime(1765037224331L),()->msg);
    assertEquals(true, obj.isPrime(2428095424619L),()->msg);
    assertEquals(true, obj.isPrime(3809491708961L),()->msg);
    assertEquals(true, obj.isPrime(4952019383323L),()->msg);
    assertEquals(true, obj.isPrime(5669795882633L),()->msg);
    assertEquals(true, obj.isPrime(6947574946087L),()->msg);
    assertEquals(true, obj.isPrime(9163611272327L),()->msg);
    assertEquals(true, obj.isPrime(222334565193649L),()->msg);

    for(long n : nums) {
      assertEquals(true, obj.isPrime(n),()->String.format("failed %d", n));
      assertEquals(true, BigInteger.valueOf(n).isProbablePrime(50),()->msg);
    }

    testRange(nums2, obj);
    testRange(nums3, obj);
    testRange(nums4, obj);
  }
  
  @Test
  public void testMillerRabinBig() {
    MillerRabinBig mb = new MillerRabinBig();
    for(BigInteger n : nums8) {
      assertEquals(true, mb.isPrime(n),()->String.format("failed %d", n));
      assertEquals(true, n.isProbablePrime(50));
    }
    assertTrue(b("4835703278458516698824713").isProbablePrime(50));
  }

  private void testRange(List<Long> nums2, MethodProvider obj) {
    Set<Long> nums2set = new HashSet<Long>(nums2);
    for(long n = nums2.get(0); n <= nums2.get(nums2.size() - 1); n++) {
      boolean expected = nums2set.contains(n);
      assertEquals(expected, obj.isPrime(n),String.format("failed %d", n));
      assertEquals(expected, BigInteger.valueOf(n).isProbablePrime(50),
        String.format("isProbable failed %d", n));
    }
  }
  
}
