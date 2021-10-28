package kpn.server.analyzer.engine.analysis.post

import kpn.database.base.Database
import kpn.database.base.Id
import kpn.core.doc.Label
import kpn.core.doc.OrphanRouteDoc
import kpn.database.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.out
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.springframework.stereotype.Component

object OrphanRouteUpdater {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
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
