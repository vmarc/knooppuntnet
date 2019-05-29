// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.CssSettings.default._

import scalacss.ScalaCssReact._

object UiCommaList {

  object Styles extends StyleSheet.Inline {

    import dsl._

    val icon: StyleA = style(
      display.inline,
      paddingLeft(0.5.em)
    )
  }

  private case class Props(children: Seq[TagMod], title: Option[String], extra: Option[VdomElement])

  private val component = ScalaComponent.builder[Props]("comma-list")
    .render_P { props =>
      val list = props.children
      val comma = <.span(", ")
      val elements = if (props.children.size > 1) {
        list.head +: list.tail.flatMap(x => Seq(comma, x))
      }
      else {
        list
      }

      val titleElements: Seq[VdomElement] = props.title match {
        case Some(title) =>
          Seq(
            <.span(title + ": ")
          )
        case None => Seq()
      }

      val extraElements: Seq[VdomElement] = props.extra match {
        case Some(extra) =>
          Seq(
            <.span(" "),
            extra
          )
        case None => Seq()
      }

      val yy = props.extra.whenDefined(extra => <.div(Styles.icon, extra))

      val all: TagMod = (titleElements ++ elements).toTagMod

      <.div(all, yy)
    }
    .build

  def apply(children: Seq[TagMod], title: Option[String] = None, extra: Option[VdomElement] = None): TagMod = {
    TagMod.when(children.nonEmpty) {
      component(Props(children, title, extra))
    }
  }
}
