// Migrated to Angular: subset-orphan-node.component.ts
package kpn.client.components.subset.nodes

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.common.NodeTagFilter
import kpn.client.components.common.PageWidth
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiTagsTable
import kpn.client.components.common.UiTagsText
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.data.Tags

object UiSubsetOrphanNodeRow {

  private case class Props(key: String, context: Context, networkType: NetworkType, node: NodeInfo)

  private val component = ScalaComponent.builder[Props]("orphan-node-row")
    .render_P { props =>
      implicit val context: Context = props.context
      new Renderer(props.networkType, props.node).render
    }
    .shouldComponentUpdate { scope =>
      CallbackTo {
        scope.currentProps.key != scope.nextProps.key
      }
    }
    .build

  def apply(networkType: NetworkType, node: NodeInfo)(implicit context: Context): VdomElement = {
    component(Props(node.id.toString, context, networkType, node))
  }

  private class Renderer(networkType: NetworkType, node: NodeInfo)(implicit context: Context) {
    def render: VdomElement = {

      val extraTags = Tags(NodeTagFilter(node).extraTags)

      <.div(
        <.div(
          UiThick(context.gotoNode(node.id, node.name(networkType.name)))
        ),
        UiThin(
          <.div(
            node.lastUpdated.yyyymmdd
          )
        ),
        TagMod.when(extraTags.nonEmpty)(
          <.div(
            nls("Extra tags: ", "Extra labels: "),
            if (PageWidth.isSmall) {
              UiTagsText(extraTags)
            }
            else {
              UiTagsTable(NodeTagFilter(extraTags))
            }
          )
        ),
        <.div(
          UiOsmLink.node(node.id),
          " ",
          UiOsmLink.josmNode(node.id)
        )
      )
    }
  }

}
