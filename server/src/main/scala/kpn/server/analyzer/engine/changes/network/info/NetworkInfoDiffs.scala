package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs

case class NetworkInfoDiffs(
  nodeDiffs: RefDiffs = RefDiffs(),
  routeDiffs: RefDiffs = RefDiffs(),
  extraNodeDiffs: IdDiffs = IdDiffs(),
  extraWayDiffs: IdDiffs = IdDiffs(),
  extraRelationDiffs: IdDiffs = IdDiffs()
) {

  def happy: Boolean = {
    nodeDiffs.added.nonEmpty ||
      routeDiffs.added.nonEmpty ||
      extraNodeDiffs.removed.nonEmpty ||
      extraWayDiffs.removed.nonEmpty ||
      extraRelationDiffs.removed.nonEmpty
  }

  def investigate: Boolean = {
    nodeDiffs.removed.nonEmpty ||
      routeDiffs.removed.nonEmpty ||
      extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty
  }
}
