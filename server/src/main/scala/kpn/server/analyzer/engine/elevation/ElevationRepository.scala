package kpn.server.analyzer.engine.elevation

import kpn.server.analyzer.engine.tiles.domain.Point

trait ElevationRepository {

  def tileCount: Int

  def elevation(point: Point): Option[Int]

}
