package kpn.core.doc

import kpn.api.common.data.raw.RawNode
import kpn.api.id.WithId

case class RawNodeDoc(
  _id: Long,
  node: RawNode
) extends WithId
