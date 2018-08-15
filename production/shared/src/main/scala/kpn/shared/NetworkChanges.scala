package kpn.shared

import kpn.shared.common.ToStringBuilder

case class NetworkChanges(
  creates: Seq[ChangeSetNetwork] = Seq(),
  updates: Seq[ChangeSetNetwork] = Seq(),
  deletes: Seq[ChangeSetNetwork] = Seq()
) {

  def nonEmpty: Boolean = creates.nonEmpty || updates.nonEmpty || deletes.nonEmpty

  def happy: Boolean = creates.exists(_.happy) || updates.exists(_.happy) || deletes.exists(_.happy)

  def investigate: Boolean = creates.exists(_.investigate) || updates.exists(_.investigate) || deletes.exists(_.investigate)

  def subsets: Set[Subset] = subsetsIn(creates) ++ subsetsIn(updates) ++ subsetsIn(deletes)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("creates", creates).
    optionalCollection("updates", updates).
    optionalCollection("deletes", deletes).
    build

  private def subsetsIn(changes: Seq[ChangeSetNetwork]): Set[Subset] = changes.flatMap(_.subsets).toSet

}
