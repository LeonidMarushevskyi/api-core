package gov.ca.cwds.data.legacy.cms.entity.enums;

import io.dropwizard.util.Generics;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeConverter;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseEntityEnumConvertor<E extends EntityEnum<K>, K> implements
    AttributeConverter<E, K> {

  static <K, E> Map<K, E> initializeMapping(EntityEnum<K>[] values) {
    Map result = new HashMap<K, EntityEnum<K>>();
    for (EntityEnum<K> item : values) {
      result.put(item.getCode(), item);
    }
    return result;
  }

  @Override
  public K convertToDatabaseColumn(E value) {
    if (value == null) {
      return null;
    }
    return value.getCode();
  }

  @Override
  public E convertToEntityAttribute(K code) {
    if (code == null) {
      return null;
    }

    if ((code instanceof String) && StringUtils.isBlank((String) code)) {
        return null;
    }

    if ((code instanceof Character) && code.equals(' ')) {
        return null;
    }

    E result = getCodeMap().get(code);

    if (result == null) {
      Class<?> enumClass = Generics.getTypeParameter(getClass(), EntityEnum.class);
      String enumClassName = enumClass.getSimpleName();
      throw new UnsupportedOperationException(
          enumClassName + " enum : the code '" + code + "' is not supported!");
    }
    return result;
  }

  abstract Map<K, E> getCodeMap();
}
