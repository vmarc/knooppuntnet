// TODO migrate to Angular
package kpn.client.components.route

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context

object UiRouteStructure {

  private case class Props(context: Context, structureStrings: Seq[String])

  private val component = ScalaComponent.builder[Props]("route-structure")
    .render_P { props =>
      <.table(
        <.tbody(
          props.structureStrings.toTagMod { structureString =>
            val formatted = structureString.replaceAll("forward", "<b>forward</b>").
              replaceAll("backward", "<b>backward</b>").
              replaceAll("unused", "<b>unused</b>").
              replaceAll("tentacle", "<b>tentacle</b>").
              replaceAll("broken", """<span style="color:red">broken</span>""").
              replaceAll("\\+", " + ").
              replaceAll("-", " - ")
            <.tr(
              <.td(^.dangerouslySetInnerHtml := formatted)
            )
          }
        )
      )
    }
    .build

  def apply(structureStrings: Seq[String])(implicit context: Context): VdomElement = {
    component(Props(context, structureStrings))
  }
}
