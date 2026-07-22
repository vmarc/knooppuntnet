package kpn.core.doc

import kpn.api.id.Storable

case class LocationNodeCount(
  name: String,
  count: Long
) extends Storable
