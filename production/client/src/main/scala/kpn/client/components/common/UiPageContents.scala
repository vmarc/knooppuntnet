// Migrated to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiPageContents {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val contents: StyleA = style(
      marginTop(40.px)
    )
  }

  private case class Props(children: Seq[TagMod])

  private val component = ScalaComponent.builder[Props]("page-contents")
    .render_P { props =>
      <.div(
        Styles.contents,
        props.children.toTagMod
      )
    }
    .build

  def apply(children: TagMod*): VdomElement = {
    component(Props(children))
  }
}
