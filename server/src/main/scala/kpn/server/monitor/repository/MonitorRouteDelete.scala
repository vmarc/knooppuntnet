package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal

class MonitorRouteDelete(database: Database) {

  def delete(routeId: ObjectId, log: Log): Unit = {
    database.monitorRoutes.deleteByObjectId(routeId, log)
    val routeFilter = equal("routeId", routeId.raw)
    database.monitorReferences.deleteMany(routeFilter, log)
    database.monitorStates.deleteMany(routeFilter, log)
    database.monitorRouteChanges.deleteMany(routeFilter, log)
    database.monitorRouteChangeGeometries.deleteMany(routeFilter, log)
  }
}
