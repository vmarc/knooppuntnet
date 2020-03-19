package kpn.core.database.views.poi

import kpn.core.db.couch.Couch
import kpn.server.repository.PoiRepositoryImpl

/*
  Assesses performance of loading all poi information for one of the very large
  poi tiles. Was used while working on improving the performance.
 */
object PoiTileViewDemo {
  def main(args: Array[String]): Unit = {
    val tileName = "13-4195-2748"
    println(s"Tile $tileName - reading pois")
    Couch.executeIn("kpn-server", "pois5") { database =>
      val repo = new PoiRepositoryImpl(database)
      val t1 = System.currentTimeMillis()
      val poiInfos = repo.tilePoiInfos(tileName)
      val t2 = System.currentTimeMillis()
      println(s"loaded ${poiInfos.size} pois in ${t2 - t1}ms")
    }
  }
}
