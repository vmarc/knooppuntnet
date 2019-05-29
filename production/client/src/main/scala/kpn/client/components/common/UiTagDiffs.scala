// TODO migrate to Angular
package kpn.client.components.common

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.VdomElement
import kpn.client.common.Context
import kpn.shared.diff.TagDiffs

object UiTagDiffs {

  private case class Props(context: Context, tagDiffs: TagDiffs)

  private val component = ScalaComponent.builder[Props]("tag-diffs")
    .render_P { props =>
      if (PageWidth.isSmall) {
        UiTagDiffsText(props.tagDiffs)(props.context)
      }
      else {
        UiTagDiffsTable(props.tagDiffs)(props.context)
      }
    }
    .build

  def apply(tagDiffs: TagDiffs)(implicit context: Context): VdomElement = {
    component(Props(context, tagDiffs))
  }
}
