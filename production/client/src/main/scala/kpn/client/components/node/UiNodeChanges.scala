// Migrated to Angular: _node-changes-page.component.ts
package kpn.client.components.node

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls
import kpn.client.common.Nls.nls
import kpn.client.common.User
import kpn.client.components.common.UiHistoryIncompleteWarning
import kpn.client.components.common.UiItems
import kpn.shared.node.NodeChangeInfos

object UiNodeChanges {

  private case class Props(context: Context, nodeChangeInfos: NodeChangeInfos)

  private val component = ScalaComponent.builder[Props]("node-changes")
    .render_P { props =>
      new Renderer(props.nodeChangeInfos)(props.context).render()
    }
    .build

  def apply(nodeChangeInfos: NodeChangeInfos)(implicit context: Context): VdomElement = {
    component(Props(context, nodeChangeInfos))
  }

  private class Renderer(nodeChangeInfos: NodeChangeInfos)(implicit context: Context) {

    def render(): VdomElement = {

      if (nodeChangeInfos.changes.nonEmpty) {
        <.div(
          <.h4(nls("History", "Historiek")),
          User.get match {
            case None =>
              if (Nls.nlsNL) {
                <.div(
                  <.br(),
                  <.i(
                    "De node historiek is enkel beschikbaar voor OpenStreetMap gebruikers, na ",
                    context.gotoLogin(),
                    "."
                  )
                )
              }
              else {
                <.div(
                  <.br(),
                  <.i(
                    "The node history is available to registered OpenStreetMap contributors only, after ",
                    context.gotoLogin(),
                    "."
                  )
                )
              }

            case Some(user) =>

              <.div(
                UiItems {
                  nodeChangeInfos.changes.zipWithIndex.map { case (nodeChange, index) =>
                    UiNodeChange(index.toString, nodeChange)
                  }
                },
                TagMod.when(nodeChangeInfos.incompleteWarning) {
                  UiHistoryIncompleteWarning()
                }
              )
          }
        )
      }
      else {
        <.div(nls("No history", "Geen historiek"))
      }
    }
  }

}
