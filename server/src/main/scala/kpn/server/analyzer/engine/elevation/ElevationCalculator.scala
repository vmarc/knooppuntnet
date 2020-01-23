package kpn.server.analyzer.engine.elevation

import kpn.core.util.Haversine
import kpn.server.analyzer.engine.tiles.CohenSutherland
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point

class ElevationCalculator(elevationRepository: ElevationRepository) {

  def calculate(points: Seq[Point]): Seq[DistanceElevation] = {
    DistanceElevationMerger.merge(
      points.sliding(2).toSeq.flatMap { case Seq(p1, p2) =>
        calculateSegment(Line(p1, p2))
      }
    )
  }

  private def calculateSegment(line: Line): Seq[DistanceElevation] = {

    val tile1 = ElevationTile(line.p1)
    val tile2 = ElevationTile(line.p2)

    if (tile1 == tile2) {
      elevationRepository.elevationFor(tile1).toSeq.map { elevation =>
        DistanceElevation(Haversine.distance(line), elevation)
      }
    }
    else {

      val tileQueue = scala.collection.mutable.Queue[ElevationTile]()
      val foundTiles = scala.collection.mutable.Set[ElevationTile]()

      tileQueue += tile1
      tileQueue += tile2

      foundTiles += tile1
      foundTiles += tile2

      while (tileQueue.nonEmpty) {
        val tile = tileQueue.dequeue()
        explore(tileQueue, foundTiles, line, tile, tile.bounds.left, -1, 0)
        explore(tileQueue, foundTiles, line, tile, tile.bounds.right, 1, 0)
        explore(tileQueue, foundTiles, line, tile, tile.bounds.top, 0, 1)
        explore(tileQueue, foundTiles, line, tile, tile.bounds.bottom, 0, -1)
      }

      println(s"line=$line, foundTiles=${foundTiles.map(t => t.fullName + " " + elevationRepository.elevationFor(t).get + "m")}")

      foundTiles.toSeq.flatMap { tile =>
        CohenSutherland.clip(tile.bounds, line).flatMap { linePart =>
          val distance = Haversine.distance(linePart)
          elevationRepository.elevationFor(tile).map { elevation =>

            println(s"    part=$linePart, distance=$distance, elevation=$elevation")

            DistanceElevation(distance, elevation)
          }
        }
      }
    }
  }

  private def explore(
    tileQueue: scala.collection.mutable.Queue[ElevationTile],
    foundTiles: scala.collection.mutable.Set[ElevationTile],
    line: Line,
    tile: ElevationTile,
    side: Line,
    xDelta: Int,
    yDelta: Int
  ): Unit = {
    if (line.intersects(side)) {
      val adjecentTile = tile.adjecent(xDelta, yDelta)
      if (!foundTiles.contains(adjecentTile)) {
        tileQueue += adjecentTile
        foundTiles += adjecentTile
        ()
      }
    }
  }

}
