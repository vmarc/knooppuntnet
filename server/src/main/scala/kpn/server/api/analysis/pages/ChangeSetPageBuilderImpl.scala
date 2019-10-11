package kpn.server.api.analysis.pages

import kpn.core.engine.changes.builder.NetworkChangeInfoBuilder
import kpn.core.engine.changes.builder.NodeChangeInfoBuilder
import kpn.core.engine.changes.builder.RouteChangeInfoBuilder
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RouteRepository
import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSetPage
import kpn.shared.changes.Review
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.KnownElements
import kpn.shared.common.ReferencedElements
import kpn.shared.node.NodeChangeInfo
import kpn.shared.route.RouteChangeInfo
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
            val reviews = Seq[Review]() // TODO reviewFacade.get(changeSetId, replicationId)

            val knownElements = findKnownElements(changeSetData.referencedElements)

            val page = ChangeSetPage(
              changeSetData.summary,
              changeSetInfo,
              changeSetData.networkChanges.map(toNetworkChangeInfo),
              changeSetData.routeChanges.map(toRouteChangeInfo),
              changeSetData.nodeChanges.map(toNodeChangeInfo),
              knownElements,
              reviews
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

  private def toNetworkChangeInfo(networkChange: NetworkChange): NetworkChangeInfo = {
    new NetworkChangeInfoBuilder().build(networkChange, Seq())
  }

  private def toRouteChangeInfo(routeChange: RouteChange): RouteChangeInfo = {
    new RouteChangeInfoBuilder().build(routeChange, Seq())
  }

  private def toNodeChangeInfo(nodeChange: NodeChange): NodeChangeInfo = {
    new NodeChangeInfoBuilder().build(nodeChange, Seq())
  }
}
