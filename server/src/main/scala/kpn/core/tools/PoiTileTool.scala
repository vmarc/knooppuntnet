package kpn.core.tools

import kpn.api.common.tiles.ZoomLevel
import kpn.core.db.couch.Couch
import kpn.core.tiles.PoiTilesBuilder
import kpn.core.tiles.TileRepositoryImpl
import kpn.core.tiles.vector.PoiVectorTileBuilder
import kpn.core.util.Log
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

/*
  Generates tiles for all points of interest in the database.

  Example use from sbt:
    runMain kpn.core.tools.PoiTileTool -t /kpn/tiles -d pois
 */
object PoiTileTool {

  val log = Log(classOf[PoiTileTool])

  def main(args: Array[String]): Unit = {

    val exit = PoiTileToolOptions.parse(args) match {
      case Some(options) =>

        Couch.executeIn(options.poiDatabaseName) { database =>

          val poiRepository = new PoiRepositoryImpl(database)
          val tileRepository = new TileRepositoryImpl(options.tileDir, "mvt")
          val tileTool = new PoiTileTool(
            poiRepository,
            new PoiTilesBuilder(
              new PoiVectorTileBuilder(),
              tileRepository
            )
          )

          tileTool.make()
        }

        log.info("Done")
        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

}

class PoiTileTool(
  poiRepository: PoiRepository,
  poiTilesBuilder: PoiTilesBuilder
) {

  def make(): Unit = {

    val pois = poiRepository.allPois()

    PoiTileTool.log.info(s"Processing ${pois.size} pois")

    (ZoomLevel.poiTileMinZoom to ZoomLevel.poiTileMaxZoom).foreach { z =>
      poiTilesBuilder.build(z, pois)
    }
  }

}
