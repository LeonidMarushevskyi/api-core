package gov.ca.cwds.data.es;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import gov.ca.cwds.data.IAddressAware;
import gov.ca.cwds.data.IPersonAware;
import gov.ca.cwds.rest.api.Request;

/**
 * A domain API {@link Request} for Intake Person Auto-complete feature to Elasticsearch.
 * 
 * <p>
 * The Intake Auto-complete for Person takes a single search term, which is used to query
 * Elasticsearch Person documents by ALL relevant fields. For example, search strings consisting of
 * only digits could be phone numbers, social security numbers, or street address numbers. Search
 * strings consisting of only letters could be last name, first name, city, state, language, and so
 * forth.
 * </p>
 * 
 * <p>
 * This class transforms a raw {@link ElasticSearchPerson} object into a format consumable by
 * Intake.
 * </p>
 * 
 * @author CWDS API Team
 */
public class AutoCompletePerson implements Serializable, IPersonAware {

  /**
   * Base version. Increment by class version.
   */
  private static final long serialVersionUID = 1L;

  // [{
  // "id": 1,
  // "date_of_birth": "1964-01-14",
  // "first_name": "John",
  // "gender": null,
  // "last_name": "Smith",
  // "middle_name": null,
  // "ssn": "858584561",
  // "name_suffix": null,
  // "addresses": [
  // {
  // "city": "city",
  // "id": 6,
  // "state": "IN",
  // "street_address": "876 home",
  // "zip": "66666",
  // "type": "Placement"
  // }
  // ],
  // "phone_numbers": [],
  // "languages": []
  // }]

  // name_suffix:
  // enum:
  // - esq
  // - ii
  // - iii
  // - iv
  // - jr
  // - sr
  // - md
  // - phd
  // - jd
  //
  // gender:
  // type: string
  // enum:
  // - male
  // - female

  public enum AutoCompleteLanguage implements Serializable {

    /**
     * 
     */
    AMERICAN_SIGN_LANGUAGE(1248, "American Sign Language", 13), ARABIC(1249, "Arabic", 14),

    /**
     * 
     */
    ARMENIAN(1250, "Armenian", 15), CAMBODIAN(1251, "Cambodian", 19), CANTONESE(1252, "Cantonese", 74),

    /**
     * 
     */
    ENGLISH(1253, "English", 7), FARSI(1254, "Farsi", 41), FILIPINO(3198, "Filipino", 49), FRENCH(1255, "French", 28),

    /**
     * 
     */
    GERMAN(1267, "German", 29), HAWAIIAN(1268, "Hawaiian", 99), HEBREW(1256, "Hebrew", 33), HMONG(1257, "Hmong", 35), ILACANO(1258, "Ilacano", 77),

    /**
     * 
     */
    INDOCHINESE(3199, "Indochinese", 99), ITALIAN(1259, "Italian", 42), JAPANESE(1260, "Japanese", 3), KOREAN(1261, "Korean", 4), LAO(1262, "Lao", 43),

    /**
     * 
     */
    MANDARIN(1263, "Mandarin", 75), MIEN(1264, "Mien", 76), OTHER_CHINESE(1265, "Other Chinese", 2), OTHER_NON_ENGLISH(1266, "Other Non-English", 99),

    /**
     * 
     */
    POLISH(1269, "Polish", 50), PORTUGUESE(1270, "Portuguese", 51), ROMANIAN(3200, "Romanian", 99), RUSSIAN(1271, "Russian", 54),

    /**
     * 
     */
    SAMOAN(1272, "Samoan", 55), SIGN_LANGUAGE_NOT_ASL(1273, "Sign Language (Not ASL)", 78), SPANISH(1274, "Spanish", 1), TAGALOG(1275, "Tagalog", 5),

    /**
     * 
     */
    THAI(1276, "Thai", 65), TURKISH(1277, "Turkish", 67), VIETNAMESE(1278, "Vietnamese", 69);

    private final int sysId;
    private final String description;
    private final int displayOrder;

    private AutoCompleteLanguage(int sysId, String description, int displayOrder) {
      this.sysId = sysId;
      this.description = description;
      this.displayOrder = displayOrder;
    }

