package kpn.server.analyzer.engine.changes.network

import kpn.api.base.WithStringId
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.common.Ref
import kpn.api.common.diff.IdDiffs
import kpn.api.custom.ChangeType

case class NetworkChange(
  _id: String,
  key: ChangeKey,
  networkId: Long,
  networkName: String,
  changeType: ChangeType,
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
) extends WithStringId {

  def toRef: Ref = {
    Ref(networkId, networkName)
  }

  def impactedNodeIds: Seq[Long] = {
    nodes.ids
  }

  def impactedRelationIds: Seq[Long] = {
    relations.ids
  }
}
