package kpn.api.common.diff

import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawWay

case class WayData(
  way: RawWay,
  nodes: Seq[RawNode]
) {
  def id: Long = way.id
}
