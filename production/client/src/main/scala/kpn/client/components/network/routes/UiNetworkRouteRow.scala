package kpn.client.components.network.routes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiOsmLink
import kpn.client.components.network.routes.UiNetworkRoutes.Styles
import kpn.client.components.network.routes.indicators.AccessibleIndicator
import kpn.client.components.network.routes.indicators.ConnectionIndicator
import kpn.client.components.network.routes.indicators.InvestigateIndicator
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.network.NetworkRouteInfo

import scalacss.ScalaCssReact._

object UiNetworkRouteRow {

  private case class Props(
    key: String,
    context: Context,
    pageWidth: PageWidth.Value,
    number: Int,
    networkType: NetworkType,
    route: NetworkRouteInfo
  )

  private val component = ScalaComponent.builder[Props]("route-row")
    .render_P { props =>
      implicit val context: Context = props.context
      new Renderer(props.number, props.networkType, props.route).render()
    }
    .build

  def apply(pageWidth: PageWidth.Value, number: Int, networkType: NetworkType, route: NetworkRouteInfo)(implicit context: Context): VdomElement = {
    component(Props(route.id.toString, context, pageWidth, number, networkType, route))
  }

  private class Renderer(number: Int, networkType: NetworkType, route: NetworkRouteInfo)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        Styles.itemRow,
        routeNumber,
        analysis,
        routeName,
        edit,
        osm,
        role,
        length,
        lastEdit
      )
    }

    private def routeNumber: VdomElement = {
      <.div(
        Styles.routeNumberValue,
        number
      )
    }

    private def analysis: VdomElement = {
      <.div(
        Styles.analysisValue,
        InvestigateIndicator(route.facts.contains(Fact.RouteBroken)),
        AccessibleIndicator(networkType, !route.facts.contains(Fact.RouteUnaccessible)),
        ConnectionIndicator(route.role.contains("connection"))
      )
    }

    private def routeName: VdomElement = {
      <.div(
        Styles.routeNameValue,
        context.gotoRoute(route.id, route.name)
      )
    }

    private def edit: TagMod = {
      TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
        <.div(
          Styles.editValue,
          UiOsmLink.josmRelation(route.id)
        )
      }
    }

    private def osm: TagMod = {
      TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
        <.div(
          Styles.osmValue,
          UiOsmLink.relation(route.id)
        )
      }
    }

    private def role: TagMod = {
      TagMod.when(PageWidth.isVeryLarge) {
        <.div(
          Styles.roleValue,
          route.role.whenDefined
        )
      }
    }

    private def length: TagMod = {
      TagMod.when(PageWidth.isVeryLarge) {
        <.div(
          Styles.lengthValue,
          route.length + "m"
        )
      }
    }

    private def lastEdit: TagMod = {
      TagMod.when(PageWidth.isLarge || PageWidth.isVeryLarge) {
        <.div(
          Styles.lastEditValue,
          route.relationLastUpdated.yyyymmdd
        )
      }
    }
  }

}
