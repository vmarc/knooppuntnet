// Migrated to Angular: tags-text.component.ts
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.CssSettings.default._
import kpn.shared.data.Tags

import scalacss.ScalaCssReact._

object UiTagsText {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val text: StyleA = style(
      paddingTop(10.px),
      paddingBottom(10.px)
    )
  }

  private case class Props(context: Context, tags: Tags)

  private val component = ScalaComponent.builder[Props]("tags-text")
    .render_P { props =>
      new Renderer(props.tags)(props.context).render()
    }
    .build

  def apply(tags: Tags)(implicit context: Context): VdomElement = component(Props(context, tags))

  private class Renderer(tags: Tags)(implicit val context: Context) {

    def render(): VdomElement = {
      if (tags.isEmpty) {
        <.div(
          Styles.text,
          nls(
            "No tags",
            "Geen labels"
          )
        )
      }
      else {
        <.div(
          tags.tags.toTagMod { tag =>
            <.div(
              tag.key,
              " = ",
              tag.value
            )
          }
        )
      }
    }
  }

}
