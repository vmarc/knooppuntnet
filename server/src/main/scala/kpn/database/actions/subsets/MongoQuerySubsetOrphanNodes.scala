package kpn.database.actions.subsets

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import kpn.api.custom.Subset
import kpn.core.doc.OrphanNodeDoc
import kpn.core.util.Log
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes.log
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter

object MongoQuerySubsetOrphanNodes {
  private val log = Log(classOf[MongoQuerySubsetOrphanNodes])
}

class MongoQuerySubsetOrphanNodes(database: Database) {

  def execute(subset: Subset): Seq[OrphanNodeDoc] = {

    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.entryName),
          equal("routeType", subset.routeType.entryName),
        )
      )
    )

    log.debugElapsed {
      val docs = database.orphanNodes.aggregate(pipeline, classOf[OrphanNodeDoc], log)
      val message = s"subset ${subset.name} orphan nodes: ${docs.size}"
      (message, docs)
    }
  }

  def ids(subset: Subset): Seq[Long] = {

    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.entryName),
          equal("routeType", subset.routeType.entryName),
        )
      ),
      project(
        fields(
          computed("_id", "$nodeId")
        )
      )
    )

    log.debugElapsed {
      val ids = database.orphanNodes.aggregate(pipeline, classOf[Id], log).map(_._id)
      val message = s"subset ${subset.name} orphan node ids: ${ids.size}"
      (message, ids)
    }
  }
}
