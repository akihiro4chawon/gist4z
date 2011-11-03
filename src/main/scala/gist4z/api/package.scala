package gist4z

import gist4z.json._
import gist4z.auth.Auth

import scalaz._
import Scalaz._
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import dispatch._

package object api {
  val apiRoot = :/("api.github.com").secure
  
  private implicit val HttpResource: Resource[Http] = resource { _.shutdown() }
  
  def http[T](f: Http => T): T = withResource(new Http, f)
  
  def jsonToString(json: JValue): String =
    Printer.compact(JsonAST.render(json))
  
  def get[A](endpoint: Request => Request, params: QueryParam[_]*)(
      implicit auth: Auth, jsonr: JSONR[A]): Result[A] = {
    fromJSON[A](parse(http(_(auth(endpoint(apiRoot)) as_str))))
  }
  
  def put[A](endpoint: Request => Request, params: QueryParam[_]*)(
      implicit auth: Auth, jsonr: JSONR[A]): Result[A] = {
    get(endpoint andThen (_.PUT), params :_*)
  }
  
  def post[A](endpoint: Request => Request, json: JValue)(
      implicit auth: Auth, jsonr: JSONR[A]): Result[A] = {
    fromJSON[A](parse(http(_(auth(endpoint(apiRoot)) << jsonToString(json) as_str))))
  }
  
  def postNothing[A](endpoint: Request => Request)(
      implicit auth: Auth, jsonr: JSONR[A]): Result[A] = {
    fromJSON[A](parse(http(_(auth(endpoint(apiRoot)).POST as_str))))
  }
  
  def patch[A](endpoint: Request => Request, json: JValue)(
      implicit auth: Auth, jsonr: JSONR[A]): Result[A] = {
    post(endpoint andThen (_.copy(method = "PATCH")), json)
  }
}
