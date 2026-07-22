package kpn.core.doc

import kpn.api.common.network.NetworkShape

case class NetworkShapeDoc(
  _id: Long,
  shape: Option[NetworkShape]
) extends WithId
