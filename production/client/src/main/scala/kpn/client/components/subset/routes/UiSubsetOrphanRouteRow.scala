// TODO migrate to Angular
package kpn.client.components.subset.routes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.RouteTagFilter
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiTagsTable
import kpn.client.components.common.UiTagsText
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.shared.RouteSummary
import kpn.shared.data.Tags

object UiSubsetOrphanRouteRow {

  private case class Props(key: String, context: Context, route: RouteSummary)

  private val component = ScalaComponent.builder[Props]("orphan-route-row")
    .render_P { props =>
      implicit val context: Context = props.context
      new Renderer(props.route).render()
    }
    .shouldComponentUpdate { scope =>
      CallbackTo {
        scope.currentProps.key != scope.nextProps.key
      }
    }
    .build

  def apply(route: RouteSummary)(implicit context: Context): VdomElement = {
    component(Props(route.id.toString, context, route))
  }

  private class Renderer(route: RouteSummary)(implicit context: Context) {

    def render(): VdomElement = {

      val extraTags = Tags(RouteTagFilter(route).extraTags)

      <.div(
        <.div(
          UiThick(context.gotoRoute(route.id, route.name))
        ),
        <.div(s"${route.meters} m"),
        TagMod.when(route.isBroken) {
          <.div(s"route is broken")
        },
        <.div(
          route.nodeNames.mkString(", ")
        ),
        <.div(
          UiThin(
            <.span(
              route.timestamp.yyyymmdd
            )
          )
        ),
        TagMod.when(extraTags.nonEmpty)(
          <.div(
            nls("Extra tags: ", "Extra labels: "),
            if (PageWidth.isSmall) {
              UiTagsText(extraTags)
            }
            else {
              UiTagsTable(RouteTagFilter(extraTags))
            }
          )
        ),
        UiOsmLink.relation(route.id),
        " ",
        UiOsmLink.josmRelation(route.id)
      )
    }
  }

}
