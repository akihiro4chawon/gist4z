package gist4z.api

import net.liftweb.json.scalaz.JsonScalaz._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// Commits API
trait Commits {

  // Get a Commit
  // GET /repos/:user/:repo/git/commits/:sha  
  def apply(user: String, repo: String, sha: String)(implicit a: SomeAuth) = 
    get[Commit](_/"repos"/user/repo/"git/commits"/sha)
  
  // Create a Commit
  // POST /repos/:user/:repo/git/commits    
  def create(user: String, repo: String, createCommit: CreateCommit)(implicit a: SomeAuth) = 
    post[Commit](_/"repos"/user/repo/"git/commits", toJSON(createCommit))
}

