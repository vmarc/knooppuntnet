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
      val wheelchairTags = poiRefs.zipWithIndex.flatMap { case (poiRef, index) =>
        if (index % 1000 == 0) {
          println(s"$index/${poiRefs.size}")
        }
        repo.get(poiRef).toSeq.flatMap { poi =>
          poi.tags.tags.filter { tag =>
            tag.key.contains("wheelchair") || tag.value.contains("wheelchair")
          }
        }
      }

      val tagStrings = wheelchairTags.map(tag => s"${tag.key}=${tag.value}")

      val tagFrequencies = tagStrings.foldLeft(Map.empty[String, Int]) {
        (count, word) => count + (word -> (count.getOrElse(word, 0) + 1))
      }

      tagFrequencies.toSeq.sortBy(_._2).reverse.foreach { kv => println(s"${kv._2} -> ${kv._1}") }
    }
  }

  private def loadPoiRefs(repo: PoiRepository): Seq[PoiRef] = {
    repo.nodeIds().map(PoiRef.node) ++
      repo.wayIds().map(PoiRef.way) ++
      repo.relationIds().map(PoiRef.relation)
  }
}
