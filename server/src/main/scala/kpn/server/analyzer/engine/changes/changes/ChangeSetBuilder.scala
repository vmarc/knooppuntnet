package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Change
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.server.analyzer.engine.context.ElementIds

object ChangeSetBuilder {

  def from(timestamp: Timestamp, osmChange: OsmChange): Seq[ChangeSet] = {
    osmChange.allChangeSetIds.flatMap { changeSetId =>
      val actions = osmChange.actions.flatMap { action =>
        val elements = action.elements.filter(_.changeSetId == changeSetId)
        if (elements.nonEmpty) {
          Some(Change(action.action, elements))
        }
        else {
          None
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

  def elementIdsIn(changeSet: ChangeSet): ElementIds = {
    val elements = changeSet.changes.flatMap(_.elements)
    val nodeIds = elements.filter(_.isNode).map(_.id).toSet
    val wayIds = elements.filter(_.isWay).map(_.id).toSet
    val relationIds = elements.filter(_.isRelation).map(_.id).toSet
    ElementIds(nodeIds, wayIds, relationIds)
  }

}
