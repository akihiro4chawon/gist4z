package gist4z.api

import _root_.scalaz._
import _root_.scalaz.Scalaz._

import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._

import dispatch._

import gist4z.auth.{Auth, SomeAuth}
import gist4z.objects._
import gist4z.json._
import gist4z.api._

// User Emails API
// Mangement of email addresses via the API requires that you are authenticated.
trait UserEmails {
  // List email addresses for a user
  // GET /user/emails
  def list()(implicit auth: SomeAuth) =
    get[List[String]](_/"user/emails")
  
  // Add email address(es)
  // POST /user/emails
  def add(emails: List[String])(implicit auth: SomeAuth) =
    post[List[String]](_/"user/emails", toJSON(emails))
  // You can post a single email address or an array of addresses:
  def add(email: String)(implicit auth: SomeAuth) =
    post[List[String]](_/"user/emails", toJSON(email))
  
  // Delete email address(es)
  // DELETE /user/emails
  def delete(emails: List[String])(implicit auth: SomeAuth) =
    http(_(auth((apiRoot/"user/emails").DELETE) << (toJSON(emails).toString) >|))
  // You can include a single email address or an array of addresses:
  def delete(email: String)(implicit auth: SomeAuth) =
    http(_(auth((apiRoot/"user/emails").DELETE) << (toJSON(email).toString) >|))
}
