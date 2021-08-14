package kpn.core.tools.poi

import kpn.core.mongo.util.Mongo
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

/*
  Removes all poi tiles for which there are no poi definitions in
  the database. When the poi tiles are completely in sync, the net
  result should be that running this logic does nothing.
 */
object PoiTileCleanTool {
  def main(args: Array[String]): Unit = {
    println("Start")
    Mongo.executeIn("kpn-test") { poiDatabase =>
      val poiRepository = new PoiRepositoryImpl(poiDatabase)
      val tileFileRepository = new TileFileRepositoryImpl("/kpn/tiles", "mvt")
      new PoiTileCleanTool(poiRepository, tileFileRepository).clean()
    }
    println("Done")
  }
}

class PoiTileCleanTool(
  poiRepository: PoiRepository,
  tileFileRepository: TileFileRepository
) {

  def clean(): Unit = {
    11 to 15 foreach { z =>
      println(s"Processing zoomlevel $z")
      val existingTileNames = tileFileRepository.existingTileNames("poi", z)
      println(s"Number of tiles before: " + existingTileNames.size)
      val obsoleteTileNames = existingTileNames.zipWithIndex.filter { case (tileName, index) =>
        if ((index % 50) == 0) {
          println(s"$z $index/${existingTileNames.size}")
        }
        val shortTileName = tileName.drop("poi-".length)
        poiRepository.tilePoiInfos(shortTileName).isEmpty
      }.map(_._1)
      println(s"$z delete ${obsoleteTileNames.size} obsolete tiles")
      tileFileRepository.delete(obsoleteTileNames)
    }
  }
}
