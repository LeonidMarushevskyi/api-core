package gov.ca.cwds.data.persistence.cms;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator.KeyDetail;
import gov.ca.cwds.rest.services.ServiceException;

/**
 * This JNI native library runs correctly on Linux Jenkins when libLZW.so and libstdc++.so.6 are
 * installed into /usr/local/lib/.
 * 
 * <p>
 * The library does build and run on OS X and Linux environments with current compilers installed.
 * </p>
 * 
 * <p>
 * The following JUnit test runs manually on the clone Jenkins server but not through Gradle on
 * Linux. However, Gradle runs successfully on OS X and Windows. Switch to the Jenkins user with:
 * </p>
 * 
 * <p>
 * <blockquote>
 * 
 * <pre>
 * {@code sudo -u jenkins bash}.
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * <p>
 * Run the JUnit manually with the sample command below. Note that jars are copied manually with the
 * sample script, cp_api_libs.sh.
 * </p>
 * 
 * <p>
 * <blockquote>
 * 
 * <pre>
 * {@code java -Djava.library.path=.:/usr/local/lib/ -cp .:/var/lib/jenkins/workspace/API/build/classes/main:/var/lib/jenkins/workspace/API/build/classes/test:/var/lib/jenkins/workspace/API/build/resources/test:/var/lib/jenkins/test_lib/junit-4.12.jar:/var/lib/jenkins/test_lib/hamcrest-core-1.3.jar:/var/lib/jenkins/test_lib/* org.junit.runner.JUnitCore gov.ca.cwds.rest.util.jni.KeyGenTest}
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * <p>
 * Force JUnit tests to run against native libraries, loaded or not, with JVM argument
 * </p>
 * 
 * <p>
 * <blockquote>
 * 
 * <pre>
 * {@code -Dcwds.jni.force=Y}
 * </pre>
 * 
 * </blockquote>
 * </p>
 * 
 * @author CWDS API Team
 */
public final class CmsKeyIdGeneratorTest {

  private static final int GOOD_KEY_LEN = CmsPersistentObject.CMS_ID_LEN;

  private static final Pattern RGX_LEGACY_KEY =
      Pattern.compile("([a-z0-9]{10})", Pattern.CASE_INSENSITIVE);

  private static final Pattern RGX_LEGACY_TIMESTAMP =
      Pattern.compile("([a-z0-9]{7})", Pattern.CASE_INSENSITIVE);

  // private CmsKeyIdGenerator inst;

  @Before
  public void setUpBeforeTest() throws Exception {
    // this.inst = new CmsKeyIdGenerator();
  }

  // ===================
  // GENERATE KEY:
  // ===================

  @Test
  public void testGenKeyGood() {
    final String key = CmsKeyIdGenerator.generate("0X5");
    assertTrue("key not generated", key != null && key.length() == GOOD_KEY_LEN);
  }

  @Test
  public void testGenKeyGoodStaff2() {
    // Good staff id.
    final String key = CmsKeyIdGenerator.generate("0yz");
    assertTrue("key not generated", key != null && key.length() == GOOD_KEY_LEN);
  }

  @Test
  public void testGenKeyBadStaffEmpty() {
    // Empty staff id.
    final String key = CmsKeyIdGenerator.generate("");
    // assertTrue("key generated", key == null || key.length() == 0);
  }

  // TODO: #145948067: default staff id until Perry is ready.
  // @Test(expected = ServiceException.class)
  public void testGenKeyBadStaffNull() {
    // Null staff id.
    final String key = CmsKeyIdGenerator.generate(null);
    // assertTrue("key generated", key == null || key.length() == 0);
  }

  @Test(expected = ServiceException.class)
  public void testGenKeyBadStaffWrongLength() {
    // Wrong staff id length.
    final String key = CmsKeyIdGenerator.generate("abcdefg");
    // assertTrue("key generated", key == null || key.length() == 0);
  }

  @Test(expected = ServiceException.class)
  public void testGenKeyBadStaffTooShort() {
    // Wrong staff id length.
    final String key = CmsKeyIdGenerator.generate("a");
    // assertTrue("key generated", key == null || key.length() == 0);
  }

  @Test(expected = ServiceException.class)
  public void testGenKeyBadStaffTooLong() {
    // Wrong staff id length.
    final String key = CmsKeyIdGenerator
        .generate("ab7777d7d7d7s8283jh4jskksjajfkdjbjdjjjasdfkljcxmzxcvjdhshfjjdkksahf");
    // assertTrue("key generated", key == null || key.length() == 0);
  }

  @Test(expected = ServiceException.class)
  public void testGenKeyBadStaffBadChars1() {
    // Invalid chars in staff id.
    final String key = CmsKeyIdGenerator.generate("ab&");
    // assertTrue("key generated", key == null || key.length() == 0);
  }

  // ===================
  // DECOMPOSE KEY:
  // ===================

  // @Test
  public void testDecomposeGoodKey() {
    // Good key, decomposes correctly.
    KeyDetail kd = new KeyDetail();
    // CmsKeyIdGenerator.decomposeKey("1qxx0OC0X5", kd);
    // assertTrue("Staff ID empty", kd.staffId != null && "0X5".equals(kd.staffId));
  }

  // @Test
  public void testDecomposeKeyLong() {
    // Wrong staff id size: too long.
    KeyDetail kd = new KeyDetail();
    // CmsKeyIdGenerator.decomposeKey("wro000000000000ng", kd);
    // assertTrue("Staff ID not empty", kd.staffId == null || "".equals(kd.staffId));
  }

