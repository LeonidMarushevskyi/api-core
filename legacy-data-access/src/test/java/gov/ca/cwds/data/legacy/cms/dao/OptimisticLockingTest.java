package gov.ca.cwds.data.legacy.cms.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.OptimisticLockException;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gov.ca.cwds.data.legacy.cms.entity.ChildClient;
import gov.ca.cwds.data.legacy.cms.entity.Client;
import gov.ca.cwds.data.legacy.cms.entity.ClientOtherEthnicity;
import gov.ca.cwds.data.legacy.cms.entity.enums.AdoptionStatus;
import gov.ca.cwds.data.legacy.cms.persistence.BaseCwsCmsInMemoryPersistenceTest;

public class OptimisticLockingTest extends BaseCwsCmsInMemoryPersistenceTest {

  private static final String ID = "AapJGAU04Z";
  private ClientDao dao = null;

  @Before
  public void before() {
    dao = new ClientDao(sessionFactory);
  }

  @Test
  @Ignore // SQL grammar error
  public void testClientLastUpdatedTimeUpdatesdAutomatically() throws Exception {
    cleanAllAndInsert("/dbunit/OptimisticLocking.xml");

    final ChildClientHolder childClientHolder = new ChildClientHolder();

    executeInTransaction(sessionFactory, (sessionFactory) -> {
      Client client = dao.find(ID);
      assertNotNull(client);
      assertTrue(client instanceof ChildClient);
      ChildClient childClient = (ChildClient) client;
      childClientHolder.setChildClient(childClient);

      Timestamp lastUpdateTime = childClient.getLastUpdateTime();
      assertNotNull(lastUpdateTime);
      assertEquals(new Short((short) 0), childClient.getAdoptedAge());

      // Change child client specific field and assure that Client timestamp changes
      childClient.setAdoptedAge((short) 10);
      dao.update(client);
      Session session = sessionFactory.getCurrentSession();
      session.flush();

      Timestamp newLastUpdateTime = childClient.getLastUpdateTime();
      assertNotNull(newLastUpdateTime);
      assertFalse(lastUpdateTime.equals(newLastUpdateTime));
      assertTrue((newLastUpdateTime.toLocalDateTime()).isAfter(lastUpdateTime.toLocalDateTime()));

      // Check if Client timestamp changes when otherEthnicities collection changes
      Set<ClientOtherEthnicity> otherEthnicities = childClient.getOtherEthnicities();
      assertEquals(1, otherEthnicities.size());
      ClientOtherEthnicity otherEthnicity = otherEthnicities.iterator().next();
      assertEquals(5922, otherEthnicity.getEthnicityCode());

      // Should increase Client timestamp but does not work
      session.lock(childClient, LockMode.OPTIMISTIC_FORCE_INCREMENT);

      ClientOtherEthnicity otherEthnicity2 = new ClientOtherEthnicity();
      otherEthnicity2.setEthnicityCode((short) 5923);
      otherEthnicity2.setId("BaoNM5i00J");
      otherEthnicity2.setLastUpdateTime(LocalDateTime.now());
      otherEthnicity2.setLastUpdateId("04Z");
      childClient.addOtherEthnicity(otherEthnicity2);

      dao.update(client);
    });

    executeInTransaction(sessionFactory, (sessionFactory) -> {
      ChildClient oldChildClient = childClientHolder.getChildClient();
      Timestamp oldLastUpdateTime = oldChildClient.getLastUpdateTime();

      Client client = dao.find(ID);
      assertNotNull(client);
      assertTrue(client instanceof ChildClient);
      ChildClient childClient = (ChildClient) client;

      Timestamp lastUpdateTime = childClient.getLastUpdateTime();
      assertNotNull(lastUpdateTime);
      // changes in otherEthnicities collection DOES NOT change Client timestamp
      assertTrue(oldLastUpdateTime.equals(lastUpdateTime));
    });
  }

