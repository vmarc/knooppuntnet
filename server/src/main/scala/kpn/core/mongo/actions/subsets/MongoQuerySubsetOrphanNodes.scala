package kpn.core.mongo.actions.subsets

import kpn.api.common.NodeInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes.log
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object MongoQuerySubsetOrphanNodes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanNodes])
}

class MongoQuerySubsetOrphanNodes(database: Database) extends MongoQuery {

  def execute(subset: Subset): Seq[NodeInfo] = {

    val pipeline = Seq(
      filter(
        and(
          equal("labels", "active"),
          equal("labels", "orphan"),
          equal("labels", s"location-${subset.country.domain}"),
          equal("labels", s"network-type-${subset.networkType.name}")
        )
      ),
      unwind("$names"),
      filter(
        equal("names.networkType", subset.networkType.name),
      ),
      sort(orderBy(ascending("names.name"))),
      BsonDocument("""{"$set": { "names": ["$names"]}}"""),
      project(
        fields(
          excludeId()
        )
      )
    )

    log.debugElapsed {
      val nodes = database.nodes.aggregate[NodeInfo](pipeline, log)
      val result = s"subset ${subset.name} orphan nodes: ${nodes.size}"
      (result, nodes)
    }
  }
}
