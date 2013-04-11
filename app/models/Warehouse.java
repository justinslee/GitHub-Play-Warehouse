package models;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Warehouse extends Model {
  private static final long serialVersionUID = -7268900706085963780L;
  @Id
  public Long id;
  public String name;
  @OneToMany(mappedBy="warehouse", cascade=CascadeType.ALL)
  public List<StockItem> stockitems = new ArrayList<>();
  @OneToOne(mappedBy="warehouse", cascade=CascadeType.ALL)
  public Address address;
  
  public Warehouse(String name) {
    this.name = name;
  }
  
  public static Finder<Long, Warehouse> find() {
    return new Finder<Long, Warehouse>(Long.class, Warehouse.class);
  }
}
