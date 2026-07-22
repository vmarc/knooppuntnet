package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Change
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.server.analyzer.engine.context.ChangeElementIds

object ChangeSetBuilder {

  def from(timestamp: Timestamp, osmChange: OsmChange): Seq[ChangeSet] = {
    osmChange.allChangeSetIds.flatMap { changeSetId =>
      val actions = osmChange.actions.flatMap { action =>
        val nodes = action.nodes.filter(_.changeSetId == changeSetId)
        val ways = action.ways.filter(_.changeSetId == changeSetId)
        val relations = action.relations.filter(_.changeSetId == changeSetId)
        Option.when(nodes.nonEmpty || ways.nonEmpty || relations.nonEmpty) {
          Change(action.action, nodes, ways, relations)
        }
      }
      if (actions.nonEmpty) {
        val timestamps = actions.flatMap(_.elements).map(_.timestamp).sorted
        val timestampFrom = timestamps.head // oldest timestamp
        val timestampUntil = timestamps.last // youngest timestamp
        val timestampBefore = TimestampUtil.relativeSeconds(timestampFrom, -1)
        val timestampAfter = TimestampUtil.relativeSeconds(timestampUntil, 1)

        Some(
          ChangeSet(
            changeSetId,
            timestamp,
            timestampFrom,
            timestampUntil,
            timestampBefore,
            timestampAfter,
            actions
          )
        )
      }
      else {
        None
      }
    }.toSeq.sortBy(_.id)
  }

  def elementIdsIn(changeSet: ChangeSet): ChangeElementIds = {
    val nodeIds = changeSet.changes.flatMap(_.nodes).map(_.id).toSet
    val wayIds = changeSet.changes.flatMap(_.ways).map(_.id).toSet
    val relationIds = changeSet.changes.flatMap(_.relations).map(_.id).toSet
    ChangeElementIds(nodeIds, wayIds, relationIds)
  }
}
