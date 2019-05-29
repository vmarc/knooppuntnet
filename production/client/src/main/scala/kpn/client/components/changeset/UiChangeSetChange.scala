// TODO migrate to Angular
package kpn.client.components.changeset

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.components.common.UiLevel2
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiThin

object UiChangeSetChange {

  private case class Props(title: String, subTitle: Option[String], icon: Option[VdomElement], body: TagMod)

  private val component = ScalaComponent.builder[Props]("change-set-change")
    .render_P { props =>

      val title: VdomElement = <.span(props.title)

      UiLevel2(
        UiLine(
          title,
          props.subTitle.whenDefined(UiThin(_)),
          props.icon.whenDefined
        ),
        props.body
      )
    }
    .build

  def apply(title: String, subTitle: Option[String] = None, icon: Option[VdomElement] = None)(body: TagMod): VdomElement = {
    component(Props(title, subTitle, icon, body))
  }
}
