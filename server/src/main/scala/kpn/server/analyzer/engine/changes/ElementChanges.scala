package kpn.server.analyzer.engine.changes

import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeAction.ChangeAction

case class ElementChanges(
  creates: Seq[Long] = Seq.empty,
  updates: Seq[Long] = Seq.empty,
  deletes: Seq[Long] = Seq.empty
) {

  def isEmpty: Boolean = creates.isEmpty && updates.isEmpty && deletes.isEmpty

  def nonEmpty: Boolean = creates.nonEmpty || updates.nonEmpty || deletes.nonEmpty

  def actionCount: Int = creates.size + updates.size + deletes.size

  def elementIds: Seq[Long] = (creates ++ updates ++ deletes).distinct.sorted

  def action(elementId: Long): ChangeAction = {
    if (creates.contains(elementId)) {
      ChangeAction.Create
    }
    else if (updates.contains(elementId)) {
      ChangeAction.Modify
    }
    else if (deletes.contains(elementId)) {
      ChangeAction.Delete
    }
    else {
      ChangeAction.Modify
    }
  }

}
