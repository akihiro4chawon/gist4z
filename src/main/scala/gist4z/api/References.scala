package gist4z.api

import net.liftweb.json.scalaz.JsonScalaz._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// Commits API
// TODO: how to handle and return `location` header?
trait References {
  //Get a Reference
  //GET /repos/:user/:repo/git/refs/:ref
  def apply(user: String, repo: String, ref: String)(implicit a: SomeAuth) =
    get[Reference](_/"repos"/user/repo/"git/refs"/ref)
  
  //Get all References
  //GET /repos/:user/:repo/git/refs
  def apply(user: String, repo: String)(implicit a: SomeAuth) =
    get[List[Reference]](_/"repos"/user/repo/"git/refs")
  
  // TODO check if the server returns Reference (or not?)
  //Create a Reference
  //POST /repos/:user/:repo/git/refs
  def create(user: String, repo: String, cr: CreateReference)(implicit a: SomeAuth) =
    post[Reference](_/"repos"/user/repo/"git/refs", toJSON(cr))
  
  //Update a Reference
  //PATCH /repos/:user/:repo/git/refs/:ref
  def update(user: String, repo: String, ref: String, ur: UpdateReference)(implicit a: SomeAuth) =
    patch[Reference](_/"repos"/user/repo/"git/refs"/ref, toJSON(ur))

}