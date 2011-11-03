package gist4z.api

import net.liftweb.json.scalaz.JsonScalaz._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// TODO add "Custom Mime Types" support
// Gist Comments API
trait Comments {

  // List comments on a gist
  // GET /gists/:gist_id/comments
  def list(gistId: String)(implicit auth: Auth) =
    get[List[Comment]](_/"gists"/gistId/"comments")
  
  // Get a single comment
  // GET /gists/comments/:id
  def byId(id: Long)(implicit auth: Auth) = 
    get[Comment](_/"gists/comments"/id.toString)
  
  // Create a comment
  // POST /gists/:gist_id/comments
  def create(gistId: String, body: String)(implicit auth: SomeAuth) =
    post[Comment](_/"gists"/gistId/"comments", toJSON(Map("body" -> body)))
    
  // Edit a comment
  // PATCH /gists/comments/:id
  def edit(id: Long, body: String)(implicit auth: SomeAuth) =
    patch[Comment](_/"gists/comments"/id.toString, toJSON(Map("body" -> body)))
    
  // Delete a comment
  // DELETE /gists/comments/:id
  def remove(id: Long)(implicit auth: SomeAuth) = 
    delete(_/"gists/comments"/id.toString)
}
