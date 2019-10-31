package kpn.core.tools

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.RouteRepositoryImpl

object SurveyDateTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("localhost", "master2b") { database =>
      new SurveyDateTool(database).analyze()
    }
  }
}

class SurveyDateTool(database: Database) {

  def analyze(): Unit = {

    val routeRepository = new RouteRepositoryImpl(database)

    val counts = new scala.collection.mutable.HashMap[String, Seq[Long]]

    val routeIds = DocumentView.allRouteIds(database) //.take(5)
    routeIds.foreach { routeId =>

      routeRepository.routeWithId(routeId) match {
        case Some(route) =>
          route.tags.tags.foreach { tag =>
            val key = tag.key.toLowerCase()
            if (key.contains("survey") || key.contains("check") || key.contains("source")) {
              val value = tag.key + "=" + tag.value
              println(value + " -> " + routeId)
              if (counts.contains(value)) {
                counts.put(value, counts(value) :+ routeId)
              }
              else {
                counts.put(value, Seq(routeId))
              }
            }
          }

        case None =>
      }
    }

    println("result")
    counts.keys.toSeq.sorted.foreach { key =>
      val routeIds = counts(key)
      println(key + "\t" + routeIds.size + "\t" + routeIds)
    }
  }

}
