package gist4z.api

import scalaz._
import Scalaz._

object QueryParam {
  implicit def toTuple2[A](param: QueryParam[A]): (String, String) = param.toParam
}

abstract class QueryParam[+A] {
  def key: String
  def value: A
  final def toParam: (String, String) = (key, paramString)
  protected def paramString = value.toString  // default implementation, supposed to be overridden
}


