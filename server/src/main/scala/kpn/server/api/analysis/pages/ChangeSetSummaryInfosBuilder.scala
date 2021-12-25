package kpn.server.api.analysis.pages

import kpn.api.common.AnalysisMode
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSummary
import kpn.api.common.ChangeSetSummaryInfo
import kpn.api.common.ChangeSetSummaryLocationInfo
import kpn.api.common.ChangeSetSummaryNetworkInfo
import kpn.api.common.LOCATION
import kpn.api.common.Language
import kpn.api.common.LocationChanges
import kpn.api.common.LocationChangesTree
import kpn.api.common.LocationChangesTreeNode
import kpn.api.common.NETWORK
import kpn.api.common.NetworkChanges
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetInfoRepository
import org.springframework.stereotype.Component

@Component
class ChangeSetSummaryInfosBuilder(
  changeSetInfoRepository: ChangeSetInfoRepository,
  locationService: LocationService
) {

  def toChangeSetSummaryInfos(language: Language, analysisMode: AnalysisMode, changeSetSummaries: Seq[ChangeSetSummary]): Seq[ChangeSetSummaryInfo] = {
    val changeSetIds = changeSetSummaries.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
    changeSetSummaries.map { summary =>
      val comment = changeSetInfos.find(s => s.id == summary.key.changeSetId).flatMap { changeSetInfo =>
        changeSetInfo.tags("comment")
      }
      analysisMode match {
        case LOCATION =>

          val changes = locationChanges(language, summary.trees)
          ChangeSetSummaryInfo(
            _id = summary._id,
            key = summary.key,
            comment = comment,
            subsets = summary.subsets,
            network = None,
            location = Some(
              ChangeSetSummaryLocationInfo(
                changes
              )
            ),
            happy = summary.happy,
            investigate = summary.investigate,
            impact = summary.impact,
          )

        case NETWORK =>

          val filteredSummary = filterChangeSetSummary(summary)

          ChangeSetSummaryInfo(
            _id = summary._id,
            key = summary.key,
            comment = comment,
            subsets = summary.subsets,
            network = Some(
              ChangeSetSummaryNetworkInfo(
                networkChanges = summary.networkChanges,
                routeChanges = filteredSummary.routeChanges,
                nodeChanges = filteredSummary.nodeChanges,
              )
            ),
            location = None,
            happy = summary.happy,
            investigate = summary.investigate,
            impact = summary.impact,
          )
      }
    }
  }

  private def translated(language: Language, treeNode: LocationChangesTreeNode): LocationChangesTreeNode = {
    treeNode.copy(
      locationName = locationService.name(language, treeNode.locationName),
      children = treeNode.children.map(child => translated(language, child))
    )
  }


  private def locationChanges(language: Language, trees: Seq[LocationChangesTree]): Seq[LocationChanges] = {
    trees.flatMap { tree =>
      tree.children.flatMap { treeNode =>
        childLocationChanges(
          language,
          tree.networkType,
          Seq(locationService.name(language, tree.locationName)),
          treeNode
        )
      }
    }
  }

  private def childLocationChanges(language: Language, networkType: NetworkType, locationNames: Seq[String], treeNode: LocationChangesTreeNode): Seq[LocationChanges] = {

    if (treeNode.children.isEmpty) {
      Seq(
        LocationChanges(
          networkType = networkType,
          locationNames = locationNames :+ locationService.name(language, treeNode.locationName),
          treeNode.routeChanges,
          treeNode.nodeChanges
        )
      )
    }
    else {
      treeNode.children.flatMap { childTreeNode =>
        childLocationChanges(
          language,
          networkType,
          locationNames :+ locationService.name(language, treeNode.locationName),
          childTreeNode
        )
      }
    }
  }

  private def filterChangeSetSummary(summary: ChangeSetSummary): ChangeSetSummary = {

    val networkReferencedRouteIds = routeIdsInNetworkChanges(summary.networkChanges)
    val routeChanges = summary.routeChanges.flatMap { changeSetSubsetElementRefs =>
      val oldElementRefs = changeSetSubsetElementRefs.elementRefs

      val removed = oldElementRefs.removed.filter(x => !networkReferencedRouteIds.contains(x.id))
      val added = oldElementRefs.added.filter(x => !networkReferencedRouteIds.contains(x.id))
      val updated = oldElementRefs.updated.filter(x => !networkReferencedRouteIds.contains(x.id))

      val elementRefs = ChangeSetElementRefs(
        removed,
        added,
        updated
      )
      if (elementRefs.nonEmpty) {
        Some(changeSetSubsetElementRefs.copy(elementRefs = elementRefs))
      }
      else {
        None
      }
    }

    val networkReferencedNodeIds: Seq[Long] = nodeIdsInNetworkChanges(summary.networkChanges)
    val nodeChanges = summary.nodeChanges.flatMap { changeSetSubsetElementRefs =>
      val oldElementRefs: ChangeSetElementRefs = changeSetSubsetElementRefs.elementRefs
      val removed = oldElementRefs.removed.filter(x => !networkReferencedNodeIds.contains(x.id))
      val added = oldElementRefs.added.filter(x => !networkReferencedNodeIds.contains(x.id))
      val updated = oldElementRefs.updated.filter(x => !networkReferencedNodeIds.contains(x.id))
      val elementRefs = ChangeSetElementRefs(
        removed,
        added,
        updated
      )
      if (elementRefs.nonEmpty) {
        Some(changeSetSubsetElementRefs.copy(elementRefs = elementRefs))
      }
      else {
        None
      }
    }

    summary.copy(
      routeChanges = routeChanges,
      nodeChanges = nodeChanges,
    )
  }

  private def routeIdsInNetworkChanges(networkChanges: NetworkChanges): Seq[Long] = {
    routeIdsIn(networkChanges.creates) ++
      routeIdsIn(networkChanges.updates) ++
      routeIdsIn(networkChanges.deletes)
  }

  private def nodeIdsInNetworkChanges(networkChanges: NetworkChanges): Seq[Long] = {
    nodeIdsIn(networkChanges.creates) ++
      nodeIdsIn(networkChanges.updates) ++
      nodeIdsIn(networkChanges.deletes)
  }

  private def routeIdsIn(changeSetNetworks: Seq[ChangeSetNetwork]): Seq[Long] = {
    changeSetNetworks.flatMap(changeSetNetwork => idsIn(changeSetNetwork.routeChanges))
  }

  private def nodeIdsIn(changeSetNetworks: Seq[ChangeSetNetwork]): Seq[Long] = {
    changeSetNetworks.flatMap(changeSetNetwork => idsIn(changeSetNetwork.nodeChanges))
  }

  private def idsIn(changeSetElementRefs: ChangeSetElementRefs): Seq[Long] = {
    changeSetElementRefs.removed.map(_.id) ++
      changeSetElementRefs.added.map(_.id) ++
      changeSetElementRefs.updated.map(_.id)
  }
}
