package kpn.core.doc

import kpn.api.common.data.raw.RawNode

case class RawNodeDoc(
  _id: Long,
  node: RawNode
) extends WithId
