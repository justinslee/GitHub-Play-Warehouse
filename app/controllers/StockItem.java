package controllers;

import static play.data.Form.form;

import java.util.List;

import models.Product;
import models.Warehouse;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class StockItem extends Controller{
  
  public static Result index() {
    List<models.StockItem> stockitems = models.StockItem.find().findList();
    return ok(stockitems.isEmpty() ? "No stockitems" : stockitems.toString());
  }
  
  public static Result details(String stockItemId) {
    models.StockItem stockItem = models.StockItem.find().where().eq("stockItemId", stockItemId).findUnique();
    return (stockItem == null) ? notFound("No stockitem found") : ok(stockItem.toString());
  }

  
  public static Result newStockItem() {
    // Create a new StockItem form and bind the request variables to it..
    Form<models.StockItem> stockItemForm = form(models.StockItem.class).bindFromRequest();
    if(stockItemForm.hasErrors()) {
      return badRequest("StockItem has an error.");
    }
    models.StockItem stockitem = stockItemForm.get();
    Warehouse warehouse = Warehouse.find().where().eq("warehouseId", stockitem.getWarehouseId()).findUnique();
    Product product = Product.find().where().eq("productId", stockitem.getProductId()).findUnique();
    stockitem.setProduct(product);
    stockitem.setWarehouse(warehouse);
    stockitem.save();
    return ok(stockitem.toString());
  }
  
  public static Result delete(String stockItemId) {
    models.StockItem stockItem = models.StockItem.find().where().eq("stockItemId", stockItemId).findUnique();
    if (stockItem != null) {
      stockItem.delete();
    }
    return ok();
  }
}