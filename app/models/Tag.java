package models;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity 
public class Tag extends Model {
  private static final long serialVersionUID = -5950706943271500599L;
  @Id
  public Long primaryKey;
  @Required
  private String tagId;
  @ManyToMany(mappedBy="tags", cascade=CascadeType.ALL)
  private List<Product> products = new ArrayList<>();

  /** 
   * No tag can be named "Tag".
   * Note: illustrate use of validate() method.
   * @return null if OK, error string if not OK.
   */
  public String validate() {
    return ("Tag".equals(this.tagId)) ? "Invalid tag name": null;
  }

  public static Finder<Long,Tag> find() {
    return new Finder<Long, Tag>(Long.class, Tag.class);
  }
  
  public String toString() {
    return String.format("[Tag %s ]", tagId);
  }
  
  public Tag(String tagId) {
    this.tagId = tagId;
  }
  
  public String getTagId() {
    return tagId;
  }

  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
