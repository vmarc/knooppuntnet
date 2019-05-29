// TODO migrate to Angular
package kpn.client.components.node

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiDetail
import kpn.client.components.shared.UiChangeSet
import kpn.client.components.shared.UiChangeSetTags
import kpn.shared.node.NodeChangeInfo

object UiNodeChange {

  private case class Props(context: Context, key: String, nodeChange: NodeChangeInfo)

  private val component = ScalaComponent.builder[Props]("node-change")
    .render_P { props =>
      new Renderer(props.key, props.nodeChange)(props.context).render()
    }
    .build

  def apply(key: String, nodeChange: NodeChangeInfo)(implicit context: Context): VdomElement = {
    component(Props(context, key, nodeChange))
  }

  private class Renderer(key: String, nodeChange: NodeChangeInfo)(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        UiChangeSet(
          nodeChange.changeKey,
          nodeChange.happy,
          nodeChange.investigate,
          nodeChange.comment,
          contents
        )
      )
    }

    private def contents: VdomElement = {
      <.div(
        changeSetTags,
        <.div(
          version,
          UiNodeChangeDetail(key, nodeChange)
        )
      )
    }

    private def changeSetTags: TagMod = {
      TagMod.when(nodeChange.changeTags.nonEmpty) {
        UiChangeSetTags(nodeChange.changeTags)
      }
    }

    private def version: TagMod = {
      nodeChange.version.whenDefined { version =>
        UiDetail(
          nls("Version", "Versie") + " " + version
        )
      }
    }
  }

}
