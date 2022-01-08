package kpn.server.api.analysis.pages.node

import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.node.NodeChangesPage
import kpn.api.custom.Tags
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeChangesPageBuilderImpl(
  nodeRepository: NodeRepository,
  changeSetRepository: ChangeSetRepository
) extends NodeChangesPageBuilder {

  override def build(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    if (nodeId == 1) {
      Some(NodeChangesPageExample.page)
    }
    else {
      buildPage(user, nodeId, parameters)
    }
  }

  private def buildPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage] = {
    nodeRepository.nodeWithId(nodeId).map { nodeDoc =>
      if (user.isDefined) {
        val nodeChanges = changeSetRepository.nodeChanges(nodeId, parameters)
        val filterOptions = changeSetRepository.nodeChangesFilter(nodeId, parameters.year, parameters.month, parameters.day)
        val totalCount = if (filterOptions.isEmpty) 0 else filterOptions.head.totalCount
        val changeCount = ChangesFilterOption.changesCount(filterOptions, parameters)
        val changes = nodeChanges.zipWithIndex.map { case (change, index) =>
          val rowIndex = parameters.pageSize * parameters.pageIndex + index
          NodeChangeInfo(
            rowIndex,
            change.id,
            change.after.map(_.version),
            change.key,
            change.changeType,
            Tags.empty,
            change.comment,
            change.before,
            change.after,
            change.connectionChanges,
            change.roleConnectionChanges,
            change.definedInNetworkChanges,
            change.tagDiffs,
            change.nodeMoved,
            change.addedToRoute,
            change.removedFromRoute,
            change.addedToNetwork,
            change.removedFromNetwork,
            change.factDiffs,
            change.facts,
            change.initialTags,
            change.initialLatLon,
            change.happy,
            change.investigate
          )
        }
        NodeChangesPage(
          nodeDoc._id,
          nodeDoc.name,
          filterOptions,
          changes,
          changeCount,
          totalCount,
        )
      }
      else {
        // user is not logged in; we do not show change information
        NodeChangesPage(
          nodeDoc._id,
          nodeDoc.name,
          Seq.empty,
          Seq.empty,
          0,
          0
        )
      }
    }
  }
}
