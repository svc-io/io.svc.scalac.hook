import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "io.svc",
    version := "0.1-SNAPSHOT",

    scalaVersion := "2.10.1",
    scalacOptions ++= Seq(),

    licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
    homepage := Some(url("https://github.com/svc-io/io.svc.scalac.hook")),

    libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _),

    publishMavenStyle := true,
    pomIncludeRepository := { x => false }
  )
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings
  ) aggregate(core, examples)

  lazy val core: Project = Project(
    "scalac-hook",
    file("core"),
    settings = buildSettings
  )

  lazy val examples: Project = Project(
    "scalac-hook-examples",
    file("examples"),
    settings = buildSettings
  ) dependsOn(core)

}