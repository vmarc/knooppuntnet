package kpn.core.tools.monitor

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.locationtech.jts.io.geojson.GeoJsonReader

object MonitorRouteMigrationTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      val tool = new MonitorRouteMigrationTool(database)
      // tool.migrateOne("fr-iwn-Camino", "Voie-Puy")
      tool.migrate()
    }
  }
}

class MonitorRouteMigrationTool(database: Database) {

  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)

  def migrateOne(groupName: String, routeName: String): Unit = {
    groupRepository.groupByName(groupName) match {
      case None => println("group not found")
      case Some(group) =>
        routeRepository.routeByName(group._id, routeName) match {
          case None => println("route not found")
          case Some(route) => migrateRoute(group, route)
        }
    }
  }

  def migrate(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        migrateRoute(group, route)
      }
    }
  }

  private def migrateRoute(group: MonitorGroup, route: MonitorRoute): Unit = {
    println(s"${group.name}:${route.name}")
    routeRepository.routeReferenceRouteWithId(route._id) match {
      case None => println("Could not read reference")
      case Some(reference) =>
        updateRouteReferenceDistance(route, reference)
    }
  }

  private def updateRouteReferenceDistance(route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    if (reference.geometry.nonEmpty) {
      val geometry = new GeoJsonReader().read(reference.geometry)
      val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometry)
      val referenceDistance = Math.round(toMeters(referenceLineStrings.map(_.getLength).sum))
      val migratedRoute = route.copy(
        referenceDistance = referenceDistance
      )
      routeRepository.saveRoute(migratedRoute)
    }
  }
}
