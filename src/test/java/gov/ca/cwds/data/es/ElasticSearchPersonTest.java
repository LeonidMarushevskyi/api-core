package gov.ca.cwds.data.es;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.data.es.ElasticSearchPerson.ESColumn;
import io.dropwizard.jackson.Jackson;

/**
 * NOTE: cannot define all test cases for CMS persistence classes found in CWDS API because they are
 * not available in api-core.
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("javadoc")
public class ElasticSearchPersonTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  String id = "1234567ABC";
  String firstName = "Mike";
  String lastName = "Smith";
  String gender = "M";
  String birthDate = "2000-10-31";
  String ssn = "111122333";
  String sourceType = "gov.ca.cwds.data.persistence.cms.rep.ReplicatedClient";
  String sourceJson = null;
  String highlight = "{\"firstName\":\"<em>mik</em>\",\"lastName\":\"<em>ith</em>\"}";

  @Mock
  private SearchHit hit;

  @Before
  public void setUp() throws Exception {

    Map<String, HighlightField> highlights = new HashMap();

  }

  @Test
  public void type() throws Exception {
    assertThat(ElasticSearchPerson.class, notNullValue());
  }

  @Test
  public void testConstuctorSuccess() throws Exception {
    ElasticSearchPerson target = new ElasticSearchPerson(id, firstName, lastName, gender, birthDate,
        ssn, sourceType, sourceJson, highlight);

    assertThat(target, notNullValue());
  }

  @Test
  public void testPullColFirstNameSuccess() throws Exception {
    // given
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "fred";
    m.put(ESColumn.FIRST_NAME.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.FIRST_NAME);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testPullColLastNameSuccess() throws Exception {
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "last name";
    m.put(ESColumn.LAST_NAME.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.LAST_NAME);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));

  }

  @Test
  public void testPullColGenderSuccess() throws Exception {
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "M";
    m.put(ESColumn.GENDER.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.GENDER);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));

  }

  @Test
  public void testPullColBirthDateSuccess() throws Exception {
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "2000-10-31";
    m.put(ESColumn.BIRTH_DATE.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.BIRTH_DATE);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));

  }

  @Test
  public void testPullColSsnSuccess() throws Exception {
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "111122333";
    m.put(ESColumn.SSN.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.SSN);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));

  }

  @Test
  public void testPullColTypeSuccess() throws Exception {
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "gov.ca.cwds.data.persistence.cms.rep.ReplicatedClient";
    m.put(ESColumn.TYPE.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.TYPE);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));

  }

  @Test
  public void testPullColSourceSuccess() throws Exception {
    Map<String, Object> m = new HashMap<String, Object>();
    final String value =
        "{\"adjudicatedDelinquentIndicator\":\"\",\"adoptionStatusCode\":\"N\",\"alienRegistrationNumber\":\"\"}";
    m.put(ESColumn.SOURCE.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.SOURCE);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));

  }

  @Test
  public void testPullColNameNullSuccess() throws Exception {
    // given
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = null;
    m.put(ESColumn.FIRST_NAME.getCol(), value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.FIRST_NAME);
    Object expected = value;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testPullColUnknownKeyFail() throws Exception {
    // given
    Map<String, Object> m = new HashMap<String, Object>();
    final String value = "something";
    m.put("who knows", value);

    Object actual = ElasticSearchPerson.<String>pullCol(m, ESColumn.FIRST_NAME);
    Assert.assertTrue("unknown column in map",
        actual instanceof String && StringUtils.isEmpty((String) actual));
  }

  @Test
  public void trim_Args$String() throws Exception {
    // given
    String s = null;
    // e.g. : given(mocked.called()).willReturn(1);
    // when
    String actual = ElasticSearchPerson.trim(s);
    // then
    // e.g. : verify(mocked).called();
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void makeESPerson_Args$SearchHit() throws Exception {
    SearchHit hit = mock(SearchHit.class);
    Map<String, Object> m = new HashMap<>();
    m.put(ESColumn.FIRST_NAME.getCol(), "john");
    m.put(ESColumn.GENDER.getCol(), "U");
    when(hit.getSource()).thenReturn(m);

    ElasticSearchPerson actual = ElasticSearchPerson.makeESPerson(hit);
    actual.setHighlightFields("{}");
    ElasticSearchPerson expected =
        new ElasticSearchPerson("", "john", "", "U", null, null, null, null, "{}");
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testSerializeSuccess() throws Exception {

    ElasticSearchPerson expected = validElasticSearchPerson();

    ElasticSearchPerson actual = new ElasticSearchPerson(id, firstName, lastName, gender, birthDate,
        ssn, sourceType, sourceJson, highlight);

    assertThat(actual, is(equalTo(expected)));

  }

  private ElasticSearchPerson validElasticSearchPerson()
      throws JsonParseException, JsonMappingException, IOException {

    return MAPPER.readValue(fixture("fixtures/data/es/validElasticSearchPerson.json"),
        ElasticSearchPerson.class);

  }
}
