package controllers;

import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import static play.data.Form.form;

public class Product extends Controller{
  
  public static Result index() {
    List<models.Product> products = models.Product.find().findList();
    return ok(products.isEmpty() ? "No products" : products.toString());
  }
  
  public static Result details(String productId) {
    models.Product product = models.Product.find().where().eq("productId", productId).findUnique();
    return (product == null) ? notFound("No product found") : ok(product.toString());
  }
  
  public static Result newProduct() {
    // Create a new Product form and bind the request variables to it..
    Form<models.Product> productForm = form(models.Product.class).bindFromRequest();
    if(productForm.hasErrors()) {
      return badRequest("Tag name cannot be 'tag'.");
    }
    models.Product product = productForm.get();
    product.save();
    return ok(product.toString());
  }
  
  public static Result delete(String productId) {
    models.Product product = models.Product.find().where().eq("productId", productId).findUnique();
    if (product != null) {
      product.delete();
    }
    return ok();
  }
}
