package kpn.server.api.analysis.pages

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.KnownElements
import kpn.api.common.common.ReferencedElements
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.route.RouteChangeInfo
import kpn.server.analyzer.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.server.analyzer.engine.changes.builder.NodeChangeInfoBuilder
import kpn.server.analyzer.engine.changes.builder.RouteChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class ChangeSetPageBuilderImpl(
  changeSetInfoRepository: ChangeSetInfoRepository,
  changeSetRepository: ChangeSetRepository,
  nodeRepository: NodeRepository,
  routeRepository: RouteRepository
) extends ChangeSetPageBuilder {

  def build(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): Option[ChangeSetPage] = {

    if (changeSetId == 1L) {
      Some(ChangeSetPageExample.page)
    }
    else {
      if (user.isDefined) {
        changeSetRepository.changeSet(changeSetId, replicationId) match {
          case Seq(changeSetData) =>

            val changeSetInfo = changeSetInfoRepository.get(changeSetId)

            val knownElements = findKnownElements(changeSetData.referencedElements)

            val page = ChangeSetPage(
              changeSetData.summary,
              changeSetInfo,
              changeSetData.networkChanges.map(toNetworkChangeInfo),
              changeSetData.routeChanges.map(toRouteChangeInfo),
              changeSetData.nodeChanges.map(toNodeChangeInfo),
              knownElements
            )
            Some(page)

          case _ =>
            // TODO CHANGE properly handle the case when there is more than one ChangeSetData object returned
            None
        }
      }
      else {
        None
      }
    }
  }

  private def findKnownElements(elements: ReferencedElements): KnownElements = {
    KnownElements(
      nodeIds = nodeRepository.filterKnown(elements.nodeIds),
      routeIds = routeRepository.filterKnown(elements.routeIds)
    )
  }

  private def toNetworkChangeInfo(networkChange: NetworkInfoChange): NetworkChangeInfo = {
    new NetworkChangeInfoBuilder().build(networkChange, Seq.empty)
  }

  private def toRouteChangeInfo(routeChange: RouteChange): RouteChangeInfo = {
    new RouteChangeInfoBuilder().build(routeChange, Seq.empty)
  }

  private def toNodeChangeInfo(nodeChange: NodeChange): NodeChangeInfo = {
    new NodeChangeInfoBuilder().build(nodeChange, Seq.empty)
  }
}
