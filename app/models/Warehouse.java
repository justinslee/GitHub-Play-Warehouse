package models;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Warehouse extends Model {
  private static final long serialVersionUID = -7268900706085963780L;
  @Id
  public Long primaryKey;
  @Required
  private String warehouseId;
  @Required
  private String name;
  @OneToMany(mappedBy="warehouse", cascade=CascadeType.ALL)
  private List<StockItem> stockitems = new ArrayList<>();
  @OneToOne(mappedBy="warehouse", cascade=CascadeType.ALL)
  private Address address;
  @Transient 
  private String addressId;
  
  public Warehouse(String warehouseId, String name) {
    this.warehouseId = warehouseId;
    this.name = name;
  }
  
  public static Finder<Long, Warehouse> find() {
    return new Finder<Long, Warehouse>(Long.class, Warehouse.class);
  }
  
  /** From example code in discussions */
  public String validate() {
    Address addressTemp = Address.find().where().eq("addressId", getAddressId()).findUnique();
    return (addressTemp == null) ? "Error: Address with ID " + addressId + " does not exist." : null;
  }
  public String toString() {
    return String.format("[Warehouse %s %s]", warehouseId, name);
  }
  public String getWarehouseId() {
    return warehouseId;
  }

  public void setWarehouseId(String warehouseId) {
    this.warehouseId = warehouseId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<StockItem> getStockitems() {
    return stockitems;
  }

  public void setStockitems(List<StockItem> stockitems) {
    this.stockitems = stockitems;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
  
  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }
}
