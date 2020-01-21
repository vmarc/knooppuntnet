package kpn.server.analyzer.engine.elevation

import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point

class ElevationCalculator {

  def calculate(points: Seq[Point]): Seq[DistanceElevation] = {
    points.sliding(2).flatMap { case Seq(p1, p2) =>
      calculateSegment(Line(p1, p2))
    }.toSeq
  }

  private def calculateSegment(line: Line): Seq[DistanceElevation] = {

    val tile1 = ElevationTile(line.p1)
    val tile2 = ElevationTile(line.p2)

    val infos = if (tile1 == tile2) {
      Seq(DistanceTile(line.length, tile1))
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
        explore(tileQueue, foundTiles, line, tile, tile.left, -1, 0)
        explore(tileQueue, foundTiles, line, tile, tile.right, 1, 0)
        explore(tileQueue, foundTiles, line, tile, tile.top, 0, 1)
        explore(tileQueue, foundTiles, line, tile, tile.bottom, 0, -1)
      }
      foundTiles.toSeq

      Seq.empty
    }

    Seq.empty // TODO pick up heights and distances
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
