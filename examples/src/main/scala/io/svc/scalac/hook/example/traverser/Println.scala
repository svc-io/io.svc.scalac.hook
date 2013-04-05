package io.svc.scalac.hook.example.traverser

import io.svc.scalac.hook.component.TraverserHook.TraverserHook
import tools.nsc._

/**
 * @author Rintcius Blok
 */
object Println {

  class PrintlnHook[G <: Global](override val global: G, val prev: Phase) extends TraverserHook(global, prev) {
    println("PrintlnHook Created")
    override val traverser = new global.Traverser {
      override def traverse(tree: global.Tree): Unit = {
        println(s"PrintlnHook In Action: $prev ${tree.symbol}")
      }
    }
  }

}
