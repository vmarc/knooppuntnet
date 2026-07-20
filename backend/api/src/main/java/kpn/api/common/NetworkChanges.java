package kpn.api.common;

import kpn.api.common.ChangeSetNetwork;

import com.google.common.collect.ImmutableList;

public record NetworkChanges(
  ImmutableList<ChangeSetNetwork> creates,
  ImmutableList<ChangeSetNetwork> updates,
  ImmutableList<ChangeSetNetwork> deletes
) {}

/* TODO migrate

  def nonEmpty: Boolean = creates.nonEmpty || updates.nonEmpty || deletes.nonEmpty

  def happy: Boolean = creates.exists(_.happy) || updates.exists(_.happy) || deletes.exists(_.happy)

  def investigate: Boolean = creates.exists(_.investigate) || updates.exists(_.investigate) || deletes.exists(_.investigate)

  def subsets: Set[Subset] = subsetsIn(creates) ++ subsetsIn(updates) ++ subsetsIn(deletes)

  private def subsetsIn(changes: Seq[ChangeSetNetwork]): Set[Subset] = changes.flatMap(_.subsets).toSet

*/
