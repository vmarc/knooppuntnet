package kpn.core.tools.poi

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.repository.PoiRepositoryImpl

object PoiExplorerTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("pois4") { database =>
      val repo = new PoiRepositoryImpl(database)
      val poiInfos = repo.allPois()
      poiInfos.zipWithIndex.foreach { case (poiInfo, index) =>
        if (index % 1000 == 0) {
          println(s"$index/${poiInfos.size}")
        }
        repo.get(PoiRef(poiInfo.elementType, poiInfo.elementId)) foreach { poi =>
          poi.tags("mapillary") foreach { tagValue =>
            println(tagValue)
          }
        }
      }
    }
  }
}
