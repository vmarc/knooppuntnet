package kpn.server.repository

import kpn.api.common.changes.details.ChangeKey
import kpn.core.mongo.Database
import kpn.core.util.Log
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.springframework.stereotype.Component

@Component
class MonitorAdminRouteRepositoryImpl(database: Database) extends MonitorAdminRouteRepository {

  private val log = Log(classOf[MonitorAdminRouteRepositoryImpl])

  override def allRouteIds: Seq[Long] = {
    database.monitorRoutes.ids(log)
  }

  override def saveRoute(route: MonitorRoute): Unit = {
    database.monitorRoutes.save(route, log)
  }

  override def saveRouteState(routeState: MonitorRouteState): Unit = {
    database.monitorRouteStates.save(routeState, log)
  }

  override def saveRouteReference(routeReference: MonitorRouteReference): Unit = {
    database.monitorRouteReferences.save(routeReference, log)
  }

  override def saveRouteChange(routeChange: MonitorRouteChange): Unit = {
    database.monitorRouteChanges.save(routeChange, log)
  }

  override def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit = {
    database.monitorRouteChangeGeometries.save(routeChangeGeometry, log)
  }

  override def route(routeId: Long): Option[MonitorRoute] = {
    database.monitorRoutes.findById(routeId, log)
  }

  override def routeState(routeId: Long): Option[MonitorRouteState] = {
    database.monitorRouteStates.findById(routeId, log)
  }

  override def routeReference(routeId: Long, key: String): Option[MonitorRouteReference] = {
    database.monitorRouteReferences.findOne(
      filter(
        and(
          equal("_id", routeId),
          equal("key", key)
        )
      ),
      log
    )
  }

  override def routeChange(changeKey: ChangeKey): Option[MonitorRouteChange] = {
    database.monitorRouteChanges.findOne(
      filter(
        and(
          equal("key.elementId", changeKey.elementId),
          equal("key.changeSetId", changeKey.changeSetId),
          equal("key.replicationNumber", changeKey.replicationNumber)
        )
      ),
      log
    )
  }

  override def routeChangeGeometry(changeKey: ChangeKey): Option[MonitorRouteChangeGeometry] = {
    database.monitorRouteChangeGeometries.findOne(
      filter(
        and(
          equal("key.elementId", changeKey.elementId),
          equal("key.changeSetId", changeKey.changeSetId),
          equal("key.replicationNumber", changeKey.replicationNumber)
        )
      ),
      log
    )
  }

  def routeReferenceKey(routeId: Long): Option[String] = {
    // TODO MONGO should be looking for most recent entry here, instead of assuming there is always exactly 1 entry ???
    database.monitorRouteReferences.findOne(
      filter(
        equal("routeId", routeId),
      ),
      log
    )
  }
}
