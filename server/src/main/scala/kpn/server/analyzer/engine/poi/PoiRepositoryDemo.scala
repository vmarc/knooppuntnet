package kpn.server.analyzer.engine.poi

import kpn.core.db.couch.Couch
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

object PoiRepositoryDemo {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-server", "pois3") { database =>
      val repo: PoiRepository = new PoiRepositoryImpl(database)

      println("Sleeping before starting to load poi ids")
      Thread.sleep(30000)
      println("Start reading node ids")
      val t1 = System.currentTimeMillis()
      val nodeIds = repo.nodeIds(stale = false)
      val t2 = System.currentTimeMillis()
      println(s"Finished reading ${nodeIds.size} node ids")
      val wayIds = repo.wayIds(stale = false)
      val t3 = System.currentTimeMillis()
      println(s"Finished reading ${wayIds.size} way ids")
      val relationIds = repo.relationIds(stale = false)
      val t4 = System.currentTimeMillis()
      println(s"Finished reading ${relationIds.size} relation ids")

      println(s"Finished reading node ids in ${t2 - t1}ms")
      println(s"Finished reading way ids in ${t3 - t2}ms")
      println(s"Finished reading relation ids in ${t4 - t3}ms")
      println(s"Overall ${t4 - t1}ms")
      println("Sleeping")
      Thread.sleep(300000)
    }
  }
}
