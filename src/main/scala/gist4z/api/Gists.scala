package gist4z.api

import _root_.scalaz._
import _root_.scalaz.Scalaz._

import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._

import dispatch._

import gist4z.auth.{Auth, NullAuth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._


// TODO catch thrown exception by dispatch and wrap with Validation

// Gists API
trait Gists {
  // List a user’s gists:
  // GET /users/:user/gists
  def users(user: String)(implicit auth: Auth) =
    get[List[Gist]](_/"users"/user/"gists")
  
  // List the authenticated user’s gists or if called anonymously, this will returns all public gists:
  // GET /gists
  def mine()(implicit auth: SomeAuth) =
    get[List[Gist]](_/"gists")
  
  // List all public gists:
  // GET /gists/public
  def public()(implicit auth: Auth) =
    get[List[Gist]](_/"gists/public")
  
  // List the authenticated user’s starred gists:
  // GET /gists/starred
  def starred()(implicit auth: SomeAuth) =
    get[List[Gist]](_/"gists/starred")
  
  // Get a single gist
  // GET /gists/:id
  def byId(id: String)(implicit auth: Auth) =
    get[ExtGist](_/"gists"/id)
  
  // Create a gist
  // POST /gists
  def create(gistCreate: GistCreate)(implicit auth: SomeAuth) =
    post[ExtGist](_/"gists", toJSON(gistCreate))
  
  // Edit a gist
  // PATCH /gists/:id
  def edit(gistCreate: GistCreate)(implicit auth: SomeAuth) =
    patch[ExtGist](_/"gists", toJSON(gistCreate))

  // Star a gist
  // PUT /gists/:id/star
  def star(id: String)(implicit auth: SomeAuth) = 
    http(_(auth((apiRoot/"gists"/id/"star").PUT) >|))

  // Unstar a gist 
  // DELETE /gists/:id/star
  def unstar(id: String)(implicit auth: SomeAuth) = 
    http(_(auth((apiRoot/"gists"/id/"star").DELETE) >|))
    
  // Check if a gist is starred
  // GET /gists/:id/star
  def isStarred(id: String)(implicit auth: SomeAuth): Validation[Exception, Boolean] =
    http {_ x auth(apiRoot/"gists"/id/"star") >:+ { (headers, req) =>
      (headers get "status") flatMap {_.headOption} map {_ take 3} collect {
        case "204" => Handler(req, (_, _, _) => true.success[Exception])
        case "404" => req >- { str =>
          fromJSON[ApiErrorMessage](parse(str)).fold(
              _ => false.success[Exception], // "Star Not Found"
              m => new StatusCode(404, m.message+": "+id).fail[Boolean]) // "Gist Not Found"
        }
      } getOrElse {req >> { _ => (new Exception("Unknown error: " + headers)).fail[Boolean]}}
    }}

  // Fork a gist
  // POST /gists/:id/fork
  def fork(id: String)(implicit auth: SomeAuth) =
    postNothing[ExtGist](_/"gists"/id/"fork")
    
  // Delete a gist
  // DELETE /gists/:id
  def delete(id: String)(implicit auth: SomeAuth) = 
    http(_(auth(apiRoot/"gists"/id).DELETE >|))
}
