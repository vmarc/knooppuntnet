package kpn.core.tools.support

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelationIds
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.RouteRepositoryImpl

import scala.xml.XML

/*
  Find routes that are active in the mongodb database, but that are deleted
  in OpenStreetMap and do not exist in the OverpassApi database anymore
  at the current time.
 */
object FindDeletedRoutesTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 1) {
      println("Usage: FindDeletedRoutesTool analysisDatabaseName")
      System.exit(-1)
    }
    val analysisDatabaseName = args(0)
    val executor = new OverpassQueryExecutorRemoteImpl()
    Mongo.executeIn(analysisDatabaseName) { database =>
      new FindDeletedRoutesTool(database, executor).report()
    }
  }
}

class FindDeletedRoutesTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[FindDeletedRoutesTool])

  private val routeRepository = new RouteRepositoryImpl(database)

  def report(): Unit = {
    log.info("Collecting route ids")
    val routeIds = routeRepository.activeRouteIds()
    log.info(s"${routeIds.size} active routes")
    val batchSize = 1000
    val deletedRouteIds = routeIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (batchRouteIds, index) =>
      val progress = s"${index * batchSize}/${routeIds.size}"
      val deletedRouteIds = findDeletedRouteIds(batchRouteIds)
      if (deletedRouteIds.nonEmpty) {
        log.info(s"$progress deleted route(s)=${deletedRouteIds.mkString(", ")}")
      }
      else {
        log.info(progress)
      }
      deletedRouteIds
    }
    log.info("deleted route(s)=" + deletedRouteIds.mkString(", "))
    log.info("done")
  }

  private def findDeletedRouteIds(routeIds: Seq[Long]): Seq[Long] = {
    val query = QueryRelationIds(routeIds)
    val xmlString = overpassQueryExecutor.executeQuery(None, query)
    val xml = XML.loadString(xmlString)
    val activeRouteIds = (xml \ "relation").map { n => (n \ "@id").text.toLong }.toSet
    (routeIds.toSet -- activeRouteIds).toSeq.sorted
  }
}
