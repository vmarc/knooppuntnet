package kpn.database.actions.subsets

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.custom.Subset
import kpn.core.doc.OrphanRouteDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQuerySubsetOrphanRoutes {
  private val log = Log(classOf[MongoQuerySubsetOrphanRoutes])
}

class MongoQuerySubsetOrphanRoutes(database: Database) {

  def execute(subset: Subset, log: Log = MongoQuerySubsetOrphanRoutes.log): Seq[OrphanRouteDoc] = {
    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.toString),
          equal("routeTypes", subset.routeType.toString)
        )
      )
    )
    log.debugElapsed {
      val docs = database.orphanRoutes.aggregate(pipeline, classOf[OrphanRouteDoc], log)
      val message = s"subset ${subset.name} orphan routes: ${docs.size}"
      (message, docs)
    }
  }

  def ids(subset: Subset, log: Log = MongoQuerySubsetOrphanRoutes.log): Seq[Long] = {
    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.toString),
          equal("routeType", subset.routeType.toString)
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    log.debugElapsed {
      val ids = database.orphanRoutes.aggregate(pipeline, classOf[Id], log).map(_._id)
      val message = s"subset ${subset.name} orphan route ids: ${ids.size}"
      (message, ids)
    }
  }
}
