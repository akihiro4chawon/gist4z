package gist4z

import scalaz._
import Scalaz._
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._

import json._
import objects._

package object json extends GeneratedJSON {
  import net.liftweb.json.scalaz.JsonScalaz.Result
  
  implicit def filesJSONR: JSONR[Seq[File]] = jsonr {
    case JObject(fs) => fs.map{case JField(k, v) => fromJSON[File](v)}
                          .sequence[Result, File]
    case x => UnexpectedJSONError(x, classOf[JObject]).fail.liftFailNel
  }
  
  implicit def filesWithContentJSONR: JSONR[Seq[FileWithContent]] = jsonr {
    case JObject(fs) => fs.map{case JField(k, v) => fromJSON[FileWithContent](v)}
                          .sequence[Result, FileWithContent]
    case x => UnexpectedJSONError(x, classOf[JObject]).fail.liftFailNel
  }
  
  implicit def gistCreateFilesJSONW : JSONW[Seq[GistCreateFile]] = jsonw { files =>
    makeObj(files map { file => file.filename -> toJSON(Map("content" -> file.content))})
  }
  
  // Yields empty list instead of Failure when given JValue is JNothing.
  // A List can be empty, unlike NonEmptyList.
  implicit def listJSONR[A: JSONR]: JSONR[List[A]] = new JSONR[List[A]] {
    def read(json: JValue) = json match {
      case JArray(xs) => 
        xs.map(fromJSON[A]).sequence[PartialApply1Of2[ValidationNEL, Error]#Apply, A]
      case JNothing => List().success
      case x => UnexpectedJSONError(x, classOf[JArray]).fail.liftFailNel
    }
  }
  
  private def jsonr[A](f: JValue => Result[A]): JSONR[A] = new JSONR[A] {
    def read(jv: JValue) = f(jv)
  }
  
  private def jsonw[A](f: A => JValue): JSONW[A] = new JSONW[A] {
    def write(a: A) = f(a)
  }
}
