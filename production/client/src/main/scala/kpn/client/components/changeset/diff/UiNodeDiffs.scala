package kpn.client.components.changeset.diff

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.changeset.UiChangeSetChange
import kpn.client.components.changeset.UiMetaInfo
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLevel3
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiThick
import kpn.client.components.common.UiThin
import kpn.client.components.node.UiNodeChangeDetail
import kpn.shared.common.KnownElements
import kpn.shared.common.Ref
import kpn.shared.data.MetaData
import kpn.shared.diff.RefDiffs
import kpn.shared.node.NodeChangeInfo

object UiNodeDiffs {

  private case class Props(
    context: Context,
    changeSetId: Long,
    contextId: String,
    nodeDiffs: RefDiffs,
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )

  private val component = ScalaComponent.builder[Props]("network-update")
    .render_P { props =>
      new Renderer(
        props.changeSetId,
        props.contextId,
        props.nodeDiffs,
        props.nodeChangeInfos,
        props.knownElements
      )(props.context).render()
    }
    .build

  def apply(
    changeSetId: Long,
    contextId: String,
    nodeDiffs: RefDiffs,
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )(implicit context: Context): VdomElement = {
    component(
      Props(
        context,
        changeSetId,
        contextId,
        nodeDiffs,
        nodeChangeInfos,
        knownElements
      )
    )
  }

  private class Renderer(
    changeSetId: Long,
    contextId: String,
    nodeDiffs: RefDiffs,
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )(implicit context: Context) {

    def render(): VdomElement = {
      <.div(
        removedNetworkNodes(),
        addedNetworkNodes(),
        updatedNetworkNodes()
      )
    }

    private def removedNetworkNodes(): TagMod = {
      TagMod.when(nodeDiffs.removed.nonEmpty) {
        UiChangeSetChange(
          nls(
            "Removed network nodes",
            "Verwijderde knooppunten"
          ),
          Some(s"(${nodeDiffs.removed.size})"),
          Some(UiInvestigate())
        ) {
          nodeDiffs.removed.toTagMod { ref =>
            UiLevel3(
              nodeTitle(ref.id, ref.name),
              nodeRemovedDetail(ref)
            )
          }
        }
      }
    }

    private def nodeRemovedDetail(ref: Ref): TagMod = {
      nodeChangeInfos.find(_.id == ref.id).whenDefined { nodeChange =>
        nodeChange.before.whenDefined { metaData =>
          UiThin(UiMetaInfo(metaData))
        }
      }
    }

    private def addedNetworkNodes(): TagMod = {
      TagMod.when(nodeDiffs.added.nonEmpty) {
        UiChangeSetChange(
          nls(
            "Added network nodes",
            "Toegevoegde knooppunten"
          ),
          Some(s"(${nodeDiffs.added.size})"),
          Some(UiHappy())
        ) {
          nodeDiffs.added.toTagMod { ref =>
            UiLevel3(
              nodeTitle(ref.id, ref.name),
              nodeAddedDetail(ref)
            )
          }
        }
      }
    }

    private def nodeAddedDetail(ref: Ref): TagMod = {
      nodeChangeInfos.find(_.id == ref.id).whenDefined { nodeChange =>
        nodeChange.after.whenDefined { metaData =>
          nodeAddedVersion(metaData)
        }
      }
    }

    private def nodeAddedVersion(metaData: MetaData): TagMod = {
      UiThin(
        <.div(
          if (metaData.version == 1) {
            nls(
              "Created in this changeset.",
              "Nieuw in deze changeset."
            )
          } else if (metaData.changeSetId == changeSetId) {
            nls(
              s"Updated in this changeset. v${metaData.version}.",
              s"Aangepast in deze changeset. v${metaData.version}."
            )
          } else {
            <.div(
              nls(
                "Existing node",
                "Bestaand knooppunt"
              ),
              " ",
              UiMetaInfo(metaData)
            )
          }
        )
      )
    }

    private def updatedNetworkNodes(): TagMod = {
      TagMod.when(nodeDiffs.updated.nonEmpty) {
        UiChangeSetChange(
          nls(
            "Updated network nodes",
            "Gewijzigde knooppunten"
          ),
          Some(s"(${nodeDiffs.updated.size})")
        ) {
          nodeDiffs.updated.toTagMod { ref =>
            UiLevel3(
              nodeTitle(ref.id, ref.name),
              nodeUpdatedDetail(ref)
            )
          }
        }
      }
    }

    private def nodeUpdatedDetail(ref: Ref): TagMod = {
      nodeChangeInfos.find(_.id == ref.id).whenDefined { nodeChange =>
        val key = "node-" + contextId + "-" + ref.id
        <.div(
          nodeUpdatedVersion(nodeChange),
          UiNodeChangeDetail(key, nodeChange)
        )
      }
    }

    private def nodeUpdatedVersion(nodeChange: NodeChangeInfo): TagMod = {
      nodeChange.before.whenDefined { before =>
        nodeChange.after.whenDefined { after =>
          UiThin(
            <.span(
              if (before.version == after.version) {
                nls(
                  s"Existing node v${after.version}.",
                  s"Bestaand knooppunt v${after.version}."
                )
              }
              else {
                nls(
                  s"Node change to v${after.version}.",
                  s"Knooppunt veranderd naar v${after.version}."
                )
              },
              " ",
              UiMetaInfo(after)
            )
          )
        }
      }
    }

    private def nodeTitle(id: Long, name: String): VdomElement = {
      if (knownElements.nodeIds.contains(id)) {
        UiLine(
          UiThick(context.gotoNode(id, name)),
          UiThin(UiOsmLink.osmNode(id))
        )
      }
      else {
        UiLine(
          UiThick(name),
          UiThin(UiOsmLink.osmNode(id))
        )
      }
    }
  }

}
