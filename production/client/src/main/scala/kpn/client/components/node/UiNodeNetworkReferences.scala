package kpn.client.components.node

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.UiImage
import kpn.client.components.common.UiNetworkTypeAndText
import kpn.shared.data.Tags
import kpn.shared.node.NodeNetworkReference
import scalacss.ScalaCssReact._

object UiNodeNetworkReferences {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val networkRoute: StyleA = style(
      marginLeft(35.px)
    )

    val routeLine: StyleA = style(
      height(36.px),
      display.flex,
      alignItems.center
    )
  }

  private case class Props(context: Context, nodeTags: Tags, references: Seq[NodeNetworkReference])

  private val component = ScalaComponent.builder[Props]("node-network-references")
    .render_P { props =>
      implicit val context: Context = props.context
      if (props.references.isEmpty) {
        <.p(nls("None", "Geen"))
      }
      else {
        <.div(
          props.references.toTagMod { reference =>

            val nodeHasExpectedRouteRelations = props.nodeTags.has(reference.networkType.expectedRouteRelationsTag)
            println("nodeHasExpectedRouteRelations=" + nodeHasExpectedRouteRelations)
            println("reference=" + reference)


            <.div(
              UiNetworkTypeAndText(
                reference.networkType,
                <.span(
                  context.gotoNetworkDetails(reference.networkId, reference.networkName)
                )
              ),
              <.div(
                Styles.networkRoute,


                TagMod.when(reference.nodeDefinedInRelation) {
                  <.div(
                    nls(
                      "This node is included as member in the networkrelation",
                      "Dit knooppunt is opgenomen in de netwerkrelatie"
                    )
                  )
                },
                TagMod.when(reference.nodeConnection) {
                  <.div(
                    nls(
                      """This node has the role "connection" in the networkrelation""",
                      """Dit knooppunt heeft de rol "connection" in de netwerkrelatie"""
                    )
                  )
                },
                reference.nodeIntegrityCheck.whenDefined { integrityCheck =>
                  TagMod.when(integrityCheck.failed) {
                    // failed: Boolean, expected: Int, actual: Int
                    <.div(
                      nls(
                        s"Integritycheck failed: expected ${integrityCheck.expected} routes, but found ${integrityCheck.actual}",
                        s"Integriteits controle NOK: verwacht aantal routes is ${integrityCheck.expected}, gevonden: ${integrityCheck.actual}"
                      )
                    )
                  }
                  TagMod.unless(integrityCheck.failed) {
                    // failed: Boolean, expected: Int, actual: Int
                    <.div(
                      nls(
                        s"Integritycheck OK: expected number of routes (${integrityCheck.expected}) matches the number of routes found",
                        s"Integriteits OK: verwacht aantal routes gevonden: ${integrityCheck.expected}"
                      )
                    )
                  }
                },
                reference.routes.toTagMod { route =>
                  <.div(
                    Styles.routeLine,
                    UiNetworkTypeAndText(reference.networkType, context.gotoRoute(route.routeId, route.routeName)),
                    TagMod.when(route.isConnection) {
                      <.span(
                        " ",
                        UiImage("link.png")
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

  def apply(nodeTags: Tags, references: Seq[NodeNetworkReference])(implicit context: Context): VdomElement = {
    component(Props(context, nodeTags, references))
  }
}
