package kpn.api.common.changes

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Timestamp

/**
 * All information of a given changeset as available in the minute diff file. A changeset can be spread
 * over multiple diff files. In that case the information in this object is not the complete changeset.
 */
case class ChangeSet(
  id: Long,
  timestamp: Timestamp, // timestamp found in minute diff state file
  timestampFrom: Timestamp,
  timestampUntil: Timestamp,
  timestampBefore: Timestamp,
  timestampAfter: Timestamp,
  changes: Seq[Change]
) {

  def relations(action: ChangeAction): Seq[RawRelation] = {
    elements(action).collect { case e: RawRelation => e }
  }

  def elements(action: ChangeAction): Seq[RawElement] = {
    changes.filter(_.action == action).flatMap(_.elements)
  }

  def nodes(action: ChangeAction): Seq[RawNode] = {
    elements(action).collect { case e: RawNode => e }
  }
}
