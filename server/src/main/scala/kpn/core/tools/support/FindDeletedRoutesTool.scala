package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.server.repository.RouteRepositoryImpl

import scala.xml.XML

object FindDeletedRoutesTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: FindDeletedRoutesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    val executor = new OverpassQueryExecutorImpl()
    Couch.executeIn(host, analysisDatabaseName) { database =>
      new FindDeletedRoutesTool(database, executor).report()
    }
  }
}

class FindDeletedRoutesTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

  private val routeRepository = new RouteRepositoryImpl(null, database, false)

  def report(): Unit = {
    println("Collecting route ids")
    val routeIds = routeRepository.allRouteIds()
    println(s"${routeIds.size} routes")
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 500 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      if (!overpassContainsRoute(routeId)) {
        println("route=" + routeId)
      }
    }
    println("done")
  }

  private def overpassContainsRoute(routeId: Long): Boolean = {
    val query = QueryRelation(routeId)
    val xmlString = overpassQueryExecutor.executeQuery(None, query)
    val xml = XML.loadString(xmlString)
    (xml \ "relation").map { n => (n \ "@id").text.toLong }.toSet.contains(routeId)
  }
}
