import sbt._

import java.util.{List => JList}
import net.java.amateras.xlsbeans._
import net.java.amateras.xlsbeans.annotation._

import scala.annotation.target.beanSetter
import scala.collection.JavaConversions._
import scala.reflect.BeanProperty

trait CaseClassGenerator {

  def generateCaseClasses(xlsdir: File, dir: File) = {
    val xls = (xlsdir / "template" / "classes.xls").asFile
    val fis = new java.io.FileInputStream(xls)
    val caseClasses = (new XLSBeans).load(fis, classOf[CaseClasses])
    
    val packageFile = (dir / "gist4z" / "json" / "GeneratedJSON.scala").asFile
    def packageSource(caseClassses: CaseClasses) = """
package gist4z.objects

import scalaz._
import Scalaz._
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import gist4z.json._
import gist4z.json.ApplyJSON._
      
trait GeneratedJSON {
"""+(caseClasses.caseClassTableList map {_.sjsonDecl} mkString "\n")+"""
}
"""

    def objectSource(caseClass: CaseClass) = 
      "package gist4z.objects\n"+caseClass.scalaDecl
    def objectFile(caseClass: CaseClass) =
      (dir / "gist4z" / "objects" / (caseClass.className+".scala")).asFile
    
    IO.write(packageFile, packageSource(caseClasses))
    for (caseClass <- caseClasses.caseClassTableList)
      IO.write(objectFile(caseClass), objectSource(caseClass))
      
    packageFile +: (caseClasses.caseClassTableList map objectFile)
  }
}

@Sheet(name = "ケースクラス一覧")
class CaseClasses {
  @(IterateTables @beanSetter)(tableLabel="フィールド一覧", tableClass=classOf[CaseClass], bottom=2) @BeanProperty
  var caseClassTableList: JList[CaseClass] = _
}

class CaseClass {
  @(LabelledCell @beanSetter)(label="クラス名", `type`=LabelledCellType.Right) @BeanProperty
  var className: String = _
  @(LabelledCell @beanSetter)(label="JSON", `type`=LabelledCellType.Right) @BeanProperty
  var json: String = _
  @(HorizontalRecords @beanSetter)(tableLabel="フィールド一覧", recordClass=classOf[Field], bottom=2) @BeanProperty
  var fields: JList[Field] = _
  
//  def scalaDecl = fields.map{_.scalaDecl}.mkString("case class "+className+" {\n  ", "\n  ", "\n}\n")
  def scalaDecl = fields.map{_.scalaDecl}.mkString("case class "+className+"(\n  ", ",\n  ", ")\n")
  def sjsonDecl = json.toLowerCase match {
    case "rw" | "wr" => sjsonrDecl + sjsonwDecl
    case "r" => sjsonrDecl
    case "w" => sjsonwDecl
    case "" => ""
  }
  
  def sjsonrDecl = """
  implicit def """+className+"JSONR: JSONR["+className+"""] =
     """+className+".applyJSON("+fields.map{"field(\""+_.fieldName+"\")"}.mkString(", ")+")"
     
  private def pair = fields map {f => <qq>("{f.fieldName}" -> toJSON(a.{f.valName}))</qq>.text}
  def sjsonwDecl = """
  implicit def """+className+"JSONW: JSONW["+className+"""] = new JSONW["""+className+"""] {
    def write(a: """+className+""") = 
      """+pair.mkString("makeObj(", " :: ", " :: Nil)")+"""
  }"""
}

class Field {
  @(Column @beanSetter)(columnName="No") @BeanProperty
  var no: Int = _
  @(Column @beanSetter)(columnName="フィールド名") @BeanProperty
  var fieldName: String = _
  @(Column @beanSetter)(columnName="型名") @BeanProperty
  var typeName: String = _
  
  def valName: String =
    "_.".r replaceAllIn(fieldName, _.matched.tail.toUpperCase)
  
  def scalaDecl = "val "+valName+" : "+typeName
}

