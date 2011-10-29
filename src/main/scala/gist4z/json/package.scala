package gist4z

import scalaz._
import Scalaz._
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import objects._

package object json extends GeneratedJSON {
  implicit def filesJSONR: JSONR[Seq[File]] = new JSONR[Seq[File]] {
    def read(json: JValue) = json match {
      case JObject(fs) => fs.map{case JField(k, v) => fromJSON[File](v)}
                            .sequence[PartialApply1Of2[ValidationNEL, Error]#Apply, File]
      case x => UnexpectedJSONError(x, classOf[JObject]).fail.liftFailNel
    }
  }
}
