package kpn.shared.data.raw

import kpn.shared.Timestamp
import kpn.shared.data.Tags

case class RawWay(
  id: Long,
  version: Int,
  timestamp: Timestamp,
  changeSetId: Long,
  nodeIds: Seq[Long],
  tags: Tags
) extends RawElement {

  override def isWay: Boolean = true

}
