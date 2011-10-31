package gist4z.auth

import dispatch._

trait Auth {
  def apply(req: Request): Request
}

case object NullAuth extends Auth {
  def apply(req: Request) = req
}

trait SomeAuth extends Auth

trait Basic extends SomeAuth {
  def user: String
  def password: String
}

object Basic {
  def apply(user: String, password: String): Basic =
    BasicUnconditionally(user, password)
}

case class BasicOrDigestIfRequired(user: String, password: String) extends Basic {
  def apply(req: Request) = req.as(user, password)
}

case class BasicUnconditionally(user: String, password: String) extends Basic {
  def apply(req: Request) = req.as_!(user, password)
}



//import scalaj.http._
//
//trait Auth {
//  def authenticate(req: Http.Request): Http.Request
//}
//
//case object NullAuth extends Auth {
//  def authenticate(req: Http.Request): Http.Request = req
//}
//
//trait SomeAuth extends Auth
//
//case class Basic(val user: String, val password: String) extends SomeAuth {
//  def authenticate(req: Http.Request) =
//    req.auth(user, password)
//}
//
//// TODO: revise when Javascript/Desktop Flow is enabled
//case class OAuth2(val accessToken: String) extends SomeAuth {
//  def authenticate(req: Http.Request) =
//    req.param("access_token", accessToken)
//}

