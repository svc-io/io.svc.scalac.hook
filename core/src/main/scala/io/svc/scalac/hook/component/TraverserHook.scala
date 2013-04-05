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

    def newTraverser(prev: Phase, unit: CompilationUnit): Traverser = newHook(hookName, prev).traverser

    implicit def newHook(name: String, prev: Phase): TraverserHook[global.type] = createHook[global.type](name, prev)

    def createHook[G <: Global](name: String, prev: Phase): TraverserHook[G] = {
      val mirror = ru.runtimeMirror(getClass.getClassLoader)

      val classSym = mirror.staticClass(name)
      val classMirror = mirror.reflectClass(classSym)

      val ctor = classSym.typeSignature.member(ru.nme.CONSTRUCTOR).asMethod
      val ctorMirror = classMirror.reflectConstructor(ctor)

      ctorMirror.apply(global, prev).asInstanceOf[TraverserHook[G]]
    }
  }

  // Cannot get it working with an abstract class here..
  class TraverserHook[G <: Global](val global: G, prev: Phase) {
    val traverser: global.Traverser = new global.Traverser
  }
}
