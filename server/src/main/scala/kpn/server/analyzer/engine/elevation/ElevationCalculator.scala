package kpn.server.analyzer.engine.elevation

import kpn.core.common.LatLonD

class ElevationCalculator {

  def calculate(latLons: Seq[LatLonD]): Seq[DistanceElevation] = {
    latLons.sliding(2).flatMap { case Seq(a, b) =>
      calculateSegment(LatLonLine(a, b))
    }.toSeq
  }

  private def calculateSegment(line: LatLonLine): Seq[DistanceElevation] = {

    val aTile = ElevationTile(line.a)
    val bTile = ElevationTile(line.b)

    val infos = if (aTile == bTile) {
      Seq(DistanceTile(line.distance, aTile))
    }
    else {

      val tileQueue = scala.collection.mutable.Queue[ElevationTile]()
      val foundTiles = scala.collection.mutable.Set[ElevationTile]()

      tileQueue += aTile
      tileQueue += bTile

      foundTiles += aTile
      foundTiles += bTile

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
    line: LatLonLine,
    tile: ElevationTile,
    side: LatLonLine,
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
