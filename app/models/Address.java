package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import play.db.ebean.Model;

@Entity
public class Address extends Model {
  private static final long serialVersionUID = -897221648973164897L;
  @Id
  public Long id;
  public String address;
  @OneToOne(cascade=CascadeType.ALL)
  public Warehouse warehouse;
  
  public Address(String address) {
    this.address = address;
  }
  
  public static Finder<Long, Address> find() {
    return new Finder<Long, Address>(Long.class, Address.class);
  }
}
