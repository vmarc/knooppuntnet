// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiLine {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val line: StyleA = style(
      display.inlineBlock,
      verticalAlign.middle,
      unsafeChild(":not(:last-child)")(
        paddingRight(0.5.em)
      )
    )
  }

  private case class Props(children: Seq[TagMod])

  private val component = ScalaComponent.builder[Props]("line")
    .render_P { props =>
      <.div(
        Styles.line,
        props.children.toTagMod
      )
    }
    .build

  def apply(children: TagMod*): VdomElement = {
    component(Props(children))
  }
}
