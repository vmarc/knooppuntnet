package kpn.core.tools.monitor

import kpn.api.custom.Relation
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.tools.monitor.MonitorDemoUpdateTool.log
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.gt

import scala.xml.XML

object MonitorDemoUpdateTool {
  private val log = Log(classOf[MonitorDemoUpdateTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      MonitorDemoUpdateToolOptions.parse(args) match {
        case Some(options) =>
          Mongo.executeIn(options.databaseName) { database =>
            val overpassQueryExecutor = new OverpassQueryExecutorImpl()
            new MonitorDemoUpdateTool(database, overpassQueryExecutor).update()
          }
          log.info("Done")
          0

        case None =>
          // arguments are bad, error message will have been displayed
          -1
      }
    }
    catch {
      case e: Throwable =>
        log.error(e.getMessage, e)
        -1
    }

    System.exit(exit)
  }
}

class MonitorDemoUpdateTool(
  database: Database,
  overpassQueryExecutor: OverpassQueryExecutor
) {

  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  private val now = Time.now

  def update(): Unit = {
    val routes = collectRoutes()
    routes.zipWithIndex.foreach { case (route, index) =>
      Log.context(s"${index + 1}/${routes.size}") {
        log.info(route._id.oid)
        updateRoute(route)
      }
    }
  }

  private def updateRoute(route: MonitorRoute): Unit = {
    route.relationId match {
      case None =>
      case Some(relationId) =>
        val routeRelation = readRelation(relationId)
        monitorRouteRepository.routeReference(route.name /*TODO better would be route._id ??*/) match {
          case None => log.error("route reference not found")
          case Some(routeReference) =>
            val monitorRouteState = new MonitorDemoAnalyzer().analyze(route, routeReference, routeRelation, now)
            monitorRouteRepository.saveRouteState(monitorRouteState)
        }
    }
  }

  private def collectRoutes(): Seq[MonitorRoute] = {
    val pipeline = Seq(
      filter(
        gt("routeId", 1),
      )
    )
    database.monitorRoutes.aggregate[MonitorRoute](pipeline).sortBy(_._id)
  }

  private def readRelation(routeId: Long): Relation = {
    val xmlString = overpassQueryExecutor.executeQuery(Some(now), QueryRelation(routeId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations(routeId)
  }
}
