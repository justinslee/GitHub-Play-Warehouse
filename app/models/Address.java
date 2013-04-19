package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Address extends Model {
  private static final long serialVersionUID = -897221648973164897L;
  @Id
  public Long primaryKey;
  @Required
  private String addressId;
  @Required
  @OneToOne(cascade=CascadeType.ALL)
  private Warehouse warehouse;
  
  public Address(String addressId) {
    this.addressId = addressId;
  }
  
  public static Finder<Long, Address> find() {
    return new Finder<Long, Address>(Long.class, Address.class);
  }
  
  public String toString() {
    return String.format("[Address %s]", addressId);
  }
  
  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }
}
