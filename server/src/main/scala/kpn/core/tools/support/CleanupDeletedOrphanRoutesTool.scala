package kpn.core.tools.support

import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import kpn.core.app.ActorSystemConfig
import kpn.core.db.RouteDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.server.repository.OrphanRepositoryImpl
import kpn.shared.Subset
import spray.can.Http
import spray.http.HttpMethods.GET
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.StatusCodes
import spray.http.Uri

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt
import scala.xml.XML

object CleanupDeletedOrphanRoutesTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-server", "master3") { database =>
      new CleanupDeletedOrphanRoutesTool(database).cleanup()
    }
  }
}

class CleanupDeletedOrphanRoutesTool(database: Database) {

  private val system = ActorSystemConfig.actorSystem()
  private val orphanRepository = new OrphanRepositoryImpl(database)
  private val timeout: Timeout = Timeout(900.seconds)

  def cleanup(): Unit = {
    Subset.all.foreach(cleanupSubset)
  }

  private def cleanupSubset(subset: Subset): Unit = {
    val subsetOrphanRouteIds = orphanRepository.orphanRoutes(subset).map(_.id)
    if (subsetOrphanRouteIds.nonEmpty) {
      val routeDocs = subsetOrphanRouteIds.flatMap(readRoute)
      val activeRouteDocs = routeDocs.filter(_.route.active)
      val deletedRouteDocs = activeRouteDocs.filter(d => isRouteDeleted(d.route.id))
      println(s"${subset.string} orphanRouteCount=${subsetOrphanRouteIds.size} active=${activeRouteDocs.size} deleted=${deletedRouteDocs.size}")
      deletedRouteDocs.foreach { routeDoc =>
        val newRouteDoc = routeDoc.copy(route = routeDoc.route.copy(active = false))
        database.save(newRouteDoc)
      }
    }
  }

  private def readRoute(routeId: Long): Option[RouteDoc] = {
    database.docWithId(s"route:$routeId", classOf[RouteDoc])
  }

  private def isRouteDeleted(routeId: Long): Boolean = {
    try {
      val request = HttpRequest(GET, Uri(s"https://www.openstreetmap.org/api/0.6/relation/$routeId/history"))
      val responseFuture = (IO(Http)(system).ask(request)(timeout)).mapTo[HttpResponse]
      val response = Await.result(responseFuture, Duration.Inf)
      response.status match {
        case StatusCodes.OK =>
          val xmlString = response.entity.data.asString
          val xx = xmlString.drop("""<?xml version="1.0" encoding="UTF-8"?>""".length + 1)
          val xml: scala.xml.Node = XML.loadString(xx)
          val osm = xml \\ "osm"
          val relations = osm \ "relation"
          val oldestRelation = relations.last
          (oldestRelation \ "@visible").text == "false"
        case _ =>
          println(s"Could not read history of route $routeId")
          false
      }
    }
    catch {
      case e: Exception =>
        println(s"route $routeId: ${e.getMessage}")
        false
    }
  }
}
