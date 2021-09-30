package kpn.database.actions.subsets

import kpn.api.custom.Subset
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoQuery
import kpn.core.doc.OrphanRouteDoc
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQuerySubsetOrphanRoutes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanRoutes])
}

class MongoQuerySubsetOrphanRoutes(database: Database) {

  def execute(subset: Subset, log: Log = MongoQuerySubsetOrphanRoutes.log): Seq[OrphanRouteDoc] = {
    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.domain),
          equal("networkType", subset.networkType.name)
        )
      )
    )
    log.debugElapsed {
      val docs = database.orphanRoutes.aggregate[OrphanRouteDoc](pipeline, log)
      val message = s"subset ${subset.name} orphan routes: ${docs.size}"
      (message, docs)
    }
  }

  def ids(subset: Subset, log: Log = MongoQuerySubsetOrphanRoutes.log): Seq[Long] = {
    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.domain),
          equal("networkType", subset.networkType.name)
        )
      ),
      project(
        fields(
          include("_id")
        )
      )
    )
    log.debugElapsed {
      val ids = database.orphanRoutes.aggregate[Id](pipeline, log).map(_._id)
      val message = s"subset ${subset.name} orphan route ids: ${ids.size}"
      (message, ids)
    }
  }
}