  @Test
  public void testUpdateSuccess() throws Exception {

    cleanAllAndInsert("/dbunit/OptimisticLocking.xml");

    final ChildClientHolder childClientHolder = new ChildClientHolder();

    // read entity from DB
    executeInTransaction(sessionFactory, (sessionFactory) -> {
      Client client = dao.find(ID);
      assertNotNull(client);
      assertTrue(client instanceof ChildClient);
      ChildClient childClient = (ChildClient) client;
      assertEquals(AdoptionStatus.NOT_FREE, childClient.getAdoptionStatus());
      Timestamp lastUpdateTime = childClient.getLastUpdateTime();
      assertNotNull(lastUpdateTime);
      childClientHolder.setChildClient(childClient);
    });

    // UI edit
    childClientHolder.getChildClient().setAdoptionStatus(AdoptionStatus.NOT_APPLICABLE);

    // persist in new transaction
    executeInTransaction(sessionFactory,
        sessionFactory -> dao.update(childClientHolder.getChildClient()));
  }

  @Test
  public void testUpdateWithStaleData() throws Exception {

    cleanAllAndInsert("/dbunit/OptimisticLocking.xml");

    final ChildClientHolder childClientHolder = new ChildClientHolder();

    // read entity from DB
    executeInTransaction(sessionFactory, (sessionFactory) -> {
      Client client = dao.find(ID);
      assertNotNull(client);
      assertTrue(client instanceof ChildClient);
      ChildClient childClient = (ChildClient) client;
      Timestamp lastUpdateTime = childClient.getLastUpdateTime();
      assertNotNull(lastUpdateTime);
      assertEquals(AdoptionStatus.NOT_FREE, childClient.getAdoptionStatus());
      childClientHolder.setChildClient(childClient);
    });

    // update DB in other transaction
    executeInTransaction(sessionFactory, (sessionFactory) -> {
      Client client = dao.find(ID);
      assertNotNull(client);
      assertTrue(client instanceof ChildClient);
      ChildClient childClient = (ChildClient) client;
      childClient.setAdoptionStatus(AdoptionStatus.PARTIALLY_FREE);
      dao.update(childClient);
    });

    // UI edit
    childClientHolder.getChildClient().setAdoptionStatus(AdoptionStatus.TOTALLY_FREE);

    // recreate entity and attempt to persist
    try {
      executeInTransaction(sessionFactory,
          sessionFactory -> dao.update(childClientHolder.getChildClient()));

      fail("OptimisticLockException should be thrown");
    } catch (OptimisticLockException e) {
    }
  }

  @Test
  public void testNoTimestampPropagationToUI() throws Exception {

    cleanAllAndInsert("/dbunit/OptimisticLocking.xml");

    final ChildClientHolder childClientHolder = new ChildClientHolder();

    // read entity from DB
    executeInTransaction(sessionFactory, (sessionFactory) -> {
      Client client = dao.find(ID);
      assertNotNull(client);
      assertTrue(client instanceof ChildClient);
      ChildClient childClient = (ChildClient) client;
      assertEquals(AdoptionStatus.NOT_FREE, childClient.getAdoptionStatus());
      childClientHolder.setChildClient(childClient);
    });

    // UI edit
    childClientHolder.getChildClient().setLastUpdateTime(null);
    childClientHolder.getChildClient().setAdoptionStatus(AdoptionStatus.TOTALLY_FREE);

    // recreate entity and attempt to persist
    try {
      executeInTransaction(sessionFactory, (sessionFactory) -> {
        Session session = sessionFactory.getCurrentSession();

        ChildClient childClient = childClientHolder.getChildClient();

        // populate lastUpdateTime by current time
        childClient.setLastUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        dao.update(childClient);
      });

      fail("OptimisticLockException should be thrown");
    } catch (OptimisticLockException e) {
    }
  }

  // imitates DTO
  private static class ChildClientHolder {
    private ChildClient childClient;

    public ChildClient getChildClient() {
      return childClient;
    }

    public void setChildClient(ChildClient childClient) {
      this.childClient = childClient;
    }
  }
}

