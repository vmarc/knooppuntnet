package kpn.core.doc

import kpn.api.base.WithId
import kpn.api.common.data.raw.RawRelation

case class RawNetworkDoc(
  _id: Long,
  relation: RawRelation
) extends WithId
