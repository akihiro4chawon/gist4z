package gist4z.api

import net.liftweb.json.scalaz.JsonScalaz._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// Trees API
trait Trees {
  // Get a Tree
  // GET /repos/:user/:repo/git/trees/:sha
  
  // Get a Tree Recursively
  // GET /repos/:user/:repo/git/trees/:sha?recursive=1
  
  // Create a Tree
  // The tree creation API will take nested entries as well. If both a tree and a nested path modifying that tree are specified, it will overwrite the contents of that tree with the new path contents and write a new tree out.
  // POST /repos/:user/:repo/git/trees
}