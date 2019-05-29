// TODO migrate to Angular
package kpn.client.components.route

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiImage
import kpn.client.components.common.UiOsmLink
import kpn.shared.NetworkType
import kpn.shared.route.Backward
import kpn.shared.route.Forward
import kpn.shared.route.RoutePage

import scalacss.ScalaCssReact._

object UiRouteMembers {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val imageCell: StyleA = style(
      padding(0.px),
      height(40.px),
      minHeight(40.px),
      maxHeight(40.px)
    )

    val image: StyleA = style(
      padding(0.px),
      height(40.px),
      margin(0.px)
    )
  }

  private case class Props(context: Context, page: RoutePage)

  private val component = ScalaComponent.builder[Props]("route-members")
    .render_P { props =>

      implicit val context: Context = props.context

      props.page.route.analysis match {
        case None => <.div()
        case Some(analysis) =>
          <.div(
            <.h4(nls("Route Members", "Route onderdelen")),
            if (analysis.members.isEmpty) {
              <.span(nls("None", "Geen"))
            } else {
              <.table(
                <.thead(
                  <.tr(
                    <.th(),
                    <.th(nls("Node", "Knoop")),
                    <.th(nls("Id", "Id")),
                    <.th(^.colSpan := 2, nls("Nodes", "Knooppunten")),
                    <.th(nls("Role", "Rol")),
                    <.th(nls("Length", "Lengte")),
                    <.th(nls("#Nodes", "#Knopen")),
                    <.th(nls("Name", "Naam")),
                    <.th(nls("Unaccessible", "Ontoegankelijk")),
                    TagMod.when(props.page.route.summary.networkType == NetworkType.bicycle) {
                      <.th(
                        ^.colSpan := 2,
                        nls("One Way", "Enkele richting")
                      )
                    }
                  )
                ),
                <.tbody(
                  analysis.members.toTagMod { member =>
                    <.tr(
                      <.td(
                        Styles.imageCell,
                        <.div(
                          Styles.image,
                          UiImage("links/" + member.linkName + ".png")
                        )
                      ),
                      <.td(
                        UiCommaList(
                          member.nodes.map { node =>
                            context.gotoNode(node.id, node.alternateName)
                          }
                        )
                      ),
                      <.td(
                        UiOsmLink.osmLink(member.memberType, member.id)
                      ),
                      <.td(
                        UiOsmLink.link("node", member.fromNodeId, member.from)
                      ),
                      <.td(
                        TagMod.when(member.isWay) {
                          UiOsmLink.link("way", member.toNodeId, member.to)
                        }
                      ),
                      <.td(
                        member.role
                      ),
                      <.td(
                        member.length
                      ),
                      <.td(
                        member.nodeCount
                      ),
                      <.td(
                        member.description
                      ),
                      <.td(
                        TagMod.when(!member.isAccessible) {
                          UiImage("warning.png")
                        }
                      ),
                      TagMod.when(props.page.route.summary.networkType == NetworkType.bicycle) {
                        <.td(
                          if (member.oneWay == Forward) {
                            nls("Yes", "Ja")
                          }
                          else if (member.oneWay == Backward) {
                            nls("Reverse", "Omgekeerd")
                          }
                          else {
                            ""
                          }
                        )
                      },
                      TagMod.when(props.page.route.summary.networkType == NetworkType.bicycle) {
                        <.td(
                          member.oneWayTags.tagString
                        )
                      }
                    )
                  }
                )
              )
            }
          )
      }
    }
    .build

  def apply(page: RoutePage)(implicit context: Context): VdomElement = component(Props(context, page))
}
