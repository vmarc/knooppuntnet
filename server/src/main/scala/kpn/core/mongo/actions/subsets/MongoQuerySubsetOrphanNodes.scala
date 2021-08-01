package kpn.core.mongo.actions.subsets

import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes.log
import kpn.core.mongo.doc.OrphanNodeDoc
import kpn.core.mongo.util.Id
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object MongoQuerySubsetOrphanNodes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanNodes])
}

class MongoQuerySubsetOrphanNodes(database: Database) extends MongoQuery {

  def execute(subset: Subset): Seq[OrphanNodeDoc] = {

    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.domain),
          equal("networkType", subset.networkType.name),
        )
      )
    )

    log.debugElapsed {
      val docs = database.orphanNodes.aggregate[OrphanNodeDoc](pipeline, log)
      val message = s"subset ${subset.name} orphan nodes: ${docs.size}"
      (message, docs)
    }
  }

  def ids(subset: Subset): Seq[Long] = {

    val pipeline = Seq(
      filter(
        and(
          equal("country", subset.country.domain),
          equal("networkType", subset.networkType.name),
        )
      ),
      project(
        fields(
          computed("_id", "$nodeId")
        )
      )
    )

    log.debugElapsed {
      val ids = database.orphanNodes.aggregate[Id](pipeline, log).map(_._id)
      val message = s"subset ${subset.name} orphan node ids: ${ids.size}"
      (message, ids)
    }
  }
}
