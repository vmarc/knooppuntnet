package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment

trait LineSegmentTileCalculator {

  def tiles(z: Int, lineSegments: Seq[LineSegment]): Seq[Tile]

  def tilesForLines(worldCoordinateReferenceLines: Seq[Seq[Coordinate]]): Seq[Tile]
}
