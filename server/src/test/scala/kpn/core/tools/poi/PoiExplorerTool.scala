package kpn.core.tools.poi

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.poi.analyzers.PoiImageAnalyzer
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
        repo.get(poiRef) foreach { poi =>
          poi.tags("image") foreach { tagValue =>
            if (!PoiImageAnalyzer.imagePrefixes.exists(prefix => tagValue.startsWith(prefix))) {
              if (!isImage(tagValue)) {
                if (!(tagValue.contains("www.flickr.com") || tagValue.contains("flic.kr"))) {
                  if (!tagValue.endsWith(".html")) {
                    println(tagValue)
                  }
                }
              }
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

  private def isImage(tagValue: String): Boolean = {
    val name = tagValue.toLowerCase()
    name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png")
  }
}
