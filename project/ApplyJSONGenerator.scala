import sbt._

trait ApplyJSONGeneartor {
  def generateApplyJSON(dir: File) = {
    val source = """
package gist4z.json
import scalaz._
import Scalaz._
import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._

object ApplyJSON {
  trait `HaskellStyle<$>`[A, B] {
    protected def func: A => B
    def `<$>`[M[_] : Functor](arg: M[A]): M[B] = arg map func
  }
  implicit def `pimpHaskellStyle<$>`[A, B](f: A => B): `HaskellStyle<$>`[A, B] =
    new `HaskellStyle<$>`[A, B] { override def func = f }

  trait `HaskellStyle<*>`[M[_], A, B] {
    protected def func: M[A => B]
    def `<*>`(arg: M[A])(implicit a: Apply[M]): M[B] = a(func, arg)
  }
  implicit def `pimpHaskellStyle<*>`[M[_], A, B](f: M[A => B]): `HaskellStyle<*>`[M, A, B] =
    new `HaskellStyle<*>`[M, A, B] { override def func = f }
"""+(2 to 22 map generateApplyJsonSingle mkString "\n")+"""
}
"""
    val file = (dir / "gist4z" / "json" / "ApplyJSON.scala").asFile
    IO.write(file, source)
    Seq(file)
  }

  private def generateApplyJsonSingle(arity: Int) = {
    val lowerAlpha = 'a' until ('a' + arity).toChar
    val upperAlpha = lowerAlpha map {_.toUpper}
    val typeParameters = upperAlpha map (_ + ": JSONR") mkString ", "
    val parameters = lowerAlpha map {c => c + ": JValue => Result[" + c.toUpper + "]"} mkString ", "
    val applicByBuilder = lowerAlpha map (_ + "(json)") mkString " |@| "
    val applicByCurried = lowerAlpha map (_ + "(json)") mkString " <*> "

    val funcBody = if (arity <= 12)
      "("+applicByBuilder+")(z)"
    else
      "z.curried `<$>` "+applicByCurried

      <qq>
  implicit def Func{arity}ToJSON[{typeParameters}, Ret](z: ({upperAlpha mkString ", "}) => Ret) = new &#123;
    def applyJSON({parameters}): JValue => Result[Ret] =
      (json: JValue) => {funcBody}
  &#125;</qq>.text
  }
}


