// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.TagFilter
import kpn.client.components.common.CssSettings.default._
import kpn.shared.data.Tag

import scalacss.ScalaCssReact._

object UiTagsTable {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val text: StyleA = style(
      paddingTop(10.px),
      paddingBottom(10.px)
    )
  }

  private case class Props(context: Context, tagFilter: TagFilter)

  private val component = ScalaComponent.builder[Props]("tags")
    .render_P { props =>
      new Renderer(props.tagFilter)(props.context).render()
    }
    .build

  def apply(tagFilter: TagFilter)(implicit context: Context): VdomElement = {
    component(Props(context, tagFilter))
  }

  private class Renderer(tagFilter: TagFilter)(implicit val context: Context) {

    def render(): VdomElement = {
      if (tagFilter.isEmpty) {
        <.div(
          Styles.text,
          nls(
            "No tags",
            "Geen labels"
          )
        )
      }
      else {
        <.table(
          ^.title := "tags",
          <.thead(
            <.tr(
              <.th(nls("Key", "Sleutel")),
              <.th(nls("Value", "Waarde"))
            )
          ),
          <.tbody(
            TagMod(
              standardTags(),
              separator(),
              extraTags()
            )
          )
        )
      }
    }

    private def standardTags(): TagMod = {
      tagRows(tagFilter.standardTags)
    }

    private def extraTags(): TagMod = {
      tagRows(tagFilter.extraTags)
    }

    private def tagRows(tags: Seq[Tag]): TagMod = {
      tags.toTagMod { tag =>
        <.tr(
          <.td(tag.key),
          <.td(tag.value)
        )
      }
    }

    private def separator(): TagMod = {
      TagMod.when(tagFilter.standardTags.nonEmpty && tagFilter.extraTags.nonEmpty) {
        <.tr(
          <.td(
            ^.colSpan := 2
          )
        )
      }
    }
  }

}
