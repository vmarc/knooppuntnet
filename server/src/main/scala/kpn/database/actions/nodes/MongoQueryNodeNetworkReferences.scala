package kpn.database.actions.nodes

import kpn.api.common.common.Reference
import kpn.database.base.Database
import kpn.database.base.Id
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
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
        filter(equal("active", true)),
        unwind("$nodes"),
        filter(equal("nodes.id", nodeId)),
        project(
          fields(
            excludeId(),
            computed("networkType", "$summary.networkType"),
            computed("networkScope", "$summary.networkScope"),
            computed("id", "$_id"),
            computed("name", "$summary.name"),
          )
        )
      )
      val references = database.networkInfos.aggregate[Reference](pipeline, log)
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
