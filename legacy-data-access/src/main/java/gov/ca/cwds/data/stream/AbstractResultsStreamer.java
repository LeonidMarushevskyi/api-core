package gov.ca.cwds.data.stream;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;

import gov.ca.cwds.data.BaseDaoImpl;
import gov.ca.cwds.data.persistence.PersistentObject;

/**
 * @author CWDS TPT-2
 */
public abstract class AbstractResultsStreamer<E extends PersistentObject> {

  private BaseDaoImpl<E> dao;
  private QueryCreator<E> queryCreator;

  AbstractResultsStreamer(BaseDaoImpl<E> dao, QueryCreator<E> queryCreator) {
    this.dao = dao;
    this.queryCreator = queryCreator;
  }

  protected abstract E getResult(ScrollableResults scrollableResults);

  public Stream<E> createStream() {
    Session session = dao.grabSession();
    Class<E> entityClass = dao.getEntityClass();
    Query<E> query = queryCreator.createQuery(session, entityClass);
    ScrollableResults results = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
    return StreamSupport.stream(newIterable(results).spliterator(), false);
  }

  private Iterable<E> newIterable(ScrollableResults scrollableResults) {
    return () -> new ScrollableResultsIterator<E>(scrollableResults) {
      @Override
      protected E extractResult(ScrollableResults scrollableResults) {
        return getResult(scrollableResults);
      }
    };
  }
}
