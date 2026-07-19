package kpn.core.doc

import kpn.api.common.data.raw.RawRelation

case class RawNetworkDoc(
  _id: Long,
  relation: RawRelation
) extends WithId
