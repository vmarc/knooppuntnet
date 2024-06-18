package kpn.core.tools.next.domain

import kpn.api.base.WithId
import kpn.api.common.data.Member
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class NextRouteRelation(
  _id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Seq[Tag],
  members: Seq[Member]
) extends WithId
