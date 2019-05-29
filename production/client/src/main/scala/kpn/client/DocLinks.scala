// TODO migrate to Angular
package kpn.client

import kpn.client.RouteConfiguration.GotoGlossaryEntry
import kpn.client.common.Context

import scala.scalajs.js.annotation.JSExportTopLevel

object DocLinks {

  var contextOption: Option[Context] = None

  @JSExportTopLevel("docLink")
  def docLink(arg: String): Unit = {
    println(s"DocLink($arg)")
    contextOption match {
      case None => println("DocLink: context not set")
      case Some(context) => context.tempGetRouter.set(GotoGlossaryEntry(context.lang, arg)).runNow()
    }
  }
}
