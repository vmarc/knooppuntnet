package kpn.api.common.data.raw

import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RawWay(
  id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  nodeIds: Vector[Long],
  tags: Seq[Tag]
) extends RawElement {

  override def isWay: Boolean = true
}
