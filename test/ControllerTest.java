import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.status;
import static play.test.Helpers.stop;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Product;
import models.Tag;
import models.StockItem;
import models.Warehouse;
import models.Address;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;

public class ControllerTest {
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
  public void testProductController() {
    // Test GET /product on an empty database.
    Result result = callAction(controllers.routes.ref.Product.index());
    assertTrue("Empty products", contentAsString(result).contains("No products"));
    
    // Test GET /product on a database containing a single product.
    String productId = "Product-01";
    Product product = new Product(productId, "French Press", "Coffee Maker");
    product.save();
    result = callAction(controllers.routes.ref.Product.index());
    assertTrue("One product", contentAsString(result).contains(productId));
    
    // Test GET /product/Product-01
    result = callAction(controllers.routes.ref.Product.details(productId));
    assertTrue("Product detail", contentAsString(result).contains(productId));
    
    // Test GET /product/BadProductId and make sure we get a 404
    result = callAction(controllers.routes.ref.Product.details("BadProductId"));
    assertEquals("Product detail (bad)", NOT_FOUND, status(result));
        
    // Test POST /products (with simulated, valid form data).
    Map<String, String> productData = new HashMap<String, String>();
    productData.put("productId", "Product-02");
    productData.put("name", "Baby Gaggia");
    productData.put("description", "Espresso machine");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(productData);
    result = callAction(controllers.routes.ref.Product.newProduct(), request);
    assertEquals("Create new product", OK, status(result));
    
    // Test POST /products (with simulated, invalid form data).
    request = fakeRequest();
    result = callAction(controllers.routes.ref.Product.newProduct(), request);
    
    // Test DELETE /product/Product-01 (a valid ProductId)
    result = callAction(controllers.routes.ref.Product.delete(productId));
    assertEquals("Delete current product OK", OK, status(result));
    result = callAction(controllers.routes.ref.Product.details(productId));
    assertEquals("Deleted product gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Product.delete(productId));
    assertEquals("Deleted missing product also OK", OK, status(result));
  }
  
  @Test
  public void testTagController() {
    // Test GET /tag on an empty database.
    Result result = callAction(controllers.routes.ref.Tag.index());
    assertTrue("Empty tags", contentAsString(result).contains("No Tags"));
    
    // Test GET /tag on a database containing a single tag.
    String tagId = "Tag-01";
    Tag tag = new Tag(tagId);
    tag.save();
    result = callAction(controllers.routes.ref.Tag.index());
    assertTrue("One tag", contentAsString(result).contains(tagId));
    
    // Test GET /tag/Tag-01
    result = callAction(controllers.routes.ref.Tag.details(tagId));
    assertTrue("Tag detail", contentAsString(result).contains(tagId));
    
    // Test GET /tag/BadTagId and make sure we get a 404
    result = callAction(controllers.routes.ref.Tag.details("BadTagId"));
    assertEquals("Tag detail (bad)", NOT_FOUND, status(result));
        
    // Test POST /tags (with simulated, valid form data).
    Map<String, String> tagData = new HashMap<String, String>();
    tagData.put("tagId", "Tag-02");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(tagData);
    result = callAction(controllers.routes.ref.Tag.newTag(), request);
    assertEquals("Create new tag", OK, status(result));
    
    // Test POST /tags (with invalid tag: tags cannot be named "Tag").
    // Illustrates use of validate() method in models.Tag.
    request = fakeRequest();
    tagData.put("tagId", "Tag");
    request.withFormUrlEncodedBody(tagData);
    result = callAction(controllers.routes.ref.Tag.newTag(), request);
    assertEquals("Create bad tag fails", BAD_REQUEST, status(result));
    
    // Test DELETE /tag/Tag-01 (a valid TagId)
    result = callAction(controllers.routes.ref.Tag.delete(tagId));
    assertEquals("Delete current tag OK", OK, status(result));
    result = callAction(controllers.routes.ref.Tag.details(tagId));
    assertEquals("Deleted tag gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Tag.delete(tagId));
    assertEquals("Deleted missing tag also OK", OK, status(result));
  }
  
  @Test
  public void testStockItemController() {
    // Test GET /stockItem on an empty database.
    Result result = callAction(controllers.routes.ref.StockItem.index());
    assertTrue("Empty stockitems", contentAsString(result).contains("No stockitems"));
    
    // Test GET /stockItem on a database containing a single stockItem.
    String stockItemId = "StockItem-01";
    String warehouseId = "Warehouse-01";
    Warehouse warehouse = new Warehouse(warehouseId, "Storage");
    warehouse.save();
    Product product = new Product("Product-01", "soda", "pop");
    product.save();  
    StockItem stockitem = new StockItem(stockItemId, null, null, 100);
    stockitem.save();
    result = callAction(controllers.routes.ref.StockItem.index());
    assertTrue("One stockItem", contentAsString(result).contains(stockItemId));
        
    // Test GET /stockItem/StockItem-01
    result = callAction(controllers.routes.ref.StockItem.details(stockItemId));
    assertTrue("StockItem detail", contentAsString(result).contains(stockItemId));
    
    // Test GET /stockItem/BadStockItemId and make sure we get a 404
    result = callAction(controllers.routes.ref.StockItem.details("BadStockItemId"));
    assertEquals("StockItem detail (bad)", NOT_FOUND, status(result));
        
    // Test POST /stockItems (with simulated, invalid form data).
    Map<String, String> stockItemData = new HashMap<String, String>();
    stockItemData.put("stockItemId", "StockItem-02");
    stockItemData.put("warehouseId", null);
    stockItemData.put("productId", null);
    stockItemData.put("quantity", "500");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(stockItemData);
    result = callAction(controllers.routes.ref.StockItem.newStockItem(), request);
    assertEquals("Create new stockItem", BAD_REQUEST, status(result));
    
    // Test for current StockItem count after bad form input
    List<StockItem> stockitems = StockItem.find().findList();
    assertEquals("Checking stockitems", stockitems.size(), 1);
    
    // Test POST /stockItems (with simulated, valid form data).
    stockItemData.put("warehouseId", "Warehouse-01");
    stockItemData.put("productId", "Product-01");    
    request = fakeRequest();
    request.withFormUrlEncodedBody(stockItemData);
    result = callAction(controllers.routes.ref.StockItem.newStockItem(), request);
    assertEquals("Create new stockItem", OK, status(result));
    
    // Check for valid insertion
    stockitems = StockItem.find().findList();
    assertEquals("Checking stockitems", stockitems.size(), 2);
       
    // Test DELETE /stockItem/StockItem-01 (a valid StockItemId)
    result = callAction(controllers.routes.ref.StockItem.delete(stockItemId));
    assertEquals("Delete current stockItem OK", OK, status(result));
    result = callAction(controllers.routes.ref.StockItem.details(stockItemId));
    assertEquals("Deleted stockItem gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.StockItem.delete(stockItemId));
    assertEquals("Deleted missing stockItem also OK", OK, status(result));
  }

  @Test
  public void testWarehouseController() {
    // Test GET /warehouse on an empty database.
    Result result = callAction(controllers.routes.ref.Warehouse.index());
    assertTrue("Empty warehouses", contentAsString(result).contains("No warehouses"));
    
    // Test GET /warehouse on a database containing a single warehouse.
    String warehouseId = "Warehouse-01";
    Warehouse warehouse = new Warehouse(warehouseId,"Storage");
    warehouse.save();
    Address address = new Address("Address-01");
    address.save();
    
    result = callAction(controllers.routes.ref.Warehouse.index());
    assertTrue("One warehouse", contentAsString(result).contains(warehouseId));
    
    // Test GET /warehouse/Warehouse-01
    result = callAction(controllers.routes.ref.Warehouse.details(warehouseId));
    assertTrue("Warehouse detail", contentAsString(result).contains(warehouseId));
    
    // Test GET /warehouse/BadWarehouseId and make sure we get a 404
    result = callAction(controllers.routes.ref.Warehouse.details("BadWarehouseId"));
    assertEquals("Warehouse detail (bad)", NOT_FOUND, status(result));
        
    // Test POST /warehouses (with simulated, valid form data).
    Map<String, String> warehouseData = new HashMap<String, String>();
    warehouseData.put("warehouseId", "Warehouse-02");
    warehouseData.put("name", "Barn");
    warehouseData.put("addressId", "Address-01");
    FakeRequest request = fakeRequest();
    request.withFormUrlEncodedBody(warehouseData);
    result = callAction(controllers.routes.ref.Warehouse.newWarehouse(), request);
    assertEquals("Create new warehouse", OK, status(result));
    
    // Illustrates use of validate() method in models.Warehouse.
    request = fakeRequest();
    warehouseData.put("warehouseId", "Warehouse-03");
    warehouseData.put("name", "Shed");
    warehouseData.put("addressId", "Address-02");    
    request.withFormUrlEncodedBody(warehouseData);
    result = callAction(controllers.routes.ref.Warehouse.newWarehouse(), request);
    assertEquals("Create bad warehouse fails", BAD_REQUEST, status(result));
    
    // Test DELETE /warehouse/Warehouse-01 (a valid WarehouseId)
    result = callAction(controllers.routes.ref.Warehouse.delete(warehouseId));
    assertEquals("Delete current warehouse OK", OK, status(result));
    result = callAction(controllers.routes.ref.Warehouse.details(warehouseId));
    assertEquals("Deleted warehouse gone", NOT_FOUND, status(result));
    result = callAction(controllers.routes.ref.Warehouse.delete(warehouseId));
    assertEquals("Deleted missing warehouse also OK", OK, status(result));
  }
}

