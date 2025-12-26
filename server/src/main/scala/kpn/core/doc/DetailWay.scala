package kpn.core.doc

import kpn.api.common.data.MetaData
import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class DetailWay(
  id: Long,
  version: Long,
  changeSetId: Long,
  timestamp: Timestamp,
  tags: Seq[Tag],
  nodeIds: Seq[Long],
  coordinates: String
) extends Tagable with Storable {
  def toMeta: MetaData = {
    MetaData(
      version,
      timestamp,
      changeSetId
    )
  }
}
