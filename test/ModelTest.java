import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;

import models.Address;
import models.Product;
import models.StockItem;
import models.Tag;
import models.Warehouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

public class ModelTest {
  private FakeApplication application;
  
  @Before 
  public void startApp() {
    application = fakeApplication(inMemoryDatabase());
    start(application);
  }

  @After
  public void stopApp() {
    stop(application);
  }
  
  @Test
  public void testModel() {
    //Create 1 tag that's associated with 1 product.
    Tag tag = new Tag("Tag");
    Product product = new Product("Product", "Description");
    product.tags.add(tag);
    tag.products.add(product);
    
    //Create 1 warehouse that's associated with 1 Stock for 1 Product
    Warehouse warehouse = new Warehouse("Warehouse");
    StockItem stockitem = new StockItem(warehouse, product, 100);
    warehouse.stockitems.add(stockitem);
    stockitem.warehouse = warehouse;
    
    //Create 1 address that is associated with 1 warehouse
    Address address = new Address("Address");
    address.warehouse = warehouse;
    warehouse.address = address;
    
    //Persist the sample model by saving all entities and relationships
    warehouse.save();
    address.save();
    tag.save();
    product.save();
    stockitem.save();
    
    //Retrieve the entire model from the database.

    List<Warehouse> warehouses = Warehouse.find().findList();
    List<Address> addresses = Address.find().findList();
    List<Tag> tags = Tag.find().findList();
    List<Product> products = Product.find().findList();
    List<StockItem> stockitems = StockItem.find().findList();
    
    //Check that we've recovered all our entities.
    assertEquals("Checking warehouse", warehouses.size(), 1);
    assertEquals("Checking address", addresses.size(), 1);
    assertEquals("Checking tags", tags.size(), 1);
    assertEquals("Checking products", products.size(), 1);
    assertEquals("Checking stockitems", stockitems.size(), 1);
    
    //Check that we've recovered all relationships.
    assertEquals("Warehouse-StockItem", warehouses.get(0).address, addresses.get(0));
    assertEquals("StockItem-Warehouse", addresses.get(0).warehouse, warehouses.get(0));
    assertEquals("Warehouse-Address", warehouses.get(0).stockitems.get(0), stockitems.get(0));
    assertEquals("Address-Warehouse", stockitems.get(0).warehouse, warehouses.get(0));
    assertEquals("Product-StockItem", products.get(0).stockitems.get(0), stockitems.get(0));
    assertEquals("StockItem-Product", stockitems.get(0).product, products.get(0));
    assertEquals("Product-Tag", products.get(0).tags.get(0), tags.get(0));
    assertEquals("Tag-Product", tags.get(0).products.get(0), products.get(0));    

    // Testing for model manipulation and cascading
    product.tags.clear();
    product.save();
    assertTrue("Previously retrieved product still has tag", !products.get(0).tags.isEmpty());
    assertTrue("Fresh Product has no tag", Product.find().findList().get(0).tags.isEmpty());
    assertTrue("Fresh Tag has no Products", Tag.find().findList().get(0).products.isEmpty());
    tag.delete();
    assertTrue("No more tags in database", Tag.find().findList().isEmpty());
    }
  
}
