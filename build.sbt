name := "Gist4z"

version := "0.1"

organization := "gist4z"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "6.0.3",
  "org.scalaj" %% "scalaj-http" % "0.2.9",
  "net.databinder" %% "dispatch-core" % "0.8.6",
  "net.databinder" %% "dispatch-http" % "0.8.6",
  "net.liftweb" %% "lift-json-scalaz" % "2.4-M4",
  "net.liftweb" %% "lift-json-ext" % "2.4-M4",
  "net.liftweb" %% "lift-json" % "2.4-M4",
  "org.specs2"  %% "specs2" % "1.6.1" % "test",
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test"
) map { _.withSources() }

addCompilerPlugin("org.scala-tools.sxr" % "sxr_2.9.0" % "0.2.7")

scalacOptions <+= scalaSource in Compile map { "-P:sxr:base-directory:" + _.getAbsolutePath }

scalacOptions += "-deprecation"

Boilerplate.genJsonApply <<= (sourceManaged in Compile) map { (dir) =>
  Boilerplate.generateApplyJSON(dir)
}

Boilerplate.genCaseClasses <<= (sourceManaged in Compile) map { (dir) =>
  Boilerplate.generateCaseClasses(file("."), dir)
}

(sourceGenerators in Compile) <+= Boilerplate.genJsonApply

(sourceGenerators in Compile) <+= Boilerplate.genCaseClasses
