package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class StockItem extends Model {
  private static final long serialVersionUID = -8143420434783395242L;
  @Id
  public Long primaryKey;
  @Required 
  private String stockItemId;
  @ManyToOne(cascade=CascadeType.ALL)
  private Warehouse warehouse;
  @Transient 
  private String warehouseId;
  @ManyToOne(cascade=CascadeType.ALL)
  private Product product;
  @Transient 
  private String productId;  
  private long quantity;

  /** 
   * StockItem requires a warehouse and a product.
   * @return null if OK, error string if not OK.
   */
  public String validate() {
    Warehouse warehouseTemp = Warehouse.find().where().eq("warehouseId", getWarehouseId()).findUnique();
    Product productTemp = Product.find().where().eq("productId", getProductId()).findUnique();
    
    //if (warehouseTemp != null) {
    //  return warehouseTemp.getName();
    //}
    //return "warehouseId" + getWarehouseId() + "productId" + getProductId();
    return (warehouseTemp == null || productTemp == null) ? "Invalid. StockItem must have a warehouse and a product": null;
  }
  
  public StockItem(String stockItemId, Warehouse warehouse, Product product, long quantity) {
    this.stockItemId = stockItemId;
    this.warehouse = warehouse;
    this.product = product;
    this.quantity = quantity;
  }

  
  public String toString() {
    return String.format("[StockItem %s]", stockItemId);
  }
  
  public String getStockItemId() {
    return stockItemId;
  }

  public void setStockItemId(String stockItemId) {
    this.stockItemId = stockItemId;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getWarehouseId() {
    return warehouseId;
  }

  public void setWarehouseId(String warehouseId) {
    this.warehouseId = warehouseId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public static Finder<Long, StockItem> find() {
    return new Finder<Long, StockItem>(Long.class, StockItem.class);
  }
}
