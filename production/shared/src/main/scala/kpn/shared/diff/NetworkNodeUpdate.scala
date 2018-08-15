package kpn.shared.diff

import kpn.shared.common.Ref
import kpn.shared.diff.network.NetworkNodeDiff

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
