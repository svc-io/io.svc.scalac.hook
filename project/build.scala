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
    settings = (buildSettings ++ corePluginSettings ++ examplePluginSettings)
  ) aggregate(core, examples)

  lazy val core: Project = Project(
    "scalac-hook",
    file("core"),
    settings = buildSettings
  )

  // Scalac command line options to install our compiler plugin.
  lazy val corePluginSettings = Seq(
    scalacOptions in Compile <++= (Keys.`package` in (core, Compile)) map { (jar: File) =>
      val addPlugin = "-Xplugin:" + jar.getAbsolutePath
      // add plugin timestamp to compiler options to trigger recompile of
      // examples after editing the plugin. (Otherwise a 'clean' is needed.)
      val dummy = "-JdummyCore=" + jar.lastModified
      Seq(addPlugin, dummy)
    }
  )

  lazy val examplePluginSettings = Seq(
    scalacOptions in Compile <++= (Keys.`package` in (examples, Compile)) map { (jar: File) =>
      val addPlugin = "-Xplugin:" + jar.getAbsolutePath
      val addHook = "-P:hook:io.svc.scalac.hook.example.traverser.Println.PrintlnHook"
      // add plugin timestamp to compiler options to trigger recompile of
      // examples after editing the plugin. (Otherwise a 'clean' is needed.)
      val dummy = "-JdummyExamples=" + jar.lastModified
      Seq(addPlugin, addHook, dummy)
    }
  )

  lazy val examples: Project = Project(
    "scalac-hook-examples",
    file("examples"),
    settings = buildSettings
  ) dependsOn(core)

}