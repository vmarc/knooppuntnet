package kpn.core.tools.monitor

import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteStateAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteStateUpdater
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.apache.commons.io.FileUtils

import java.io.File
import scala.xml.XML

object MonitorPerformanceAnalysisTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      new MonitorPerformanceAnalysisTool(database).analyze()
    }
  }
}

class MonitorPerformanceAnalysisTool(database: Database) {

  private val log = Log(classOf[MonitorPerformanceAnalysisTool])
  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)

  private val now = Time.now

  def analyze(): Unit = {
    groupRepository.groupByName("AAA") match {
      case None => println("group not found")
      case Some(group) =>
        routeRepository.routeByName(group._id, "Hexatrek") match {
          case None => println("route not found")
          case Some(route) =>
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
    }
  }

  private def readRelation(relationId: Long): Option[Relation] = {
    log.infoElapsed(
      "read relation",
      {
        val xmlString = FileUtils.readFileToString(new File(s"/kpn/monitor/$relationId.xml"), "UTF-8")
        val xml = XML.loadString(xmlString)
        val rawData = new Parser().parse(xml.head)
        val data = new DataBuilder(rawData).data
        data.relations.get(relationId)
      }
    )
  }

  private def analyze(
    route: MonitorRoute,
    routeReference: MonitorRouteReference,
    routeRelation: Relation,
    now: Timestamp
  ): Unit = {
    val analyzer = new MonitorRouteStateAnalyzer()
    val analyzedRouteState = analyzer.analyze(route, routeReference, routeRelation, now)
    println(s"wayCount=${analyzedRouteState.wayCount}")
    println(s"osmDistance=${analyzedRouteState.osmDistance}")
    println(s"osmSegments.size=${analyzedRouteState.osmSegments.size}")
    println(s"deviations.size=${analyzedRouteState.deviations.size}")
    new MonitorRouteStateUpdater(monitorRouteRepository).update(route, analyzedRouteState, routeRelation)
  }
}
