import sbt._

object Boilerplate extends ApplyJSONGeneartor with CaseClassGenerator {
  val genJsonApply = TaskKey[Seq[File]]("generate-json-apply")
  val genCaseClasses = TaskKey[Seq[File]]("generate-case-classes")
}

