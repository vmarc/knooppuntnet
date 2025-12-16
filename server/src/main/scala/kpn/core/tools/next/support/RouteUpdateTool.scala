package kpn.core.tools.next.support

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo

object RouteUpdateTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      new RouteUpdateTool(database).execute3()
    }
  }
}

class RouteUpdateTool(database: Database) {

  private val log = Log(classOf[RouteUpdateTool])

  def execute(): Unit = {
    val ids = database.routes.ids()
    val idsCount = ids.length
    ids.sorted.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/$idsCount $routeId") {
        log.info("update")
        try {
          database.routes.findById(routeId) match {
            case None => log.warn("RouteDoc not found")
            case Some(routeDoc) =>
              database.baseRoutes.findById(routeId) match {
                case None => log.warn("BaseRouteDoc not found")
                case Some(baseRouteDoc) =>
                  baseRouteDoc.base.networkNodeIds match {
                    case None => // no update needed
                    case Some(networkNodeIds) =>
                      if (networkNodeIds.isEmpty) {
                        val updatedBaseRouteDoc = baseRouteDoc.copy(base = baseRouteDoc.base.copy(networkNodeIds = None))
                        database.baseRoutes.save(updatedBaseRouteDoc)
                      }
                      else {
                        val updatedRouteDoc = routeDoc.copy(base = routeDoc.base.copy(networkNodeIds = Some(networkNodeIds)))
                        database.routes.save(updatedRouteDoc)
                      }
                  }
              }
          }
        }
        catch {
          case e: Throwable => log.error("Could not update", e)
        }
      }
    }
  }

  def execute2(): Unit = {
    val ids = database.routes.ids()
    val idsCount = ids.length
    ids.sorted.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/$idsCount $routeId") {
        log.info("update")
        try {
          database.routes.findById(routeId) match {
            case None => log.warn("RouteDoc not found")
            case Some(routeDoc) =>
              if (routeDoc.structureRows.flatMap(_.relation).nonEmpty) {
                database.routes.save(routeDoc)
                log.warn("UPDATED")
              }
          }
        }
        catch {
          case e: Throwable => log.error("Could not update", e)
        }
      }
    }
  }

  def execute3(): Unit = {
    val ids = database.routes.ids()
    val idsCount = ids.length
    ids.sorted.zipWithIndex.foreach { case (routeId, index) =>
      Log.context(s"${index + 1}/$idsCount $routeId") {
        try {
          database.routes.findById(routeId) match {
            case None => log.warn("RouteDoc not found")
            case Some(routeDoc) => log.info("read")
          }
        }
        catch {
          case e: Throwable => log.error("Could not read", e)
        }
      }
    }
  }
}
