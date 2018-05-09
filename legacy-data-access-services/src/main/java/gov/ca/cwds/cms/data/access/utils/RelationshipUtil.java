package gov.ca.cwds.cms.data.access.utils;

import gov.ca.cwds.data.legacy.cms.entity.Client;
import gov.ca.cwds.data.legacy.cms.entity.ClientRelationship;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** @author CWDS TPT-3 Team */
public class RelationshipUtil {

  private static final Set<Short> typesChildParent = new HashSet<>();
  private static final Set<Short> typesParentChild = new HashSet<>();

  static {
    typesChildParent.addAll(
      Arrays.asList(new Short[] {189, 190, 192, 195, 196, 198, 284, 285, 287, 290, 291, 6360}));

    typesParentChild.addAll(
      Arrays.asList(new Short[] {204, 205, 207, 201, 211, 213, 246, 247, 251, 252, 5620, 6361}));
  }

  private RelationshipUtil() {}

  public static boolean isChildParent(ClientRelationship relationship) {
    return typesChildParent.contains(relationship.getType().getSystemId());
  }

  public static boolean isParentChild(ClientRelationship relationship) {
    return typesParentChild.contains(relationship.getType().getSystemId());
  }

  public static Client getChild(ClientRelationship relationship) {
    if (isChildParent(relationship)) {
      return relationship.getPrimaryClient();
    }
    return relationship.getSecondaryClient();
  }

  public static Client getParent(ClientRelationship relationship) {
    if(isChildParent(relationship)) {
      return relationship.getSecondaryClient();
    }
    return relationship.getPrimaryClient();

  }
}
