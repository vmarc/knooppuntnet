package kpn.server.api.analysis.pages

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.Language
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetData
import kpn.api.common.changes.ChangeSetPage
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.KnownElements
import kpn.api.common.common.ReferencedElements
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.route.RouteChangeInfo
import kpn.api.custom.ChangeType
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.analysis.location.LocationService
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
  routeRepository: RouteRepository,
  locationService: LocationService
) extends ChangeSetPageBuilder {

  def build(user: Option[String], language: Language, changeSetId: Long, replicationId: Option[ReplicationId]): Option[ChangeSetPage] = {

    if (changeSetId == 1L) {
      Some(ChangeSetPageExample.page)
    }
    else {
      if (user.isDefined) {
        changeSetRepository.changeSet(changeSetId, replicationId) match {
          case Seq(changeSetData) =>

            val changeSetInfo = changeSetInfoRepository.get(changeSetId)
            val knownElements = findKnownElements(changeSetData.referencedElements)
            val networkChanges = changeSetData.networkChanges.map(toNetworkChangeInfo)
            val routeChanges = changeSetData.routeChanges.zipWithIndex.map { case (routeChange, index) =>
              toRouteChangeInfo(index, routeChange)
            }
            val nodeChanges = changeSetData.nodeChanges.map(toNodeChangeInfo)
            val orphanRouteChanges = buildOrphanRouteChanges(changeSetData)
            val orphanNodeChanges = buildOrphanNodeChanges(changeSetData)

            val locationChanges = changeSetData.summary.locationChanges.map { change =>
              change.copy(
                locationNames = change.locationNames.map(locationName => locationService.name(language, locationName))
              )
            }
            val locations = changeSetData.summary.locations.map(loc => locationService.name(language, loc))

            val summary = changeSetData.summary.copy(
              locationChanges = locationChanges,
              locations = locations
            )

            Some(
              ChangeSetPage(
                summary,
                changeSetInfo,
                networkChanges,
                orphanRouteChanges,
                orphanNodeChanges,
                routeChanges,
                nodeChanges,
                knownElements
              )
            )

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
    new NetworkChangeInfoBuilder().build(0, networkChange, Seq.empty)
  }

  private def toRouteChangeInfo(index: Int, routeChange: RouteChange): RouteChangeInfo = {
    new RouteChangeInfoBuilder().build(index, routeChange, Seq.empty)
  }

  private def toNodeChangeInfo(nodeChange: NodeChange): NodeChangeInfo = {
    new NodeChangeInfoBuilder().build(nodeChange, Seq.empty)
  }

  private def buildOrphanRouteChanges(changeSetData: ChangeSetData): Seq[ChangeSetSubsetElementRefs] = {

    val referencedInNetworkChangeRouteIds = changeSetData.networkChanges.flatMap(_.routeDiffs.ids).distinct.sorted

    Subset.all.flatMap { subset =>

      val subsetOrphanRouteChanges = changeSetData.routeChanges.filter { routeChange =>
        routeChange.subsets.contains(subset) && !referencedInNetworkChangeRouteIds.contains(routeChange.id)
      }

      val removed = subsetOrphanRouteChanges.filter(_.changeType == ChangeType.Delete).map(toChangeSetElementRef)
      val added = subsetOrphanRouteChanges.filter(_.changeType == ChangeType.Create).map(toChangeSetElementRef)
      val updated = subsetOrphanRouteChanges.filter(_.changeType == ChangeType.Update).map(toChangeSetElementRef)

      if (removed.nonEmpty || added.nonEmpty || updated.nonEmpty) {
        Some(
          ChangeSetSubsetElementRefs(
            subset,
            ChangeSetElementRefs(
              removed,
              added,
              updated
            )
          )
        )
      }
      else {
        None
      }
    }
  }

  private def buildOrphanNodeChanges(changeSetData: ChangeSetData): Seq[ChangeSetSubsetElementRefs] = {

    val referencedInNetworkChangeNodeIds = changeSetData.networkChanges.flatMap(_.nodeDiffs.ids).distinct.sorted

    Subset.all.flatMap { subset =>

      val subsetOrphanNodeChanges = changeSetData.nodeChanges.filter { nodeChange =>
        nodeChange.subsets.contains(subset) && !referencedInNetworkChangeNodeIds.contains(nodeChange.id)
      }

      val removed = subsetOrphanNodeChanges.filter(_.changeType == ChangeType.Delete).map(toChangeSetElementRef)
      val added = subsetOrphanNodeChanges.filter(_.changeType == ChangeType.Create).map(toChangeSetElementRef)
      val updated = subsetOrphanNodeChanges.filter(_.changeType == ChangeType.Update).map(toChangeSetElementRef)

      if (removed.nonEmpty || added.nonEmpty || updated.nonEmpty) {
        Some(
          ChangeSetSubsetElementRefs(
            subset,
            ChangeSetElementRefs(
              removed,
              added,
              updated
            )
          )
        )
      }
      else {
        None
      }
    }
  }

  private def toChangeSetElementRef(routeChange: RouteChange): ChangeSetElementRef = {
    ChangeSetElementRef(
      routeChange.id,
      routeChange.name,
      routeChange.happy,
      routeChange.investigate
    )
  }

  private def toChangeSetElementRef(nodeChange: NodeChange): ChangeSetElementRef = {
    ChangeSetElementRef(
      nodeChange.id,
      nodeChange.name,
      nodeChange.happy,
      nodeChange.investigate
    )
  }
}
