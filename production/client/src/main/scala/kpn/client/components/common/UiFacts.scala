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
import kpn.shared.route.RouteInfo

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
  }

  private case class Props(context: Context, facts: Seq[Fact], routeInfo: Option[RouteInfo])

  private val component = ScalaComponent.builder[Props]("example")
    .render_P { props =>

      implicit val context: Context = props.context

      val facts = props.facts.filterNot(_.id == Fact.RouteBroken.id)

      if (facts.isEmpty) {
        <.p(nls("None", "Geen"))
      } else {
        <.div(
          facts.toTagMod { fact =>
            <.div(
              Styles.fact,
              UiLine(
                <.div(
                  Styles.level,
                  UiFactLevel(fact.level)
                ),
                nls(fact.name, fact.nlName)
              ),
              <.div(
                Styles.description,
                UiFactDescription(fact, props.routeInfo)
              )
            )
          }
        )
      }
    }
    .build

  def apply(facts: Seq[Fact], routeInfo: Option[RouteInfo] = None)(implicit context: Context): VdomElement = {
    component(Props(context, facts, routeInfo))
  }
}
