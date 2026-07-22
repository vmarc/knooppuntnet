package kpn.database.actions.locations

import kpn.api.id.Storable

case class LocationQueryResult(
  name: String,
  nodeCount: Long,
  routeCount: Long,
  factCount: Long
) extends Storable
