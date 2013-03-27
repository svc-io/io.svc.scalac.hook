package io.svc.scalac.hook.example.traverser

import io.svc.scalac.hook.component.TraverserHook.TraverserHook
import tools.nsc._

/**
 * @author Rintcius Blok
 */
object Println {

  class PrintlnHook(override val global: Global) extends TraverserHook(global) {
    println("PrintlnHook Created")
    override def traverse(prev: Phase, unit: global.CompilationUnit) = {
      println("PrintlnHook In Action: " + prev + " " + unit)
    }
  }

}
