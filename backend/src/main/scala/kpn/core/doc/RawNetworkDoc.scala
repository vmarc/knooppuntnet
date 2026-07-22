package kpn.core.doc

import kpn.api.common.data.raw.RawRelation
import kpn.api.id.WithId

case class RawNetworkDoc(
  _id: Long,
  relation: RawRelation
) extends WithId
