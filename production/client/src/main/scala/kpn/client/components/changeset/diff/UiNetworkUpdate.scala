// TODO migrate to Angular
package kpn.client.components.changeset.diff

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.Implicits._
import japgolly.scalajs.react.vdom.TagMod
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^.<
import japgolly.scalajs.react.vdom.html_<^.^
import kpn.client.common.Context
import kpn.client.common.Nls.nls
import kpn.client.components.common.UiCommaList
import kpn.client.components.common.UiHappy
import kpn.client.components.common.UiInvestigate
import kpn.client.components.common.UiLevel1
import kpn.client.components.common.UiLine
import kpn.client.components.common.UiNetworkTypeIcon
import kpn.client.components.common.UiNodeRef
import kpn.client.components.common.UiRouteRef
import kpn.shared.ChangeSetSummary
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.common.KnownElements
import kpn.shared.node.NodeChangeInfo
import kpn.shared.route.RouteChangeInfo

object UiNetworkUpdate {

  private case class Props(
    context: Context,
    changeSetSummary: ChangeSetSummary,
    networkChangeInfo: NetworkChangeInfo,
    routeChangeInfos: Seq[RouteChangeInfo],
    nodeChangeInfos: Seq[NodeChangeInfo],
    knownElements: KnownElements
  )

  private val component = ScalaComponent.builder[Props]("network-update")
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

      UiLevel1(
        <.div(
          <.a(^.id := networkChangeInfo.networkId),
          UiLine(
            UiNetworkTypeIcon(networkChangeInfo.networkType),
            <.span(nls("Network", "Netwerk")).render,
            context.gotoNetworkDetails(networkChangeInfo.networkId, networkChangeInfo.networkName)
          )
        ),
        <.div(
          changeType,
          oldOrphanRoutes(),
          newOrphanRoutes(),
          oldOrphanNodes(),
          newOrphanNodes(),
          UiNetworkDiff(changeSetSummary, networkChangeInfo, routeChangeInfos, nodeChangeInfos, knownElements)
        )
      )
    }

    private def changeType: TagMod = {
      <.div(
        <.b(
          networkChangeInfo.changeType match {
            case ChangeType.Create =>
              nls(
                "Network created",
                "Netwerk nieuw aangemaakt"
              )

            case ChangeType.Delete =>
              nls(
                "Network deleted",
                "Netwerk verwijderd"
              )

            case _ =>
              ""
          }
        )
      )
    }

    private def oldOrphanRoutes(): TagMod = {
      routeRefs(
        nls(
          "Orphan routes added to this network",
          "Route wezen toegevoegd aan dit netwerk"
        ),
        networkChangeInfo.orphanRoutes.oldRefs,
        UiHappy()
      )
    }

    private def newOrphanRoutes(): TagMod = {
      routeRefs(
        nls(
          "Following routes that used to be part of this network have become orphan",
          "Volgende routes maakten deel uit van dit netwerk, maar zijn nu route wees geworden"
        ),
        networkChangeInfo.orphanRoutes.newRefs,
        UiInvestigate()
      )
    }

    private def oldOrphanNodes(): TagMod = {
      nodeRefs(
        nls(
          "Orphan nodes added to this network",
          "Knooppuntwezen toegevoegd aan dit netwerk"
        ),
        networkChangeInfo.orphanNodes.oldRefs,
        UiHappy()
      )
    }

    private def newOrphanNodes(): TagMod = {
      nodeRefs(
        nls(
          "Following nodes that used to be part of this network have become orphan",
          "Volgende knooppunten maakten tot nu toe deel uit van dit netwerk, maar zijn knooppunt wees geworden"
        ),
        networkChangeInfo.orphanNodes.newRefs,
        UiInvestigate()
      )
    }

    private def routeRefs(title: String, refs: Seq[kpn.shared.common.Ref], icon: VdomElement): TagMod = {
      TagMod.when(refs.nonEmpty) {
        <.div(
          UiCommaList(
            refs.map { ref =>
              UiRouteRef(ref, knownElements)
            },
            Some(title),
            Some(icon)
          )
        )
      }
    }

    private def nodeRefs(title: String, refs: Seq[kpn.shared.common.Ref], icon: VdomElement): TagMod = {
      TagMod.when(refs.nonEmpty) {
        <.div(
          UiCommaList(
            refs.map { ref =>
              UiNodeRef(ref, knownElements)
            },
            Some(title),
            Some(icon)
          )
        )
      }
    }
  }

}
