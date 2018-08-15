package kpn.shared.diff

import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawWay

case class WayData(
  way: RawWay,
  nodes: Seq[RawNode]
) {
  def id: Long = way.id
}
