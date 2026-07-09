package kpn.server.monitor.route.update

import org.locationtech.jts.geom.Coordinate

case class MonitorStateDeviationWorldCoordinates(
  id: Long,
  worldCoordinateLines: Seq[Seq[Coordinate]]
)
