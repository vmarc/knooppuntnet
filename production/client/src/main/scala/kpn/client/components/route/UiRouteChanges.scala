// Migrated to Angular: _route-changes-page.component.ts
package kpn.client.components.route

import japgolly.scalajs.react.ScalaComponent
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
import kpn.shared.route.RouteChangeInfos

object UiRouteChanges {

  private case class Props(context: Context, routeChangeInfos: RouteChangeInfos)

  private val component = ScalaComponent.builder[Props]("route-changes")
    .render_P { props =>
      new Renderer(props.routeChangeInfos)(props.context).render()
    }
    .build

  def apply(routeChangeInfos: RouteChangeInfos)(implicit context: Context): VdomElement = {
    component(Props(context, routeChangeInfos))
  }

  private class Renderer(routeChangeInfos: RouteChangeInfos)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        <.h4(nls("History", "Historiek")),
        User.get match {
          case None =>
            if (Nls.nlsNL) {
              <.div(
                <.br(),
                <.i(
                  "De route historiek is enkel beschikbaar voor OpenStreetMap gebruikers, na ",
                  context.gotoLogin(),
                  "."
                )
              )
            }
            else {
              <.div(
                <.br(),
                <.i(
                  "The route history is available to registered OpenStreetMap contributors only, after ",
                  context.gotoLogin(),
                  "."
                )
              )
            }

          case Some(user) =>

            <.div(
              UiItems {
                routeChangeInfos.changes.zipWithIndex.map { case (routeChangeInfo, index) =>
                  UiRouteChange(routeChangeInfo, index)
                }
              },
              TagMod.when(routeChangeInfos.incompleteWarning) {
                UiHistoryIncompleteWarning()
              }
            )
        }
      )
    }
  }

}
