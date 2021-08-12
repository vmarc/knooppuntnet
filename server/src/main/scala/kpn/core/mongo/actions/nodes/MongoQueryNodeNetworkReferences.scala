package kpn.core.mongo.actions.nodes

import kpn.api.common.common.Reference
import kpn.core.mongo.Database
import kpn.core.mongo.util.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.in
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object MongoQueryNodeNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeNetworkReferences])
}

class MongoQueryNodeNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeNetworkReferences.log): Seq[Reference] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("active", true),
            equal("nodeRefs", nodeId),
          )
        ),
        project(
          fields(
            excludeId(),
            computed("networkType", "$attributes.networkType"),
            computed("networkScope", "$attributes.networkScope"),
            computed("id", "$_id"),
            computed("name", "$attributes.name"),
          )
        )
      )
      val references = database.oldNetworks.aggregate[Reference](pipeline, log)
      (s"node network references: ${references.size}", references)
    }
  }

  def executeNodeIds(nodeIds: Seq[Long], log: Log = MongoQueryNodeNetworkReferences.log): Seq[Long] = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(equal("active", true)),
        unwind("$relationMembers"),
        filter(in("relationMembers.nodeId", nodeIds: _*)),
        project(
          fields(
            include("_id")
          )
        )
      )
      val ids = database.networks.aggregate[Id](pipeline, log).map(_._id)
      (s"network references: ${ids.size}", ids)
    }
  }
}
