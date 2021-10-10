package kpn.server.api.analysis.pages.node

import kpn.api.common.changes.filter.ChangesFilter
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
        val changesFilter = changeSetRepository.nodeChangesFilter(nodeId, parameters.year, parameters.month, parameters.day)
        val totalCount = changesFilter.currentItemCount(parameters.impact)
        val changes = nodeChanges.map { change =>
          NodeChangeInfo(
            change.id,
            change.after.map(_.version),
            change.key,
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
            change.happy,
            change.investigate
          )
        }
        NodeChangesPage(
          nodeDoc._id,
          nodeDoc.name,
          changesFilter,
          changes,
          totalCount,
          changesFilter.totalCount
        )
      }
      else {
        // user is not logged in; we do not show change information
        NodeChangesPage(
          nodeDoc._id,
          nodeDoc.name,
          ChangesFilter.empty,
          Seq.empty,
          0,
          0
        )
      }
    }
  }
}
