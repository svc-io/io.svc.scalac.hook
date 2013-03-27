package io.svc.scalac.hook

import component.TraverserHook.TraverserHookComponent
import scala.tools.nsc.Global
import tools.nsc.plugins.{PluginComponent, Plugin}
import scala.reflect.runtime.{universe => ru}

/**
 * @author Rintcius Blok
 */

class HookPlugin(val global: Global) extends Plugin {

  val name = "hook"

  //TODO how to get all global phase names?
  val runsAfter = List[String]("refchecks", "typer")

  val description = "scalac hook"

  override val optionsHelp = Some(s"  -P:$name:<hookName>")

  var hookNames: List[String] = Nil

  override def processOptions(options: List[String], error: String => Unit) {
    hookNames = options
  }

  // TODO val components cannot depend on options it seems..
  // lazy val components = hookNames map (newComponent)
  // so let's just create 1 without hookName as parameter
  val components = List(newComponent())


  def newComponent(/*hookName: String*/): PluginComponent = {
    new TraverserHookComponent(global, this /*, hookName*/)
  }
}


