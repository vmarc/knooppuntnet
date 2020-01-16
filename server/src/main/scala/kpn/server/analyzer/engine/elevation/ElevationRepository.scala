package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD

trait ElevationRepository {

  def elevation(latLon: LatLonD): Option[Int]

}
