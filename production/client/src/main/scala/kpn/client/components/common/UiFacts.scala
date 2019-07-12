// Migrated to Angular: facts.component.ts
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.client.components.facts.UiFactDescription
import kpn.shared.Fact
import scalacss.ScalaCssReact._

object UiFacts {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val levelWidth = 25

    val fact: StyleA = style(
      marginTop(15.px)
    )

    val level: StyleA = style(
      display.inlineBlock,
      width(levelWidth.px)
    )

    val description: StyleA = style(
      paddingLeft(levelWidth.px),
      paddingBottom(3.px),
      fontStyle.italic
    )

    val reference: StyleA = style(
      display.inlineBlock,
      paddingLeft(20.px)
    )
  }

  private case class Props(context: Context, factInfos: Seq[FactInfo])

  private val component = ScalaComponent.builder[Props]("facts")
    .render_P { props =>

      implicit val context: Context = props.context

      val factInfos = props.factInfos.filterNot(_.fact.id == Fact.RouteBroken.id)

      if (factInfos.isEmpty) {
        <.p(nls("None", "Geen"))
      } else {
        <.div(
          factInfos.toTagMod { factInfo =>
            val fact = factInfo.fact
            <.div(
              Styles.fact,
              UiLine(
                <.div(
                  Styles.level,
                  UiFactLevel(fact.level)
                ),
                nls(fact.name, fact.nlName),
                factInfo.networkRef.whenDefined { networkRef =>
                  <.div(
                    Styles.reference,
                    "(",
                    context.gotoNetworkDetails(networkRef.id, networkRef.name),
                    ")"
                  )
                },
                factInfo.routeRef.whenDefined { routeRef =>
                  <.div(
                    Styles.reference,
                    "(",
                    context.gotoRoute(routeRef.id, routeRef.name),
                    ")"
                  )
                },
                factInfo.nodeRef.whenDefined { nodeRef =>
                  <.div(
                    Styles.reference,
                    "(",
                    context.gotoNode(nodeRef.id, nodeRef.name),
                    ")"
                  )
                }
              ),
              <.div(
                Styles.description,
                UiFactDescription(fact)
              )
            )
          }
        )
      }
    }
    .build

  def apply(factInfos: Seq[FactInfo])(implicit context: Context): VdomElement = {
    component(Props(context, factInfos))
  }
}
