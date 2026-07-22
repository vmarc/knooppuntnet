package kpn.core.poi

import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.worldXtoLon
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.worldYtoLat
import kpn.server.analyzer.engine.tiles.domain.Rectangle
import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.TileId

import java.text.DecimalFormat

object PoiLocation {

  val belgiumAndNetherlands: Rectangle = latLonBoundsFrom(TileId(10, 519, 331), TileId(10, 532, 349))
  private val germany = latLonBoundsFrom(TileId(10, 535, 355), TileId(10, 536, 356))
  private val germanyNorth = latLonBoundsFrom(TileId(10, 531, 330), TileId(10, 536, 338))
  private val germanySouth = latLonBoundsFrom(TileId(10, 528, 338), TileId(10, 537, 344))
  private val germanyEast = latLonBoundsFrom(TileId(10, 544, 331), TileId(10, 553, 341))
  private val austria = latLonBoundsFrom(TileId(10, 552, 358), TileId(10, 555, 360))
  private val france = latLonBoundsFrom(TileId(10, 518, 343), TileId(10, 520, 344))
  private val spain = latLonBoundsFrom(TileId(10, 508, 380), TileId(10, 509, 381))

  val allBoundingBoxes: Seq[Rectangle] = Seq(
    belgiumAndNetherlands,
    germany,
    germanyNorth,
    germanySouth,
    germanyEast,
    austria,
    france,
    spain
  )

  val simpleBoundingBoxes: Seq[Rectangle] = Seq(
    germany,
    germanyNorth,
    germanySouth,
    germanyEast,
    austria,
    france,
    spain
  )

  val boundingBoxStrings: Seq[String] = {
    val formatter = new DecimalFormat("#.####")
    allBoundingBoxes.map { bounds =>
      val minLat = formatter.format(bounds.yMin) // south
      val minLon = formatter.format(bounds.xMin) // west
      val maxLat = formatter.format(bounds.yMax) // north
      val maxLon = formatter.format(bounds.xMax) // east
      s"($minLat, $minLon, $maxLat, $maxLon)"
    }
  }

  private def latLonBoundsFrom(topLeftTileId: TileId, bottomRightTileId: TileId): Rectangle = {
    val topLeftTile = RouteTiles.tile(topLeftTileId)
    val bottomRightTile = RouteTiles.tile(bottomRightTileId)
    Rectangle(
      xMin = worldXtoLon(topLeftTile.bounds.xMin),
      xMax = worldXtoLon(bottomRightTile.bounds.xMax),
      yMin = worldYtoLat(bottomRightTile.bounds.yMax),
      yMax = worldYtoLat(topLeftTile.bounds.yMin)
    )
  }
}
