package gov.ca.cwds.data.legacy.cms.entity.enums;

import java.util.Collections;
import java.util.Map;
import javax.persistence.Converter;

public enum SameHomeStatus implements EntityEnum<String> {
  NO("N", "No"),
  UNKNOWN("U", "Unknown"),
  YES("Y", "Yes");

  private final String code;
  private final String description;

  SameHomeStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static SameHomeStatus fromCode(String code) {
    return new SameHomeStatusConverter().convertToEntityAttribute(code);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Converter
  public static class SameHomeStatusConverter extends BaseEntityEnumConverter<SameHomeStatus, String> {

    private static final Map<String, SameHomeStatus> codeMap =
        Collections.unmodifiableMap(initializeMapping(SameHomeStatus.values()));

    @Override
    Map<String, SameHomeStatus> getCodeMap() {
      return codeMap;
    }
  }
}
