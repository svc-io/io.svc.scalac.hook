package io.svc.scalac.hook.component

import io.svc.scalac.hook.HookPlugin
import tools.nsc._
import tools.nsc.plugins.PluginComponent
import scala.reflect.runtime.{universe => ru}

/**
 * @author Rintcius Blok
 */
object TraverserHook {

  class TraverserHookComponent(val global: Global, val plugin: HookPlugin/*, val hookName: String*/) extends PluginComponent {

    import global._
    import global.definitions._

    //TODO can hookName be injected to the component instead? this is just a quick hack
    lazy val hookName = plugin.hookNames(0)

    val runsAfter = plugin.runsAfter

    val phaseName = "traverserhook"

    def newPhase(prev: Phase): Phase = new TraverserHookPhase(prev)

    class TraverserHookPhase(prev: Phase) extends StdPhase(prev) {
      def apply(unit: CompilationUnit) {
        newTraverser(prev, unit).traverse(unit.body)
      }
    }

    def newTraverser(prev: Phase, unit: CompilationUnit): Traverser = {
      new Traverser {
        override def traverse(tree: Tree): Unit = {
          newHook(hookName).traverse(prev, unit)
        }
      }
    }

    implicit def newHook(name: String): TraverserHook[global.type] = createHook[global.type](name)

    def createHook[G <: Global](name: String): TraverserHook[G] = {
      val mirror = ru.runtimeMirror(getClass.getClassLoader)

      val classSym = mirror.staticClass(name)
      val classMirror = mirror.reflectClass(classSym)

      val ctor = classSym.typeSignature.member(ru.nme.CONSTRUCTOR).asMethod
      val ctorMirror = classMirror.reflectConstructor(ctor)

      ctorMirror.apply(global).asInstanceOf[TraverserHook[G]]
    }
  }

  implicit def TraverserHook(global: Global): TraverserHook[global.type] = new TraverserHook[global.type](global)

  // Cannot get it working with an abstract class here..
  class TraverserHook[G <: Global](val global: G) {
     def traverse(prev: Phase, unit: global.CompilationUnit): Unit = ???
  }

  class TmpHook(override val global: Global) extends TraverserHook(global) {
    println("TmpHook Created")
    override def traverse(prev: Phase, unit: global.CompilationUnit) = {
      println("TmpHook In Action: " + prev + " " + unit)
    }
  }

}
