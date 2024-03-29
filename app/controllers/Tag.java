package controllers;

import java.util.List;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import static play.data.Form.form;

public class Tag extends Controller{
  
  public static Result index() {
    List<models.Tag> tags = models.Tag.find().findList();
    return ok(tags.isEmpty() ? "No Tags" : tags.toString());
  }
  
  public static Result details(String tagId) {
    models.Tag tag = models.Tag.find().where().eq("tagId", tagId).findUnique();
    return (tag == null) ? notFound("No tag found") : ok(tag.toString());
  }
  
  public static Result newTag() {
    // Create a new Tag form and bind the request variables to it..
    Form<models.Tag> tagForm = form(models.Tag.class).bindFromRequest();
    if(tagForm.hasErrors()) {
      return badRequest("Tag ID and name is required.");
    }
    models.Tag tag = tagForm.get();
    tag.save();
    return ok(tag.toString());
  }
  
  public static Result delete(String tagId) {
    models.Tag tag = models.Tag.find().where().eq("tagId", tagId).findUnique();
    if (tag != null) {
      tag.delete();
    }
    return ok();
  }
}