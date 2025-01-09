package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class BaseNetworkDoc(
  _id: Long,
  name: Option[String],
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  members: Seq[RawMember],
  tags: Seq[Tag],
  nodeIds: Seq[Long],
  routeIds: Seq[Long],
) extends WithId
