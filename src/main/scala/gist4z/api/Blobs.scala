package gist4z.api

import net.liftweb.json.scalaz.JsonScalaz._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// TODO add "Custom Mime Types" support
// TODO type issue: an encoding parameter that can be either utf-8 or base64. do we need custom ADTs? 
// TODO how to handle and return `Location` header as gist4z api?
// Gist Comments API

// Blobs API
trait Blobs {

  // Get a Blob
  // GET /repos/:user/:repo/git/blobs/:sha
  def apply(user: String, repo: String, sha: String)(implicit a: SomeAuth) =
    get[Blob](_/"repos"/user/repo/"git/blobs"/sha)

  // Create a Blob
  // POST /repos/:user/:repo/git/blobs
  def create(user: String, repo: String, blob: Blob)(implicit a: SomeAuth) =
    post[BlobResponce](_/"repos"/user/repo/"git/blobs", toJSON(blob))
}

