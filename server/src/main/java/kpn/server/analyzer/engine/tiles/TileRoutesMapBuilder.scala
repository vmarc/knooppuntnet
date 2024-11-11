package kpn.server.analyzer.engine.tiles

import kpn.core.util.Log
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRoutes
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.LineSegment

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters.ImmutableSeqIsParallelizable

class TileRoutesMapBuilder(lineSegmentTileCalculator: LineSegmentTileCalculator) {

  private val log = Log(classOf[TileRoutesMapBuilder])

  def build(z: Int, tileRoutes: Seq[TileDataRoute]): Map[String, TileRoutes] = {

    val count = new AtomicInteger(0)
    var progress = new AtomicInteger(0)
    val map = scala.collection.mutable.Map[String, TileRoutes]()
    val context = Log.contextMessages
    tileRoutes.par.foreach { tileRoute =>
      Log.context(context) {
        val index = count.incrementAndGet()
        val allLineSegments = tileRoute.segments.flatMap { segment =>
          val worldCoordinates = segment.worldCoordinates.sliding(2).toSeq.map { case Seq(x, y) => new Coordinate(x, y) }
          worldCoordinates.sliding(2).toSeq.map { case Seq(c1, c2) => new LineSegment(c1, c2) }
        }
        val tiles = lineSegmentTileCalculator.tiles(z, allLineSegments)
        val currentProgress = (100d * (index + 1) / tileRoutes.size).round.toInt
        if (currentProgress > progress.get()) {
          progress.set(currentProgress)
          log.info(s"Build route map ${index + 1}/${tileRoutes.size} ${progress.get()}% tileCount=${map.size}")
        }
        tiles.foreach { tile =>
          map(tile.name) = map.get(tile.name) match {
            case Some(tileRoutes1) => TileRoutes(tile, tileRoutes1.routes :+ tileRoute)
            case None => TileRoutes(tile, Seq(tileRoute))
          }
        }
      }
    }
    log.info(s"Build route map ${tileRoutes.size}/${tileRoutes.size} 100% tileCount=${map.size}")
    map.toMap
  }
}
