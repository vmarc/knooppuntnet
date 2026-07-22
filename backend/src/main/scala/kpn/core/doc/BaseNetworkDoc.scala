package kpn.core.doc

import kpn.api.common.data.Tagable
import kpn.api.common.network.NetworkBaseData
import kpn.api.custom.Tag
import kpn.api.id.WithId

case class BaseNetworkDoc(
  _id: Long,
  active: Boolean,
  base: NetworkBaseData,
  nodeIds: Seq[Long],
  relationIds: Seq[Long],
) extends WithId with Tagable {
  def tags: Seq[Tag] = base.raw.tags
}
