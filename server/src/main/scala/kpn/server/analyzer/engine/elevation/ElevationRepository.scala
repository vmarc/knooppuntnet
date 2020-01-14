package kpn.server.analyzer.engine.elevation

import kpn.api.common.LatLon

trait ElevationRepository {

  def elevation(latLon: LatLon): Option[Double]

}
