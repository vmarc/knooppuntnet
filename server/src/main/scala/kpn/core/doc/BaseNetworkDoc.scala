package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class BaseNetworkDoc(
  _id: Long,
  active: Boolean,
  routeType: RouteType,
  routeScope: RouteScope,
  name: Option[String],
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  members: Seq[RawMember],
  tags: Seq[Tag],
  nodeIds: Seq[Long],
  relationIds: Seq[Long],
) extends WithId with Tagable
