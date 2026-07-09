package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.diff.network.NetworkNodeDiff

case class NetworkNodeUpdate(
  before: NetworkNodeData,
  after: NetworkNodeData,
  diffs: NetworkNodeDiff
) {

  def id: Long = after.id

  def name: String = after.name

  def toRef: Ref = Ref(id, name)

  def happy: Boolean = diffs.happy

  def investigate: Boolean = diffs.investigate
}
