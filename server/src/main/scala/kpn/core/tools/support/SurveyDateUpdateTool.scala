package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.database.doc.NodeDoc
import kpn.core.database.doc.RouteDoc
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch

object SurveyDateUpdateTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "analysis") { database =>
      new SurveyDateUpdateTool(database).update()
    }
  }
}

class SurveyDateUpdateTool(database: Database) {

  def update(): Unit = {
    updateNodes()
    updateRoutes()
  }

  private def updateNodes(): Unit = {

    println(s"reading nodeIds")
    val nodeIds = DocumentView.allNodeIds(database)
    println(s"processing ${nodeIds.size} nodes")
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }
      val docId = s"node:$nodeId"
      database.docWithId(docId, classOf[NodeDoc]) match {
        case None =>
        case Some(doc) =>
          if (doc.node.lastSurvey.isDefined) {
            println(s"update node $nodeId")
            database.save(doc)
          }
      }
    }
    println("done")
  }

  private def updateRoutes(): Unit = {

    println(s"reading routeIds")
    val routeIds = DocumentView.allRouteIds(database)
    println(s"processing ${routeIds.size} routes")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      val docId = s"route:$routeId"
      database.docWithId(docId, classOf[RouteDoc]) match {
        case None =>
        case Some(doc) =>
          if (doc.route.lastSurvey.isDefined) {
            println(s"update route $routeId")
            database.save(doc)
          }
      }
    }
    println("done")
  }
}
