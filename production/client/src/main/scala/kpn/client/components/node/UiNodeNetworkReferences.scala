package kpn.client.components.node

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.common.UiMarked
import kpn.client.components.common.UiNetworkTypeAndText
import kpn.shared.data.Tags
import kpn.shared.node.NodeNetworkReference
import scalacss.ScalaCssReact._

object UiNodeNetworkReferences {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val networkRoute: StyleA = style(
      marginLeft(35.px),
      paddingBottom(35.px)
    )

    val text: StyleA = style(
      paddingBottom(10.px)
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

            <.div(
              UiNetworkTypeAndText(
                reference.networkType,
                <.span(
                  context.gotoNetworkDetails(reference.networkId, reference.networkName)
                )
              ),
              <.div(
                Styles.networkRoute,
                <.div(
                  Styles.text,
                  if (reference.nodeConnection) {
                    if (nodeHasExpectedRouteRelations) {
                      UiMarked(
                        nls(
                          s"""This node has the role "connection" in the networkrelation:
                             |the _${reference.networkType.expectedRouteRelationsTag}_ tag does not
                             |apply to this netwerk.""".stripMargin,
                          s"""Dit knooppunt heeft de rol "connection" in de netwerkrelatie:
                             |de _${reference.networkType.expectedRouteRelationsTag}_ tag is niet van
                             |toepassing voor dit netwerk.""".stripMargin
                        ),
                        paragraphs = false
                      )
                    }
                    else {
                      <.div(
                        nls(
                          """This node has the role "connection" in the networkrelation.""",
                          """Dit knooppunt heeft de rol "connection" in de netwerkrelatie."""
                        )
                      )
                    }
                  }
                  else if (reference.nodeDefinedInRelation) {
                    <.div(
                      nls(
                        """This node is included as member in the networkrelation.""",
                        """Dit knooppunt is opgenomen in de netwerkrelatie."""
                      )
                    )
                  }
                  else if (nodeHasExpectedRouteRelations) {
                    UiMarked(
                      nls(
                        s"""This node is not included as member in the networkrelation:
                           |the _${reference.networkType.expectedRouteRelationsTag}_ tag does not apply
                           |to this netwerk.""".stripMargin,
                        s"""Dit knooppunt is niet opgenomen in de netwerkrelatie:
                           |de _${reference.networkType.expectedRouteRelationsTag}_ tag is niet van
                           |toepassing voor dit netwerk.""".stripMargin
                      ),
                      paragraphs = false
                    )
                  }
                  else {
                    <.div(
                      nls(
                        "This node is not included as member in the networkrelation.",
                        "Dit knooppunt is niet opgenomen in de netwerkrelatie."
                      )
                    )
                  },
                  reference.nodeIntegrityCheck.whenDefined { integrityCheck =>
                    if (integrityCheck.failed) {
                      <.div(
                        nls(
                          s"Integritycheck failed: expected ${integrityCheck.expected} routes, but found ${integrityCheck.actual}.",
                          s"Onverwacht aantal routes (${integrityCheck.actual}), verwacht: ${integrityCheck.expected}."
                        )
                      )
                    }
                    else {
                      <.div(
                        nls(
                          s"Expected number of routes (${integrityCheck.expected}) matches the number of routes found.",
                          s"Verwacht aantal routes (${integrityCheck.expected}) klopt."
                        )
                      )
                    }
                  }
                ),
                reference.routes.toTagMod { route =>
                  <.div(
                    Styles.routeLine,
                    UiNetworkTypeAndText(reference.networkType, context.gotoRoute(route.routeId, route.routeName), route.isConnection)
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
