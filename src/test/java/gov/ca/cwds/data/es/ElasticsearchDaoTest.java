package gov.ca.cwds.data.es;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import gov.ca.cwds.data.es.ElasticSearchPerson.ESColumn;
import gov.ca.cwds.rest.ElasticsearchConfiguration;

/**
 * Class under test: {@link ElasticsearchDao}.
 * 
 * <p>
 * <strong>REQUIRES ELASTICSEARCH 5.X!</strong>
 * </p>
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("javadoc")
public final class ElasticsearchDaoTest {

  private static final String TEST_INDEXNAME = "people";
  private static final String TEST_INDEXTYPE = "person";

  @Mock
  private Client client;

  private ElasticsearchDao target;

  @InjectMocks
  @Spy
  private ElasticsearchDao cut = new ElasticsearchDao(client, new ElasticsearchConfiguration());

  // @Mock
  // private TransportClient.Builder clientBuilder;

  @Mock
  private SearchRequestBuilder srb;

  @Mock
  private IndexRequestBuilder irb;

  @Mock
  private ListenableActionFuture<SearchResponse> listenSearch;

  @Mock
  private ListenableActionFuture<IndexResponse> listenIndex;

  @Mock
  private SearchResponse respSearch;

  @Mock
  private IndexResponse respIndex;

  @Mock
  private SearchHits hits;

  @Mock
  private SearchHit hit;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    mock(TransportClient.class);
    mock(SearchRequestBuilder.class);
    MockitoAnnotations.initMocks(this);
    target = new ElasticsearchDao(client, new ElasticsearchConfiguration());

    // To run queries against a fake transport and client, use this:
    // cut.setTransportAddress(DummyTransportAddress.INSTANCE);

    // Mock up searches:
    when(client.prepareSearch(any())).thenReturn(srb);
    when(srb.setTypes(any())).thenReturn(srb);
    when(srb.setSearchType(any(SearchType.class))).thenReturn(srb);
    when(srb.setFrom(0)).thenReturn(srb);
    when(srb.setSize(any(Integer.class))).thenReturn(srb);
    when(srb.setExplain(any(Boolean.class))).thenReturn(srb);
    when(srb.execute()).thenReturn(listenSearch);
    when(srb.setQuery(any(QueryBuilder.class))).thenReturn(srb);
    // when(srb.addHighlightedField(any(String.class))).thenReturn(srb);
    // when(srb.setHighlighterNumOfFragments(any(Integer.class))).thenReturn(srb);
    // when(srb.setHighlighterRequireFieldMatch(any(Boolean.class))).thenReturn(srb);
    // when(srb.setHighlighterOrder(any(String.class))).thenReturn(srb);
    when(listenSearch.actionGet()).thenReturn(respSearch);
    when(respSearch.getHits()).thenReturn(hits);
    when(hits.getHits()).thenReturn(new SearchHit[] {hit});

    // Mock up document creation:
    when(client.prepareIndex(any(String.class), any(String.class), any(String.class)))
        .thenReturn(irb);
    // when(irb.setConsistencyLevel(WriteConsistencyLevel.DEFAULT)).thenReturn(irb);
    when(irb.setSource(any(String.class))).thenReturn(irb);
    when(irb.execute()).thenReturn(listenIndex);
    when(listenIndex.actionGet()).thenReturn(respIndex);
    // when(respIndex.isCreated()).thenReturn(true);
  }

  @After
  public void teardown() throws IOException {
    target.close();
  }

  // @Test
  // public void testIndexNameEmptyFails() throws Exception {
  // thrown.expect(IllegalArgumentException.class);
  // thrown.expectMessage(startsWith("index cannot be Null or empty"));
  // cut.index("", "some_document_type", "some_document", "some_id");
  // }

  // @Test
  // public void testDocumentTypeEmptyFails() throws Exception {
  // thrown.expect(IllegalArgumentException.class);
  // thrown.expectMessage(startsWith("documentType cannot be Null or empty"));
  // cut.index("some_index", "", "some_document", "some_id");
  // }

  // @Test
  // public void testIndexDoc() throws Exception {
  // assertThat("index doc", cut.index("some_index", "some_document_type", "fred", "1234"));
  // }

  // @Test
  public void type() throws Exception {
    assertThat(ElasticsearchDao.class, notNullValue());
  }

  // @Test
  public void instantiation() throws Exception {
    assertThat(target, notNullValue());
  }

  // @Test
  public void stop_Args$() throws Exception {
    target.stop();
  }

  // @Test(expected = IllegalArgumentException.class)
  public void autoCompletePerson_Args$String_Throws$ApiElasticSearchException() throws Exception {
    String searchTerm = null;
    target.searchPerson(searchTerm);
    fail("Expected exception was not thrown!");
  }

  // @Test
  public void autoCompletePerson_Args$String_mock_client() throws Exception {
    String searchTerm = "junk";
    final ElasticSearchPerson[] actual = target.searchPerson(searchTerm);
    assertThat("nothing returned", actual != null);
  }

  // @Test
  public void buildBoolQueryFromSearchTermsBuildsExpectedQuery() {
    BoolQueryBuilder actualQuery =
        target.buildBoolQueryFromSearchTerms("john smith 9/1/1990 123456789   ");
    QueryBuilder expectedQuery = QueryBuilders.boolQuery()
        .should(QueryBuilders.prefixQuery(ESColumn.FIRST_NAME.getCol(), "john"))
        .should(QueryBuilders.prefixQuery(ESColumn.LAST_NAME.getCol(), "john"))
        .should(QueryBuilders.prefixQuery(ESColumn.FIRST_NAME.getCol(), "smith"))
        .should(QueryBuilders.prefixQuery(ESColumn.LAST_NAME.getCol(), "smith"))
        .should(QueryBuilders.matchQuery(ESColumn.BIRTH_DATE.getCol(), "1990-09-01"))
        .should(QueryBuilders.prefixQuery(ESColumn.SSN.getCol(), "123456789"));
    assertThat(actualQuery.toString(), is(equalTo(expectedQuery.toString())));
  }

  // @Test
  public void buildBoolQueryFromMalformedSearchTermsBuildsQueryWithNoClauses() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("a-#4 df$ jk-/+ ");
    assertThat(actualQuery.hasClauses(), is(equalTo(false)));
  }

  // @Test
  public void buildBoolQueryFromMalformedDateBuildsQueryWithNoClauses() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("9/8-9000");
    assertThat(actualQuery.hasClauses(), is(equalTo(false)));
  }

  // @Test
  public void buildBoolQueryFromMalformedSsnBuildsQueryWithNoClauses() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("111-1090/0905 ");
    assertThat(actualQuery.hasClauses(), is(equalTo(false)));
  }

  // @Test
  public void buildBoolQueryFromMoreThanNineDigitSsnBuildsQueryWithNoClauses() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("111223333111 ");
    assertThat(actualQuery.hasClauses(), is(equalTo(false)));
  }

  // @Test
  public void buildBoolQueryFromTwoDateBuildsQueryWithTwoDateClauses() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("1989-01-01 9/1/1990   ");
    QueryBuilder expectedQuery = QueryBuilders.boolQuery()
        .should(QueryBuilders.matchQuery(ESColumn.BIRTH_DATE.getCol(), "1989-01-01"))
        .should(QueryBuilders.matchQuery(ESColumn.BIRTH_DATE.getCol(), "1990-09-01"));
    assertThat(actualQuery.toString(), is(equalTo(expectedQuery.toString())));
  }

  // @Test
  public void buildBoolQueryFromTwoSsnBuildsQueryWithTwoSsnClauses() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("123456789   111223333 ");
    QueryBuilder expectedQuery = QueryBuilders.boolQuery()
        .should(QueryBuilders.prefixQuery(ESColumn.SSN.getCol(), "123456789"))
        .should(QueryBuilders.prefixQuery(ESColumn.SSN.getCol(), "111223333"));
    assertThat(actualQuery.toString(), is(equalTo(expectedQuery.toString())));
  }

  // @Test
  public void buildBoolQueryWithSsnAndBirthYear() {
    BoolQueryBuilder actualQuery = target.buildBoolQueryFromSearchTerms("123456789   1998 ");
    QueryBuilder expectedQuery = QueryBuilders.boolQuery()
        .should(QueryBuilders.prefixQuery(ESColumn.SSN.getCol(), "123456789"))
        .should(QueryBuilders.prefixQuery(ESColumn.SSN.getCol(), "1998")).should(QueryBuilders
            .rangeQuery(ESColumn.BIRTH_DATE.getCol()).gte("1998-01-01").lte("1998-12-31"));
    assertThat(actualQuery.toString(), is(equalTo(expectedQuery.toString())));
  }

}
