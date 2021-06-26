package kpn.core.mongo.migration

import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroup
import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorAdminRouteRepositoryImpl
import kpn.server.repository.MonitorGroupRepositoryImpl
import kpn.server.repository.MonitorRouteRepositoryImpl

object MigrateMonitorTool {

  private val log = Log(classOf[MigrateMonitorTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      Couch.executeIn("kpn-database", "monitor") { couchDatabase =>
        new MigrateMonitorTool(couchDatabase, database).migrate()
      }
    }
    log.info("Done")
  }
}

class MigrateMonitorTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val monitorGroupRepository = new MonitorGroupRepositoryImpl(null, couchDatabase, false)
  private val monitorRouteRepository = new MonitorRouteRepositoryImpl(null, couchDatabase, false)
  private val monitorAdminRouteRepository = new MonitorAdminRouteRepositoryImpl(null, couchDatabase, false)

  def migrate(): Unit = {
    monitorGroupRepository.groups().foreach { group =>
      database.monitorGroups.save(group.toMongo)
      migrateGroupRoutes(group)
    }
  }

  private def migrateGroupRoutes(group: MonitorGroup): Unit = {
    monitorGroupRepository.groupRoutes(group.name).foreach { route =>
      database.monitorRoutes.save(route.toMongo)
      migrateRouteState(route)
      migrateRouteReference(route)
      migrateRouteChanges(route)
    }
  }

  private def migrateRouteState(route: MonitorRoute): Unit = {
    monitorRouteRepository.routeState(route.id) match {
      case Some(routeState) => database.monitorRouteStates.save(routeState.toMongo)
      case None =>
    }
  }

  private def migrateRouteReference(route: MonitorRoute): Unit = {
    monitorAdminRouteRepository.routeReferenceKey(route.id) match {
      case None =>
      case Some(key) =>
        monitorRouteRepository.routeReference(route.id, key) match {
          case Some(routeReference) => database.monitorRouteReferences.save(routeReference.toMongo)
          case None =>
        }
    }
  }

  private def migrateRouteChanges(route: MonitorRoute): Unit = {
    val all = MonitorChangesParameters(1000)
    val changes = monitorRouteRepository.routeChanges(route.id, all)
    database.monitorRouteChanges.insertMany(changes.map(change => change.toMongo))
    changes.foreach { change =>
      monitorRouteRepository.routeChangeGeometry(route.id, change.key.changeSetId, change.key.replicationNumber) match {
        case Some(changeGeometry) => database.monitorRouteChangeGeometries.save(changeGeometry.toMongo)
        case None =>
      }
    }
  }
}
