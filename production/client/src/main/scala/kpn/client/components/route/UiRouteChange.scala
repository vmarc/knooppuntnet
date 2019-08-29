// Migrated to Angular: route-change.component.ts
package kpn.client.components.route

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiDetail
import kpn.client.components.shared.UiChangeSet
import kpn.client.components.shared._UiChangeSetInfo
import kpn.shared.route.RouteChangeInfo

object UiRouteChange {

  private case class Props(context: Context, routeChangeInfo: RouteChangeInfo, index: Int)

  private val component = ScalaComponent.builder[Props]("route-change")
    .render_P { props =>
      new Renderer(props.routeChangeInfo, props.index)(props.context).render()
    }
    .build

  def apply(entry: RouteChangeInfo, index: Int)(implicit context: Context): VdomElement = {
    component(Props(context, entry, index))
  }

  private class Renderer(routeChangeInfo: RouteChangeInfo, index: Int)(implicit context: Context) {

    private val key = "route-" + routeChangeInfo.id + "-" + index

    def render(): VdomElement = {
      UiChangeSet(
        routeChangeInfo.changeKey,
        routeChangeInfo.happy,
        routeChangeInfo.investigate,
        routeChangeInfo.comment,
        contents
      )
    }

    private def contents: VdomElement = {
      <.div(
        routeChangeInfo.changeSetInfo.whenDefined(csi => _UiChangeSetInfo(csi)),
        version,
        UiRouteChangeDetail(key, routeChangeInfo)
      )
    }

    private def version: VdomElement = {

      val before = routeChangeInfo.before.map(_.version)
      val after = routeChangeInfo.after.map(_.version)

      val changed = before.isDefined && after.isDefined && before.get != after.get
      val unchanged = if (changed) "" else " (niet veranderd)"

      UiDetail(
        nls("Version", "Versie") + " " + routeChangeInfo.version + unchanged
      )
    }
  }

}
