package kpn.core.tools.poi

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

object PoiExplorerTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("pois4") { database =>
      val repo = new PoiRepositoryImpl(database)
      val poiRefs = loadPoiRefs(repo)
      poiRefs.zipWithIndex.foreach { case (poiRef, index) =>
        if (index % 1000 == 0) {
          println(s"$index/${poiRefs.size}")
        }
        repo.get(poiRef).toSeq.foreach { poi =>
          poi.tags.tags.foreach { tag =>
            if (tag.key.contains("wikimedia")) {
              println(tag)
            }
          }
        }
      }
    }
  }

  private def loadPoiRefs(repo: PoiRepository): Seq[PoiRef] = {
    repo.nodeIds().map(PoiRef.node) ++
      repo.wayIds().map(PoiRef.way) ++
      repo.relationIds().map(PoiRef.relation)
  }
}
