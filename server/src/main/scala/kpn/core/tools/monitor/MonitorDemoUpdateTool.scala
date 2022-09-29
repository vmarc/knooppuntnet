package kpn.core.tools.monitor

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

import scala.xml.XML

object MonitorDemoUpdateTool {
  private val log = Log(classOf[MonitorDemoUpdateTool])

  def main(args: Array[String]): Unit = {

    val exit: Int = try {
      MonitorDemoUpdateToolOptions.parse(args) match {
        case Some(options) =>
          Mongo.executeIn(options.databaseName) { database =>
            val overpassQueryExecutor = new OverpassQueryExecutorImpl()
            new MonitorDemoUpdateTool(database, overpassQueryExecutor).analyze()
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

  private val log = Log(classOf[MonitorDemoUpdateTool])
  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)
  private val now = Time.now

  def analyze(): Unit = {
    val routes = collectRoutes()
    routes.zipWithIndex.foreach { case (route, index) =>
      Log.context(s"${index + 1}/${routes.size}") {
        groupRepository.groupById(route.groupId) match {
          case None => log.warn(s"route ${route.name} (${route._id.oid}) - group with id ${route.groupId.oid} not found")
          case Some(group) =>
            Log.context(s"${group.name}/${route.name}") {
              analyzeRoute(route)
            }
        }
      }
    }
  }

  private def analyzeRoute(route: MonitorRoute): Unit = {
    route.relationId match {
      case None => log.info("no analysis - relationId definition missing")
      case Some(relationId) =>
        readRelation(relationId) match {
          case None => log.info(s"""no analysis - relation with id "$relationId" not found in overpass""")
          case Some(routeRelation) =>
            routeRepository.routeReferenceRouteWithId(route._id) match {
              case None => log.info(s"""no analysis - reference definition missing""")
              case Some(routeReference) =>
                if (routeReference.geometry.isEmpty) {
                  log.info(s"""no analysis - reference definition incomplete""")
                }
                else {
                  analyze(route, routeReference, routeRelation, now)
                }
            }
        }
    }
  }

  private def collectRoutes(): Seq[MonitorRoute] = {
    val pipeline = Seq(
      sort(orderBy(ascending("groupId", "name"))),
    )
    database.monitorRoutes.aggregate[MonitorRoute](pipeline).sortBy(_._id)
  }

  private def readRelation(routeId: Long): Option[Relation] = {
    val xmlString = overpassQueryExecutor.executeQuery(Some(now), QueryRelation(routeId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(routeId)
  }

  private def analyze(
    route: MonitorRoute,
    routeReference: MonitorRouteReference,
    routeRelation: Relation,
    now: Timestamp
  ): Unit = {
    val analyzedRouteState = new MonitorDemoAnalyzer().analyze(route, routeReference, routeRelation, now)
    val newRouteState = routeRepository.routeState(route._id) match {
      case None =>
        // no previous state found, so we save the analysis result as is
        log.info(s"""save first time analysis""")
        analyzedRouteState
      case Some(previousState) =>
        val stateWithSameIdAndTimestamp = previousState.copy(
          _id = analyzedRouteState._id,
          timestamp = analyzedRouteState.timestamp
        )
        if (analyzedRouteState == stateWithSameIdAndTimestamp) {
          // no change: previous analysis result is the same as the current analysis result
          // we update the document already in the database to save the last analysis timestamp
          log.info("same analysis result (updating timestamp only)")
          analyzedRouteState.copy(_id = previousState._id)
        }
        else {
          // the current analysis result is different from the previous analysis result
          // we keep the previous analysis document and save the new analysis document
          log.info("new analysis result")
          analyzedRouteState
        }
    }
    routeRepository.saveRouteState(newRouteState)
  }
}
