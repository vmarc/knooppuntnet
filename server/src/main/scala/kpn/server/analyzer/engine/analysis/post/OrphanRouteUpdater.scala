package kpn.server.analyzer.engine.analysis.post

import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.springframework.stereotype.Component

object OrphanRouteUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-3") { database =>
      new OrphanRouteUpdater(database).update()
    }
  }
}

@Component
class OrphanRouteUpdater(database: Database) {

  private val log = Log(classOf[OrphanRouteUpdater])

  def update(): Unit = {
    log.debugElapsed {
      val allRouteIds = findAllRouteIds()
      val routeIdsReferencedInNetworks = findRouteIdsReferencedInNetworks()
      val routeIds = (allRouteIds.toSet -- routeIdsReferencedInNetworks).toSeq.sorted
      updateOrphanRoutes(routeIds)
      ("done", ())
    }
  }

  private def findAllRouteIds(): Seq[Long] = {
    new OrphanRouteUpdater_AllRouteIds(database, log).execute()
  }

  private def findRouteIdsReferencedInNetworks(): Seq[Long] = {
    new OrphanRouteUpdater_ReferencesInNetworks(database, log).execute()
  }

  private def updateOrphanRoutes(routeIds: Seq[Long]): Unit = {
    new OrphanRouteUpdater_Update(database, log).execute(routeIds)
  }
}
