package kpn.core.doc

import kpn.api.common.network.NetworkShape
import kpn.api.id.WithId

case class NetworkShapeDoc(
  _id: Long,
  shape: Option[NetworkShape]
) extends WithId
