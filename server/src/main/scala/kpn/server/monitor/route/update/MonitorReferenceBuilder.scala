package kpn.server.monitor.route.update

import kpn.api.common.tiles.ZoomLevel
import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import kpn.server.json.Json
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorReferenceTile
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment
import org.springframework.stereotype.Component

@Component
class MonitorReferenceBuilder(
  lineSegmentTileCalculator: LineSegmentTileCalculator
) {

  private val log = Log(classOf[MonitorReferenceBuilder])

  def build(reference: MonitorReference): MonitorReference = {
    val referenceTiles = buildReferenceTiles(reference)
    reference.copy(tiles = referenceTiles)
  }

  private def buildReferenceTiles(reference: MonitorReference): Seq[MonitorReferenceTile] = {
    val worldCoordinateReferenceLines = buildWorldCoordinateReferenceLines(reference)
    val tiles = tilesForReference(worldCoordinateReferenceLines)
    val referenceTiles = tiles.flatMap { tile =>
      val lines = worldCoordinateReferenceLines.flatMap(worldCoordinates => toTileLine(tile, worldCoordinates))
      if (lines.isEmpty) {
        None
      }
      else {
        Some(
          MonitorReferenceTile(
            tile.z,
            tile.x,
            tile.y,
            lines
          )
        )
      }
    }
    referenceTiles
  }

  private def buildWorldCoordinateReferenceLines(reference: MonitorReference): Seq[Seq[Coordinate]] = {
    reference.referenceLines.map { line =>
      val coordinates = Json.value(line, classOf[CoordinateArray]).coordinates
      coordinates.toSeq.map { c =>
        val x = lonToWorldX(c.getX)
        val y = latToWorldY(c.getY)
        new Coordinate(x, y)
      }
    }
  }

  private def tilesForReference(worldCoordinateReferenceLines: Seq[Seq[Coordinate]]): Seq[Tile] = {
    worldCoordinateReferenceLines.flatMap { worldCoordinates =>
      val lineSegments = worldCoordinates
        .sliding(2)
        .map { case Seq(c1, c2) => new LineSegment(c1, c2) }
        .toSeq
      (ZoomLevel.newMinZoom to ZoomLevel.newMaxZoom).flatMap { z =>
        lineSegmentTileCalculator.tiles(z, lineSegments)
      }
    }
  }

  private def toTileLine(tile: Tile, worldCoordinates: Seq[Coordinate]): Option[String] = {
    val tileCoordinates = TileUtil.routeTileCoordinates(tile, worldCoordinates)
    if (tileCoordinates.nonEmpty) {
      Some(tileCoordinates
        .map(coordinate => s"[${coordinate.x},${coordinate.y}]")
        .mkString("[", ",", "]")
      )
    }
    else {
      None
    }
  }
}
