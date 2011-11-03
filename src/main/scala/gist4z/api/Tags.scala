package gist4z.api

import net.liftweb.json.scalaz.JsonScalaz._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// Tags API
// This tags api only deals with tag objects - so only annotated tags, not lightweight tags.
trait Tags {
  // Get a Tag
  // GET /repos/:user/:repo/git/tags/:sha  
  def apply(user: String, repo: String, sha: String)(implicit a: SomeAuth) =
    get[Tag](_/"repos"/user/repo/"git/tags"/sha)
  
  // Create a Tag Object
  // POST /repos/:user/:repo/git/tags
  def create(user: String, repo: String, ct: CreateTag)(implicit a: SomeAuth) =
    post[Tag](_/"repos"/user/repo/"git/tags", toJSON(ct))
}
