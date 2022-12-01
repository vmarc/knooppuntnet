package kpn.core.tools.monitor

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

object MonitorRouteMigrationTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      new MonitorRouteMigrationTool(database).migrate()
    }
  }
}

class MonitorRouteMigrationTool(database: Database) {

  private val groupRepository = new MonitorGroupRepositoryImpl(database)
  private val routeRepository = new MonitorRouteRepositoryImpl(database)

  def migrate(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"${group.name}:${route.name}")
        routeRepository.routeState(route._id) match {
          case None => println("  state not found")
          case Some(state) =>
            routeRepository.routeReferenceRouteWithId(route._id) match {
              case None => println("  reference not found")
              case Some(reference) =>
                val referenceDistance = state.gpxDistance // km
                val deviationDistance = Math.round(state.deviations.map(_.distance).sum.toFloat / 1000)
                val deviationCount = state.deviations.size
                val osmSegmentCount = state.osmSegments.size
                val happy = referenceDistance > 0 && deviationCount == 0 && osmSegmentCount == 1
                val migratedRoute = route.copy(
                  referenceType = Some(reference.referenceType),
                  referenceDay = Some(reference.created.toDay),
                  referenceFilename = reference.filename,
                  referenceDistance = referenceDistance,
                  deviationDistance = deviationDistance,
                  deviationCount = deviationCount,
                  osmWayCount = state.wayCount,
                  osmDistance = state.osmDistance,
                  osmSegmentCount = osmSegmentCount,
                  happy = happy
                )
                routeRepository.saveRoute(migratedRoute)
            }
        }
      }
    }
  }

  def migrate2(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"${group.name}:${route.name}")
        val happy = route.referenceDistance > 0 && route.deviationCount == 0 && route.osmSegmentCount == 1
        val migratedRoute = route.copy(
          happy = happy
        )
        routeRepository.saveRoute(migratedRoute)
      }
    }
  }
}
