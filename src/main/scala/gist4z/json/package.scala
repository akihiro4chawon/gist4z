package gist4z

import scalaz._
import Scalaz._
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._

import json._
import objects._

package object json extends GeneratedJSON {
  implicit def filesJSONR: JSONR[Seq[File]] = jsonr {
    case JObject(fs) => fs.map{case JField(k, v) => fromJSON[File](v)}
                          .sequence[PartialApply1Of2[ValidationNEL, Error]#Apply, File]
    case x => UnexpectedJSONError(x, classOf[JObject]).fail.liftFailNel
  }
  
  implicit def gistCreateFilesJSONW : JSONW[Seq[GistCreateFile]] = jsonw { files =>
    makeObj(files map { file => file.filename -> toJSON(Map("content" -> file.content))})
  }
  
  private def jsonr[A](f: JValue => Result[A]): JSONR[A] = new JSONR[A] {
    def read(jv: JValue) = f(jv)
  }
  
  private def jsonw[A](f: A => JValue): JSONW[A] = new JSONW[A] {
    def write(a: A) = f(a)
  }
}
