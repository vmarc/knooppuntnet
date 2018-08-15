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
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiDetail
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiOsmLink
import kpn.client.components.common.UiThin
import kpn.shared.ChangeSetSummary
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.common.KnownElements
import kpn.shared.node.NodeChangeInfo
import kpn.shared.route.RouteChangeInfo

object UiNetworkDiff {

  private case class Props(
    context: Context,
    changeSetSummary: ChangeSetSummary,
    networkChangeInfo: NetworkChangeInfo,
    routeChangeInfos: Seq[RouteChangeInfo],
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )

  private val component = ScalaComponent.builder[Props]("network-diff")
    .render_P { props =>
      new Renderer(
        props.changeSetSummary,
        props.networkChangeInfo,
        props.routeChangeInfos,
        props.nodeChangeInfos,
        props.knownElements
      )(props.context).render()
    }
    .build

  def apply(
    changeSetSummary: ChangeSetSummary,
    networkChangeInfo: NetworkChangeInfo,
    routeChangeInfos: Seq[RouteChangeInfo],
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )(implicit context: Context): VdomElement = {
    component(
      Props(
        context,
        changeSetSummary,
        networkChangeInfo,
        routeChangeInfos,
        nodeChangeInfos,
        knownElements
      )
    )
  }

  private class Renderer(
    changeSetSummary: ChangeSetSummary,
    networkChangeInfo: NetworkChangeInfo,
    routeChangeInfos: Seq[RouteChangeInfo],
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )(implicit context: Context) {

    def render(): VdomElement = {

      <.div(
        networkMetaData(),

        removedNodes(),
        addedNodes(),
        updatedNodes(),

        removedWays(),
        addedWays(),
        updatedWays(),

        removedRelations(),
        addedRelations(),
        updatedRelations(),

        UiNodeDiffs(
          changeSetSummary.key.changeSetId,
          networkChangeInfo.networkId.toString,
          networkChangeInfo.networkNodes,
          nodeChangeInfos,
          knownElements
        ),

        UiRouteDiffs(
          changeSetSummary.key.changeSetId,
          networkChangeInfo.networkId.toString,
          networkChangeInfo.routes,
          routeChangeInfos,
          knownElements
        )
      )
    }

    private def networkMetaData(): TagMod = {

      networkChangeInfo.after.whenDefined { after =>

        val newVersion = networkChangeInfo.before match {
          case Some(before) => before.version != after.version
          case _ => false
        }

        UiDetail(
          UiThin(
            <.div(
              if (newVersion) {
                <.span(
                  nls(
                    s"Relation changed to v${after.version}.",
                    s"Relatie gewijzigd naar v${after.version}."
                  )
                )
              }
              else {
                <.span(
                  nls(
                    s"Relation unchanged.",
                    s"Relatie ongewijzigd."
                  )
                )
              },
              " ",
              UiMetaInfo(after)
            )
          )
        )
      }
    }

    private def removed: String = nls("Removed", "Verwijderde")

    private def added: String = nls("Added", "Toegevoegde")

    private def updated: String = nls("Updated", "Aangepaste")

    private def removedNodes(): TagMod = {
      nodeList(removed, networkChangeInfo.nodes.removed, Some(UiHappy()))
    }

    private def addedNodes(): TagMod = {
      nodeList(added, networkChangeInfo.nodes.added, Some(UiInvestigate()))
    }

    private def updatedNodes(): TagMod = {
      nodeList(updated, networkChangeInfo.nodes.updated)
    }

    private def nodeList(action: String, nodeIds: Seq[Long], icon: Option[VdomElement] = None): TagMod = {
      TagMod.when(nodeIds.nonEmpty) {
        val title = s"$action ${nls("non-network nodes", "nodes die geen knooppunten zijn")}"
        UiChangeSetChange(title, Some(s"(${nodeIds.size})"), icon) {
          UiCommaList(
            nodeIds.map(UiOsmLink.osmNode)
          )
        }
      }
    }

    private def removedWays(): TagMod = {
      wayList(removed, networkChangeInfo.ways.removed, Some(UiHappy()))
    }

    private def addedWays(): TagMod = {
      wayList(added, networkChangeInfo.ways.added, Some(UiInvestigate()))
    }

    private def updatedWays(): TagMod = {
      wayList(updated, networkChangeInfo.ways.updated)
    }

    private def wayList(action: String, wayIds: Seq[Long], icon: Option[VdomElement] = None): TagMod = {
      TagMod.when(wayIds.nonEmpty) {
        val title = s"$action ${nls("ways", "wegen")}"
        UiChangeSetChange(title, Some(s"(${wayIds.size})"), icon) {
          UiCommaList(
            wayIds.map(UiOsmLink.osmWay)
          )
        }
      }
    }

    private def removedRelations(): TagMod = {
      relationList(removed, networkChangeInfo.relations.removed, Some(UiHappy()))
    }

    private def addedRelations(): TagMod = {
      relationList(added, networkChangeInfo.relations.added, Some(UiInvestigate()))
    }

    private def updatedRelations(): TagMod = {
      relationList(updated, networkChangeInfo.relations.updated)
    }

    private def relationList(action: String, relationIds: Seq[Long], icon: Option[VdomElement] = None): TagMod = {
      TagMod.when(relationIds.nonEmpty) {
        val title = s"$action ${nls("non-route relations", "relaties die geen route relatie zijn")}"
        UiChangeSetChange(title, Some(s"(${relationIds.size})"), icon) {
          UiCommaList(
            relationIds.map(UiOsmLink.osmRelation)
          )
        }
      }
    }
  }

}
