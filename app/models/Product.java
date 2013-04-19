package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import play.db.ebean.Model;
import play.data.validation.Constraints.Required;

@Entity
public class Product extends Model {
  private static final long serialVersionUID = -463965431223831634L;
  @Id
  public Long primaryKey;
  @Required
  private String productId;
  @Required
  private String name;
  private String description;
  @ManyToMany(cascade=CascadeType.ALL)
  private List<Tag> tags = new ArrayList<>();
  @OneToMany(mappedBy="product", cascade=CascadeType.ALL)
  private List<StockItem> stockitems = new ArrayList<>();

  public Product(String productId, String name, String description) {
    this.productId = productId;
    this.name = name;
    this.description = description;
  }
  
  public static Finder<Long, Product> find() {
    return new Finder<Long, Product>(Long.class, Product.class);
  }
  
  public String toString() {
    return String.format("[Product %s %s %s]", productId, name, description);
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public List<StockItem> getStockitems() {
    return stockitems;
  }

  public void setStockitems(List<StockItem> stockitems) {
    this.stockitems = stockitems;
  }
}