  // @Test
  public void testDecomposeKeyShort() {
    // Wrong staff id size: too short.
    KeyDetail kd = new KeyDetail();
    // CmsKeyIdGenerator.decomposeKey("w", kd);
    // assertTrue("Staff ID not empty", kd.staffId == null || "".equals(kd.staffId));
  }

  // @Test
  public void testDecomposeKeyEmpty() {
    // Empty staff id.
    KeyDetail kd = new KeyDetail();
    // CmsKeyIdGenerator.decomposeKey("", kd);
    // assertTrue("Staff ID not empty", kd.staffId == null || "".equals(kd.staffId));
  }

  // @Test
  public void testDecomposeKeyNull() {
    // Null staff id.
    KeyDetail kd = new KeyDetail();
    // CmsKeyIdGenerator.decomposeKey(null, kd);
    // assertTrue("Staff ID not empty", kd.staffId == null || "".equals(kd.staffId));
  }

  @Test
  public void type() throws Exception {
    assertThat(CmsKeyIdGenerator.class, notNullValue());
  }

  @Test
  public void createTimestampStr_Args__Date() throws Exception {
    final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
    // given
    Date ts = mock(Date.class);
    // e.g. : given(mocked.called()).willReturn(1);
    // when
    String actual = target.createTimestampStr(ts);
    // then
    // e.g. : verify(mocked).called();
    assertTrue("bad generated timestamp", RGX_LEGACY_TIMESTAMP.matcher(actual).matches());
  }

  // @Test
  // public void createTimestampStr_Args__Date_T__ParseException() throws Exception {
  // final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
  // // given
  // Date ts = mock(Date.class);
  // // e.g. : given(mocked.called()).willReturn(1);
  // try {
  // // when
  // target.createTimestampStr(ts);
  // fail("Expected exception was not thrown!");
  // } catch (ParseException e) {
  // // then
  // }
  // }

  @Test
  public void createTimestampStr_Args__() throws Exception {
    final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
    final String actual = target.createTimestampStr();
    assertTrue("bad generated timestamp", RGX_LEGACY_TIMESTAMP.matcher(actual).matches());
  }

  @Test
  public void timestampToDouble_Args__Calendar() throws Exception {
    final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
    final Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("PST"));
    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-06-09 13:04:22"));

    final double actual = target.timestampToDouble(cal);
    final double expected = 5.922526581E9;
    assertThat(actual, is(equalTo(expected)));
  }

  // @Test
  // public void doubleToStrN_Args__int__double__BigDecimalArray() throws Exception {
  // final CmsKeyIdGenerator target = null;
  // // given
  // int dstLen = 0;
  // double src = 0.0;
  // BigDecimal[] powers = new BigDecimal[] {};
  // // e.g. : given(mocked.called()).willReturn(1);
  // // when
  // String actual = target.doubleToStrN(dstLen, src, powers);
  // // then
  // // e.g. : verify(mocked).called();
  // String expected = null;
  // assertThat(actual, is(equalTo(expected)));
  // }
  //
  // @Test
  // public void strToDouble_Args__String__int__BigDecimalArray() throws Exception {
  // final CmsKeyIdGenerator target = null;
  // // given
  // String src = null;
  // int base = 0;
  // BigDecimal[] powers = new BigDecimal[] {};
  // // e.g. : given(mocked.called()).willReturn(1);
  // // when
  // double actual = target.strToDouble(src, base, powers);
  // // then
  // // e.g. : verify(mocked).called();
  // double expected = 0.0;
  // assertThat(actual, is(equalTo(expected)));
  // }

  // @Test
  // public void getTimestampSeed_Args__Date() throws Exception {
  // final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
  // // given
  // final Date ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-06-09 13:04:22");
  // final Calendar actual = target.getTimestampSeed(ts);
  // // then
  // // e.g. : verify(mocked).called();
  // Calendar expected = null;
  // assertThat(actual, is(equalTo(expected)));
  // }
  //
  // @Test
  // public void getTimestampSeed_Args__Date_T__ParseException() throws Exception {
  // final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
  // // given
  // Date ts = mock(Date.class);
  // // e.g. : given(mocked.called()).willReturn(1);
  // try {
  // // when
  // target.getTimestampSeed(ts);
  // fail("Expected exception was not thrown!");
  // } catch (ParseException e) {
  // // then
  // }
  // }

  @Test(expected = ServiceException.class)
  public void makeKey_Args__String__Date__null_staff() throws Exception {
    final CmsKeyIdGenerator target = new CmsKeyIdGenerator();
    String staffId = null;
    Date ts = mock(Date.class);
    String actual = target.makeKey(staffId, ts);
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void generate_Args__String__null_staff() throws Exception {
    String actual = CmsKeyIdGenerator.generate(null);
    assertTrue("bad generated key", RGX_LEGACY_KEY.matcher(actual).matches());
  }

  @Test
  public void generate_Args__String__Date() throws Exception {
    final String staffId = "0X5";
    final Date ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-06-09 13:04:22");
    final String actual = CmsKeyIdGenerator.generate(staffId, ts);
    final String expected = "06SoJiH0X5";
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void getUIIdentifierFromKey_Args__String() throws Exception {
    // given
    final String key = "5Y3vKVs0X5";
    // e.g. : given(mocked.called()).willReturn(1);
    // when
    String actual = CmsKeyIdGenerator.getUIIdentifierFromKey(key);
    final String expected = "0315-2076-8676-8002051";
    assertThat(actual, is(equalTo(expected)));
  }

}