    public int getSysId() {
      return sysId;
    }

    public String getDescription() {
      return description;
    }

    public int getDisplayOrder() {
      return displayOrder;
    }
  }

  @JsonFormat(shape = JsonFormat.Shape.OBJECT)
  public enum AutoCompletePersonAddressType implements Serializable {
    Home, School, Work, Placement, Homeless, Other
  }

  /**
   * Child class. Represents the Address section of Intake Auto-complete.
   * 
   * @author CWDS API Team
   */
  public static final class AutoCompletePersonAddress implements Serializable, IAddressAware {

    /**
     * Base version. Increment by version.
     */
    private static final Long serialVersionUID = 1L;

    private Long id;
    private String streetAddress;
    private String city;
    private String state;
    private String zip;
    private AutoCompletePersonAddressType type;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    @Override
    public String getStreetAddress() {
      return streetAddress;
    }

    @Override
    public void setStreetAddress(String streetAddress) {
      this.streetAddress = streetAddress;
    }

    @Override
    public String getCity() {
      return city;
    }

    @Override
    public void setCity(String city) {
      this.city = city;
    }

    @Override
    public String getState() {
      return state;
    }

    @Override
    public void setState(String state) {
      this.state = state;
    }

    @Override
    public String getZip() {
      return zip;
    }

    @Override
    public void setZip(String zip) {
      this.zip = zip;
    }

    public AutoCompletePersonAddressType getType() {
      return type;
    }

    public void setType(AutoCompletePersonAddressType type) {
      this.type = type;
    }

  }

  @JsonFormat(shape = JsonFormat.Shape.OBJECT)
  public enum AutoCompletePersonPhoneType {
    Cell, Work, Home, Other
  }

  /**
   * Child class. Represents the Phone section of Intake Auto-complete.
   * 
   * @author CWDS API Team
   */
  public static final class AutoCompletePersonPhone implements Serializable {

    /**
     * Base version. Increment by class version.
     */
    private static final long serialVersionUID = 1L;

    private Long id;
    private String number;
    private AutoCompletePersonPhoneType type;

    public AutoCompletePersonPhone() {

    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getNumber() {
      return number;
    }

    public void setNumber(String number) {
      this.number = number;
    }

    public AutoCompletePersonPhoneType getType() {
      return type;
    }

    public void setType(AutoCompletePersonPhoneType type) {
      this.type = type;
    }

  }


  @JsonProperty("id")
  private String id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("middle_name")
  private String middleName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("name_suffix")
  private String nameSuffix;

  @JsonProperty("type")
  private String gender;

  @JsonProperty("type")
  private String ssn;

  @JsonProperty("date_of_birth")
  private String dateOfBirth;

  @JsonProperty("addresses")
  private List<AutoCompletePersonAddress> addresses;

  @JsonProperty("phone_numbers")
  private List<AutoCompletePersonPhone> phoneNumbers;

  /**
   * Default constructor.
   */
  public AutoCompletePerson() {
    // Default, no-op.
  }

  /**
   * Construct from incoming ElasticSearchPerson.
   * 
   * @param esp incoming {@link ElasticSearchPerson}
   */
  public AutoCompletePerson(ElasticSearchPerson esp) {
    this.setId(esp.getId());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getMiddleName() {
    return middleName;
  }

  @Override
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getNameSuffix() {
    return nameSuffix;
  }

  public void setNameSuffix(String nameSuffix) {
    this.nameSuffix = nameSuffix;
  }

  @Override
  public String getGender() {
    return gender;
  }

  @Override
  public void setGender(String gender) {
    this.gender = gender;
  }

  @Override
  public String getSsn() {
    return ssn;
  }

  @Override
  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public List<AutoCompletePersonAddress> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<AutoCompletePersonAddress> addresses) {
    this.addresses = addresses;
  }

  public List<AutoCompletePersonPhone> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(List<AutoCompletePersonPhone> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  @Override
  public String getBirthDate() {
    return getDateOfBirth();
  }

  @Override
  public void setBirthDate(String birthDate) {
    setDateOfBirth(birthDate);
  }

}