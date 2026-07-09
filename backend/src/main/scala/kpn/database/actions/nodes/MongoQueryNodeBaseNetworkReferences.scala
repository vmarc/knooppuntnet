package kpn.database.actions.nodes

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.common.common.Reference
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline

object MongoQueryNodeBaseNetworkReferences {
  private val log = Log(classOf[MongoQueryNodeBaseNetworkReferences])
}

class MongoQueryNodeBaseNetworkReferences(database: Database) {

  def execute(nodeId: Long, log: Log = MongoQueryNodeBaseNetworkReferences.log): Seq[Reference] = {
    val pipeline = buildPipeline(nodeId)
    log.infoElapsed {
      val references = database.baseNetworks.aggregate(pipeline, classOf[Reference], log)
      val updatedReferences = references.map { ref =>
        if (ref.name == null) {
          ref.copy(name = "") // needed because BaseNetworkDoc.name is Option[String]
        }
        else {
          ref
        }
      }
      (s"node network references: ${updatedReferences.size}", updatedReferences)
    }
  }

  private def buildPipeline(nodeId: Long): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("nodeIds", nodeId),
        )
      ),
      unwind("$base.members"),
      filter(
        and(
          equal("base.members.memberType", "node"),
          equal("base.members.ref", nodeId),
        )
      ),
      project(
        fields(
          excludeId(),
          computed("routeType", "$base.routeType"),
          computed("routeScope", "$base.routeScope"),
          computed("id", "$_id"),
          computed("name", "$base.name"),
          computed("role", "$base.members.role"),
        )
      )
    )
  }
}
