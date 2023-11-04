package kpn.core.tools.monitor

import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.MonitorUtil

object OsmSegmentReportTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val tool = new OsmSegmentReportTool(groupRepository, routeRepository)
      tool.report("fr-iwn-Camino", "VPSM")
    }
  }
}

class OsmSegmentReportTool(
  groupRepository: MonitorGroupRepository,
  routeRepository: MonitorRouteRepository
) {
  def report(groupName: String, routeName: String): Unit = {
    groupRepository.groupByName(groupName) match {
      case None => println("Group not found")
      case Some(group) =>
        routeRepository.routeByName(group._id, routeName) match {
          case None => println("Route not found")
          case Some(route) => reportOsmSegments(route)
        }
    }
  }

  private def reportOsmSegments(route: MonitorRoute): Unit = {
    route.osmSegments.zipWithIndex.foreach { case (osmSegment, index) =>
      println(s"Segment ${index + 1}\n")
      print(s"|relationId")
      print(s"|segmentId")
      print(s"|meters")
      print(s"|reversed")
      print(s"|name")
      println("|")
      println("|-|-|-|-|-|")
      osmSegment.elements.foreach { osmSegmentElement =>
        print(s"|${osmSegmentElement.relationId}")
        print(s"|${osmSegmentElement.segmentId}")
        print(s"|${osmSegmentElement.meters}")
        print(s"|${if (osmSegmentElement.reversed) "**reversed**" else "-"}")
        val name = MonitorUtil.subRelation(route, osmSegmentElement.relationId) match {
          case None => "?"
          case Some(subRelation) => subRelation.name
        }
        print(s"|$name")
        println("|")
      }
      println("")
    }
  }
}
