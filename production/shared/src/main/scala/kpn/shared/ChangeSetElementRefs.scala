package kpn.shared

import kpn.shared.common.ToStringBuilder
import kpn.shared.diff.RefDiffs

object ChangeSetElementRefs {
  val empty = ChangeSetElementRefs(Seq(), Seq(), Seq())
}

case class ChangeSetElementRefs(
  removed: Seq[ChangeSetElementRef]= Seq.empty,
  added: Seq[ChangeSetElementRef]= Seq.empty,
  updated: Seq[ChangeSetElementRef]= Seq.empty
) {

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty

  def elementIds: Seq[Long] = removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)

  def happy: Boolean = removed.exists(_.happy) || added.exists(_.happy) || updated.exists(_.happy)

  def investigate: Boolean = removed.exists(_.investigate) || added.exists(_.investigate) || updated.exists(_.investigate)

  def referencedElementIds: Set[Long] = {
    (removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)).toSet
  }

  def toRefDiffs: RefDiffs = {
    RefDiffs(
      removed.map(_.toRef),
      added.map(_.toRef),
      updated.map(_.toRef)
    )
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("removed", removed).
    optionalCollection("added", added).
    optionalCollection("updated", updated).
    build
}
