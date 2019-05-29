// TODO migrate to Angular
package kpn.client.components.shared

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.components.common.CssSettings.default._
import kpn.shared.data.Tags

import scalacss.ScalaCssReact._

object UiChangeSetTags {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val tags: StyleA = style(
      paddingTop(5.px),
      paddingLeft(15.px),
      paddingBottom(25.px)
    )

    val tag: StyleA = style(
      paddingLeft(5.px),
      borderLeftWidth(1.px),
      borderLeftStyle.solid,
      borderLeftColor(c"#ccc"),
      color(gray)
    )
  }

  private case class Props(context: Context, tags: Tags)

  private val component = ScalaComponent.builder[Props]("change-set-tags")
    .render_P { props =>
      implicit val context: Context = props.context
      <.div(
        TagMod.when(props.tags.nonEmpty) {
          <.div(
            Styles.tags,
            props.tags.tags.toTagMod { tag =>
              <.div(
                Styles.tag,
                tag.key,
                "=",
                tag.value
              )
            }
          )
        }
      )
    }
    .build

  def apply(tags: Tags)(implicit context: Context): VdomElement = {
    component(Props(context, tags))
  }
}
